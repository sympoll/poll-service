package com.MTAPizza.Sympoll.pollmanagementservice;

import com.MTAPizza.Sympoll.pollmanagementservice.dto.poll.PollResponse;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PollManagementServiceApplicationTests {
    private static int pollId;

    /**
    * Initialize postgres test container with the init script inside poll-management-service/test/resources
    * */
    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("postgres:16.2").withInitScript("init.sql");

    @LocalServerPort
    private Integer port;

    @BeforeEach
    void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    /**
     * Run mock test container.
     */
    static {
        postgreSQLContainer.start();
    }


    /**
     * Send request to create the specified poll.
     */
    @Test
    @Order(1)
    void shouldCreatePoll() {
        String requestBodyProg = """
                {
                  "title": "Favorite Programming Language",
                  "description": "Vote for your favorite programming language",
                  "numAnswersAllowed": 1,
                  "creatorId": 123,
                  "groupId": 456,
                  "deadline": "2024-07-22T10:00:00.000Z",
                  "answers": [
                    "Java",
                    "Python",
                    "C++",
                    "JavaScript"
                  ]
                }
                """;

        PollResponse pollResponseProg = createPoll(requestBodyProg);

        /* Verify poll response */
        assertNotNull(pollResponseProg.pollId(), "Poll ID should not be null"); // Verify ID
        assertEquals("Favorite Programming Language", pollResponseProg.title()); // Verify title
        assertEquals(4, pollResponseProg.answersList().size(), "Expected 4 answers in the response"); // Verify 4 answers were created


        String requestBodyBurger = """
                {
                  "title": "Favorite burger in Tel Aviv",
                  "description": "Vote for your favorite burger in Tel Aviv",
                  "numAnswersAllowed": 1,
                  "creatorId": 123,
                  "groupId": 456,
                  "deadline": "2024-07-22T10:00:00.000Z",
                  "answers": [
                    "Bentz Brothers",
                    "Gourmet 26",
                    "Vitrina",
                    "Port 19",
                    "Marlen"
                  ]
                }
                """;

        PollResponse pollResponseBurger = createPoll(requestBodyBurger);

        /* Verify poll response */
        assertNotNull(pollResponseBurger.pollId(), "Poll ID should not be null"); // Verify ID
        assertEquals("Favorite burger in Tel Aviv", pollResponseBurger.title()); // Verify title
        assertEquals(5, pollResponseBurger.answersList().size(), "Expected 5 answers in the response"); // Verify 4 answers were created

    }

    PollResponse createPoll(String requestBody){
        // Check that response is in fact 201
        Response response = RestAssured.given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/api/poll")
                .then()
                .statusCode(201)
                .extract().response();

        return response.as(PollResponse.class);
    }

    @Test
    @Order(2)
    void shouldGetAllPolls() {
        // Check that response is in fact 200
        Response response = RestAssured.given()
                .contentType("application/json")
                .when()
                .get("/api/poll")
                .then()
                .statusCode(200)
                .extract().response();

        List<PollResponse> pollResponses = response.as(new TypeRef<List<PollResponse>>() {});
        /* Verify poll response */
        assertEquals(2, pollResponses.size(), "Expected 2 Polls in the response");
        pollId = pollResponses.get(0).pollId();
    }

    @Test
    @Order(3)
    void shouldGetPollById() {
        // Check that response is in fact 200
        Response response = RestAssured.given()
                .queryParam("pollId", pollId)
                .contentType("application/json")
                .when()
                .get("/api/poll/id")
                .then()
                .statusCode(200)
                .extract().response();

        PollResponse pollResponse = response.as(PollResponse.class);
        /* Verify poll response */
        assertNotNull(pollResponse.pollId(), "Poll ID should not be null");
    }

    @Test
    @Order(4)
    void shouldDeletePoll() {
        // Check that response is in fact 200
        Response responseDelete = RestAssured.given()
                .queryParam("pollId", pollId)
                .contentType("application/json")
                .when()
                .delete("/api/poll")
                .then()
                .statusCode(200)
                .extract().response();

        int pollIdResponse = responseDelete.as(Integer.class);

        // Check that response is in fact 200
        Response responseAll = RestAssured.given()
                .contentType("application/json")
                .when()
                .get("/api/poll")
                .then()
                .statusCode(200)
                .extract().response();

        List<PollResponse> pollResponses = responseAll.as(new TypeRef<List<PollResponse>>() {});
        /* Verify poll response */
        assertEquals(1, pollIdResponse, "Poll ID should be 1");
        assertEquals(1, pollResponses.size(), "Expected 1 Polls in the response");
    }
}

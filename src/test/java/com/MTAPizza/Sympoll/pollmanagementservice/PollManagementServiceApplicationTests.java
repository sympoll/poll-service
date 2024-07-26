package com.MTAPizza.Sympoll.pollmanagementservice;

import com.MTAPizza.Sympoll.pollmanagementservice.dto.error.IllegalPollArgumentError;
import com.MTAPizza.Sympoll.pollmanagementservice.dto.poll.PollCreateRequest;
import com.MTAPizza.Sympoll.pollmanagementservice.dto.poll.PollResponse;
import com.MTAPizza.Sympoll.pollmanagementservice.service.poll.PollService;
import com.MTAPizza.Sympoll.pollmanagementservice.validator.exception.PollExceptionHandler;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Import(PollExceptionHandler.class)
class PollManagementServiceApplicationTests {

    private static UUID pollId;

    private static DateTimeFormatter formatter;

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
        formatter = DateTimeFormatter.ISO_DATE_TIME;
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
                  "deadline": "2024-12-22T10:00:00.000Z",
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
                  "deadline": "2024-12-22T10:00:00.000Z",
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

        UUID pollIdResponse = responseDelete.as(UUID.class);

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
        assertNotNull(pollIdResponse, "Poll ID should not be null");
        assertEquals(1, pollResponses.size(), "Expected 1 Polls in the response");
    }

    @Test
    @Order(5)
    void shouldNotCreatePoll() {
        PollCreateRequest request = new PollCreateRequest(
                "Favorite Programming Language",
                "Vote for your favorite programming language",
                1,
                123,
                456,
                LocalDateTime.now().format(formatter),
                "2023-01-01T10:00:00.000Z", // Invalid deadline
                List.of("Java", "Python", "C++", "JavaScript")
        );

        // Perform the POST request with the invalid request body
        Response response = RestAssured.given()
                .contentType("application/json")
                .body(request)
                .when()
                .post("/api/poll")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract().response();

        // Verify the response body
        IllegalPollArgumentError errorResponse = response.as(IllegalPollArgumentError.class);
        assertNotNull(errorResponse, "Error response should not be null");
        assertEquals("A deadline cannot be earlier than the time a poll was created", errorResponse.message());
    }
}

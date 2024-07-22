package com.MTAPizza.Sympoll.pollmanagementservice;

import com.MTAPizza.Sympoll.pollmanagementservice.dto.poll.PollResponse;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PollManagementServiceApplicationTests {

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
    void shouldCreatePoll() {
        String requestBody = """
                {
                  "title": "Favorite Programming Language",
                  "description": "Vote for your favorite programming language",
                  "numAnswersAllowed": 1,
                  "creatorId": 123,
                  "groupId": 456,
                  "timeCreated": "2024-07-22T10:00:00.000Z",
                  "timeEnds": "2024-07-22T10:00:00.000Z",
                  "answers": [
                    "Java",
                    "Python",
                    "C++",
                    "JavaScript"
                  ]
                }
                """;
        // Check that response is in fact 201
        Response response = RestAssured.given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("api/poll")
                .then()
                .statusCode(201)
                .extract().response();

        PollResponse pollResponse = response.as(PollResponse.class);

        /* Verify poll response */
        assertNotNull(pollResponse.pollId(), "Poll ID should not be null"); // Verify ID
        assertEquals("Favorite Programming Language", pollResponse.title()); // Verify title
        assertEquals(4, pollResponse.answersList().size(), "Expected 4 answers in the response"); // Verify 4 answers were created
    }
}

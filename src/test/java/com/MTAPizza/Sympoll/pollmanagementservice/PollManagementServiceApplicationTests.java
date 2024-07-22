package com.MTAPizza.Sympoll.pollmanagementservice;

import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PollManagementServiceApplicationTests {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("postgres:16.2").withInitScript("/docker/init.sql");

    @LocalServerPort
    private Integer port;

    @BeforeEach
    void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    static {
        postgreSQLContainer.start();

    }

    @Test
    void shouldCreatePoll() {
        String requestBody = """
                {
                  "title": "Favorite Programming Language",
                  "description": "Vote for your favorite programming language",
                  "numAnswersAllowed": 1,
                  "creatorId": 123,
                  "groupId": 456,
                  "timeCreated": "2024-07-22T10:00:00",
                  "timeEnds": "2024-07-29T10:00:00",
                  "answers": [
                    "Java",
                    "Python",
                    "C++",
                    "JavaScript"
                  ]
                }
                """;

        RestAssured.given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("api/poll")
                .then()
                .statusCode(201)
                .body("id", Matchers.notNullValue());
    }
}

package com.MTAPizza.Sympoll.pollmanagementservice;

import com.MTAPizza.Sympoll.pollmanagementservice.dto.error.IllegalPollArgumentError;
import com.MTAPizza.Sympoll.pollmanagementservice.dto.poll.PollCreateRequest;
import com.MTAPizza.Sympoll.pollmanagementservice.dto.poll.PollResponse;
import com.MTAPizza.Sympoll.pollmanagementservice.validator.exception.PollExceptionHandler;
import com.google.gson.Gson;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
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

    private static final DateTimeFormatter formatter;

    private static final Gson gson;

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

    static {
        postgreSQLContainer.start(); //  Run mock test container.
        formatter = DateTimeFormatter.ISO_DATE_TIME;
        gson = new Gson();
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

        PollResponse pollResponseProg = tryToCreatePollAndAssertStatusCode(requestBodyProg, HttpStatus.CREATED).as(PollResponse.class);

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
                    "Benz Brothers",
                    "Gourmet 26",
                    "Vitrina",
                    "Port 19",
                    "Marlen"
                  ]
                }
                """;

        PollResponse pollResponseBurger = tryToCreatePollAndAssertStatusCode(requestBodyBurger, HttpStatus.CREATED).as(PollResponse.class);

        /* Verify poll response */
        assertNotNull(pollResponseBurger.pollId(), "Poll ID should not be null"); // Verify ID
        assertEquals("Favorite burger in Tel Aviv", pollResponseBurger.title()); // Verify title
        assertEquals(5, pollResponseBurger.answersList().size(), "Expected 5 answers in the response"); // Verify 4 answers were created

    }


    /**
     * Send a request to the poll service to create a poll with the given properties in the request body.
     * The expectedStatus is the status that the request is expected to return based on the body provided.
     * Returns the service's response.
     */
    Response tryToCreatePollAndAssertStatusCode(String requestBody, HttpStatus expectedStatus){
        // Check that response is in fact 201
        return RestAssured.given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/api/poll")
                .then()
                .statusCode(expectedStatus.value())
                .extract().response();
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

        List<PollResponse> pollResponses = response.as(new TypeRef<>() {});
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

        List<PollResponse> pollResponses = responseAll.as(new TypeRef<>() {});
        /* Verify poll response */
        assertNotNull(pollIdResponse, "Poll ID should not be null");
        assertEquals(1, pollResponses.size(), "Expected 1 Polls in the response");
    }


    /**
     * This should try to create polls and each of these functions should not succeed.
     */
    @Test
    @Order(5)
    void shouldNotCreatePoll() {
        tryToCreatePollUsingInvalidRequestBody();
        tryToCreatePollWithInvalidDate();
        tryToCreatePollWithInvalidAnswersAllowed();
    }


    /**
     * Test if to see that the system can detect if the user gave an earlier deadline than the poll's creation time.
     */
    void tryToCreatePollWithInvalidDate(){
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
        Response response = tryToCreatePollAndAssertStatusCode(gson.toJson(request), HttpStatus.BAD_REQUEST);

        // Verify the response body
        IllegalPollArgumentError errorResponse = response.as(IllegalPollArgumentError.class);
        assertNotNull(errorResponse, "Error response should not be null");
        assertEquals("A deadline cannot be earlier than the time a poll was created", errorResponse.message());
    }


    /**
     * Test to see if the system can detect that the user gave the option to select more answers than the actual number of answers provided.
     */
    void tryToCreatePollWithInvalidAnswersAllowed(){
        PollCreateRequest request = new PollCreateRequest(
                "Favorite Programming Language",
                "Vote for your favorite programming language",
                5,
                123,
                456,
                LocalDateTime.now().format(formatter),
                "2023-01-01T10:00:00.000Z", // Invalid deadline
                List.of("Java", "Python", "C++", "JavaScript")
        );

        // Perform the POST request with the invalid request body
        Response response = tryToCreatePollAndAssertStatusCode(gson.toJson(request), HttpStatus.BAD_REQUEST);

        // Verify the response body
        IllegalPollArgumentError errorResponse = response.as(IllegalPollArgumentError.class);
        assertNotNull(errorResponse, "Error response should not be null");
        assertEquals("Number of allowed answers is greater than number of available answers", errorResponse.message());
    }


    /**
     * Test if the system can handle a request to create a poll with a query parameter that doesn't exist ("Field").
     */
    void tryToCreatePollUsingInvalidRequestBody(){
        String requestBody = """
                {
                  "title": "Favorite burger in Tel Aviv",
                  "description": "Vote for your favorite burger in Tel Aviv",
                  "numAnswersAllowed": 1,
                  "creatorId": 123,
                  "groupId": 456,
                  "deadline": "2024-12-22T10:00:00.000Z",
                  "Field": "value"
                  "answers": [
                    "Benz Brothers",
                    "Gourmet 26",
                    "Vitrina",
                    "Port 19",
                    "Marlen"
                  ]
                }
                """;

        // Perform the POST request with the invalid request body
        Response response = tryToCreatePollAndAssertStatusCode(requestBody, HttpStatus.BAD_REQUEST);

        // Verify the response code
        IllegalPollArgumentError errorResponse = response.as(IllegalPollArgumentError.class);
        assertNotNull(errorResponse, "Error response should not be null");
    }


}

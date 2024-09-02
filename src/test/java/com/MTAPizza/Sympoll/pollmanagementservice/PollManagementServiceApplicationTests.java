package com.MTAPizza.Sympoll.pollmanagementservice;

import com.MTAPizza.Sympoll.pollmanagementservice.dto.error.IllegalArgumentResponse;
import com.MTAPizza.Sympoll.pollmanagementservice.dto.group.UserGroupsResponse;
import com.MTAPizza.Sympoll.pollmanagementservice.dto.poll.PollCreateRequest;
import com.MTAPizza.Sympoll.pollmanagementservice.dto.poll.PollResponse;
import com.MTAPizza.Sympoll.pollmanagementservice.dto.poll.delete.PollDeleteResponse;
import com.MTAPizza.Sympoll.pollmanagementservice.dto.vote.VoteResponse;
import com.MTAPizza.Sympoll.pollmanagementservice.dto.vote.count.VoteCountResponse;
import com.MTAPizza.Sympoll.pollmanagementservice.stub.GroupClientStub;
import com.MTAPizza.Sympoll.pollmanagementservice.stub.UserClientStub;
import com.MTAPizza.Sympoll.pollmanagementservice.exception.PollExceptionHandler;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.google.gson.Gson;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWireMock(port = 8081) // WireMock runs on port 8081
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Import(PollExceptionHandler.class)
class PollManagementServiceApplicationTests {
    private static final Logger log = LoggerFactory.getLogger(PollManagementServiceApplicationTests.class);
    private static UUID pollId; // Used in delete poll and get poll by id
    private static final int votingItemIdForVoteTest = 1; // vote on the first item of the first poll
    private static final UUID userId = UUID.fromString("b1f8e925-2129-473d-bc09-b3a2a331f839");
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
        WireMock.reset();
        UserClientStub.initStubs(userId);
        GroupClientStub.initStubs(userId);
    }

    static {
        postgreSQLContainer.start(); //  Run mock test container.
        gson = new Gson();
    }

    /**
     * Send request to create the specified poll.
     */
    @Test
    @Order(1)
    void shouldCreatePoll() {
        String requestBodyProg = String.format("""
                {
                  "title": "Favorite Programming Language",
                  "description": "Vote for your favorite programming language",
                  "nofAnswersAllowed": 1,
                  "creatorId": "%s",
                  "groupId": "123",
                  "deadline": "2024-12-22T10:00:00.000Z",
                  "votingItems": [
                    "Java",
                    "Python",
                    "C++",
                    "JavaScript"
                  ]
                }
                """, userId);

        PollResponse pollResponseProg = tryToCreatePollAndAssertStatusCode(requestBodyProg, HttpStatus.CREATED).as(PollResponse.class);

        /* Verify poll response */
        assertNotNull(pollResponseProg.pollId(), "Poll ID should not be null"); // Verify ID
        assertEquals("Favorite Programming Language", pollResponseProg.title()); // Verify title
        assertEquals(4, pollResponseProg.votingItems().size(), "Expected 4 answers in the response"); // Verify 4 answers were created

        String requestBodyBurger = String.format("""
                {
                  "title": "Favorite burger in Tel Aviv",
                  "description": "Vote for your favorite burger in Tel Aviv",
                  "nofAnswersAllowed": 1,
                  "creatorId": "%s",
                  "groupId": "123",
                  "deadline": "2024-12-22T10:00:00.000Z",
                  "votingItems": [
                    "Benz Brothers",
                    "Gourmet 26",
                    "Vitrina",
                    "Port 19",
                    "Marlen"
                  ]
                }
                """, userId);

        PollResponse pollResponseBurger = tryToCreatePollAndAssertStatusCode(requestBodyBurger, HttpStatus.CREATED).as(PollResponse.class);

        /* Verify poll response */
        assertNotNull(pollResponseBurger.pollId(), "Poll ID should not be null"); // Verify ID
        assertEquals("Favorite burger in Tel Aviv", pollResponseBurger.title()); // Verify title
        assertEquals(5, pollResponseBurger.votingItems().size(), "Expected 5 answers in the response"); // Verify 4 answers were created

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
                .get("/api/poll/all")
                .then()
                .statusCode(200)
                .extract().response();

        List<PollResponse> pollResponses = response.as(new TypeRef<>() {});
        /* Verify poll response */
        assertEquals(5, pollResponses.size(), "Expected 5 Polls in the response");
        pollId = pollResponses.get(4).pollId(); // Assigned to be the last poll created
    }


    @Test
    @Order(3)
    void shouldGetPollById() {
        // Check that response is in fact 200
        Response response = RestAssured.given()
                .queryParam("pollId", pollId)
                .contentType("application/json")
                .when()
                .get("/api/poll/by-poll-id")
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
        String requestBody = String.format("""
                {
                  "userId": "%s",
                  "pollId": "%s",
                  "groupId": "123"
                }
                """, userId, pollId);

        // Check that response is in fact 200
        Response responseDelete = RestAssured.given()
                .body(requestBody)
                .contentType("application/json")
                .when()
                .delete("/api/poll/by-poll-id")
                .then()
                .statusCode(200)
                .extract().response();

        PollDeleteResponse pollDeleteResponse = responseDelete.as(PollDeleteResponse.class);

        // Check that response is in fact 200
        Response responseAll = RestAssured.given()
                .contentType("application/json")
                .when()
                .get("/api/poll/all")
                .then()
                .statusCode(200)
                .extract().response();

        List<PollResponse> pollResponses = responseAll.as(new TypeRef<>() {});
        /* Verify poll response */
        assertNotNull(pollDeleteResponse.pollId(), "Poll ID should not be null");
        assertEquals(4, pollResponses.size(), "Expected 4 Polls in the response");
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
                userId,
                "123",
                "2023-01-01T10:00:00.000Z", // Invalid deadline
                List.of("Java", "Python", "C++", "JavaScript")
        );

        // Perform the POST request with the invalid request body
        Response response = tryToCreatePollAndAssertStatusCode(gson.toJson(request), HttpStatus.BAD_REQUEST);

        // Verify the response body
        IllegalArgumentResponse errorResponse = response.as(IllegalArgumentResponse.class);
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
                userId,
                "123",
                "2023-01-01T10:00:00.000Z", // Invalid deadline
                List.of("Java", "Python", "C++", "JavaScript")
        );

        // Perform the POST request with the invalid request body
        Response response = tryToCreatePollAndAssertStatusCode(gson.toJson(request), HttpStatus.BAD_REQUEST);

        // Verify the response body
        IllegalArgumentResponse errorResponse = response.as(IllegalArgumentResponse.class);
        assertNotNull(errorResponse, "Error response should not be null");
        assertEquals("Number of allowed answers is greater than number of available answers", errorResponse.message());
    }


    /**
     * Test if the system can handle a request to create a poll with a query parameter that doesn't exist ("Field").
     */
    void tryToCreatePollUsingInvalidRequestBody(){
        String requestBody = String.format("""
                {
                  "title": "Favorite burger in Tel Aviv",
                  "description": "Vote for your favorite burger in Tel Aviv",
                  "nofAnswersAllowed": 1,
                  "creatorId": "%s",
                  "groupId": "123",
                  "deadline": "2024-12-22T10:00:00.000Z",
                  "invalidField": "value"
                  "votingItems": [
                    "Benz Brothers",
                    "Gourmet 26",
                    "Vitrina",
                    "Port 19",
                    "Marlen"
                  ]
                }
                """, userId);

        // Perform the POST request with the invalid request body
        Response response = tryToCreatePollAndAssertStatusCode(requestBody, HttpStatus.BAD_REQUEST);

        // Verify the response code
        IllegalArgumentResponse errorResponse = response.as(IllegalArgumentResponse.class);
        assertNotNull(errorResponse, "Error response should not be null");
    }

    /**
     * Send request to create vote for 'Benz Brothers' in the second poll.
     */
    @Test
    @Order(6)
    void shouldAddVote(){
        String requestBody = String.format("""
                {
                  "votingItemId": %d,
                  "action": "add"
                }
                """, votingItemIdForVoteTest);

        // Check that response is in fact 200
        Response response = RestAssured.given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .put("/api/poll/vote")
                .then()
                .statusCode(200)
                .extract().response();

        VoteResponse voteResponse = response.as(VoteResponse.class);

        /* Verify vote response */
        assertEquals(1, voteResponse.voteCount(), "Expected 1 vote count");
    }

    /**
     * Send request to verify the vote count of 'Benz Brothers' in the second poll.
     */
    @Test
    @Order(7)
    void shouldGetVoteCount() {
        String requestBody = String.format("""
                {
                  "votingItemId": %d
                }
                """, votingItemIdForVoteTest);

        // Check that response is in fact 200
        Response response = RestAssured.given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .get("/api/poll/vote")
                .then()
                .statusCode(200)
                .extract().response();

        VoteCountResponse voteCountResponse = response.as(VoteCountResponse.class);

        /* Verify vote count */
        assertEquals(1, voteCountResponse.voteCount(), "Expected 1 vote count");
    }

    /**
     * Send request to delete the vote for 'Benz Brothers' in the second poll.
     */
    @Test
    @Order(8)
    void shouldDeleteVote() {
        // For this test 'voteId' argument is irrelevant. So, the test sends 'pollIdForVote' as a placeholder for this UUID.
        String requestBodyDelete = String.format("""
                {
                  "votingItemId": %d,
                  "action": "remove"
                }
                """, votingItemIdForVoteTest);

        // Check that response is in fact 200
        Response response = RestAssured.given()
                .contentType("application/json")
                .body(requestBodyDelete)
                .when()
                .put("/api/poll/vote")
                .then()
                .statusCode(200)
                .extract().response();

        VoteResponse voteResponse = response.as(VoteResponse.class);

        /* Verify vote count */
        assertEquals(0, voteResponse.voteCount(), "Expected 0 vote count");
    }

    @Test
    @Order(9)
    void shouldGetAllUserPolls() {
        // Check that response is in fact 200
        Response response = RestAssured.given()
                .contentType("application/json")
                .queryParam("userId", userId)
                .when()
                .get("/api/poll/all-user-polls")
                .then()
                .statusCode(200)
                .extract().response();

        List<PollResponse> pollsResponse = response.as(new TypeRef<>() {});

        /* Verify vote count */
        assertEquals(4, pollsResponse.size(), "Expected 4 polls");
    }
}

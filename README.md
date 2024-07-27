# Poll Management Service

## Overview

The Poll Management Service is a RESTful API for creating, retrieving, and deleting polls. It operates on port `8081` by default.

## Base URL

http://localhost:8081/api/poll

## Endpoints

### Create a Poll

- **URL:** `/`
- **Method:** `POST`
- **Description:** Creates a new poll.
- **Request Body:**
    ```json
    {
        "title": "string",
        "description": "string",
        "numAnswersAllowed": 0,
        "creatorId": "string",
        "groupId": "string",
        "deadline": "2024-07-27T00:00:00Z",
        "answers": [
            "string"
        ]
    }
    ```
- **Response:**
    ```json
    {
        "pollId": "uuid",
        "title": "string",
        "description": "string",
        "numAnswersAllowed": 0,
        "creatorId": "string",
        "groupId": "string",
        "timeCreated": "2024-07-27T00:00:00Z",
        "timeUpdated": "2024-07-27T00:00:00Z",
        "timeEnds": "2024-07-27T00:00:00Z",
        "answers": [
            {
                "answerId": "uuid",
                "answerContent": "string",
                "answerOrdinal": 0,
                "numberOfVotes": 0
            }
        ]
    }
    ```
- **Responses:**
    - `201 Created` – Poll created successfully.
    - `400 Bad Request` – Invalid input data.

### Get All Polls

- **URL:** `/`
- **Method:** `GET`
- **Description:** Retrieves a list of all polls.
- **Response:**
    ```json
    [
        {
            "pollId": "uuid",
            "title": "string",
            "description": "string",
            "numAnswersAllowed": 0,
            "creatorId": "string",
            "groupId": "string",
            "timeCreated": "2024-07-27T00:00:00Z",
            "timeUpdated": "2024-07-27T00:00:00Z",
            "timeEnds": "2024-07-27T00:00:00Z",
            "answers": [
                {
                    "answerId": "uuid",
                    "answerContent": "string",
                    "answerOrdinal": 0,
                    "numberOfVotes": 0
                }
            ]
        }
    ]
    ```
- **Responses:**
    - `200 OK` – List of polls successfully retrieved.

### Get Poll by ID

- **URL:** `/id`
- **Method:** `GET`
- **Description:** Retrieves a specific poll by its ID.
- **Query Parameters:**
    - `pollId` (UUID) – The ID of the poll to retrieve.
- **Response:**
    ```json
    {
        "pollId": "uuid",
        "title": "string",
        "description": "string",
        "numAnswersAllowed": 0,
        "creatorId": "string",
        "groupId": "string",
        "timeCreated": "2024-07-27T00:00:00Z",
        "timeUpdated": "2024-07-27T00:00:00Z",
        "timeEnds": "2024-07-27T00:00:00Z",
        "answers": [
            {
                "answerId": "uuid",
                "answerContent": "string",
                "answerOrdinal": 0,
                "numberOfVotes": 0
            }
        ]
    }
    ```
- **Responses:**
    - `200 OK` – Poll successfully retrieved.
    - `404 Not Found` – Poll with the specified ID not found.

### Delete a Poll

- **URL:** `/`
- **Method:** `DELETE`
- **Description:** Deletes a specific poll by its ID.
- **Query Parameters:**
    - `pollId` (UUID) – The ID of the poll to delete.
- **Response:**
    ```json
    {
        "pollId": "uuid"
    }
    ```
- **Responses:**
    - `200 OK` – Poll successfully deleted.
    - `404 Not Found` – Poll with the specified ID not found.

### Health Check

- **URL:** `/health`
- **Method:** `GET`
- **Description:** Checks the health of the service.
- **Response:**
    ```text
    OK
    ```
- **Responses:**
    - `200 OK` – Service is healthy.

## Error Codes

- `400 Bad Request` – The request could not be understood or was missing required parameters.
- `404 Not Found` – The specified resource could not be found.
- `500 Internal Server Error` – An error occurred on the server.

## Notes

- Make sure to include the `Content-Type: application/json` header in your requests.
- Use valid UUIDs and ISO-8601 timestamps in your requests.
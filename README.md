# Poll Management Service

## Overview

The Poll Management Service is a RESTful API for creating, retrieving, and deleting polls.

#### Service port: 8082
#### DB port: 5432

<br />   
   
## Base URL (for local testing)

http://localhost

<br />   

## Endpoints

### Create a Poll

- **Endpoint:** `/api/poll`
- **Method:** `POST`
- **Description:** Creates a new poll.
- **Request Body:**   


    ```json
    {
        "title": "string",
        "description": "string",
        "nofAnswersAllowed": 0
        "creatorId": "string",
        "groupId": "string",
        "deadline": "2024-07-27T00:00:00Z",
        "votingItems": [
            "string",
            "string",
            "string"
        ]
    }
    ```
    - constraints:   
            `nofAnswersAllowed` - a number between 0 and the number of voting options.   
            `deadline` - a time in the future.
   
- **Response:**   
  
    ```json
    {
        "pollId": "uuid",
        "title": "string",
        "description": "string",
        "nofAnswersAllowed": 0,
        "creatorId": "string",
        "groupId": "string",
        "timeCreated": "2024-07-27T00:00:00Z",
        "timeUpdated": "2024-07-27T00:00:00Z",
        "deadline": "2024-07-27T00:00:00Z",
        "votingItems": [
            {
                "votingItemId": "uuid",
                "votingItemOrdinal": 0,
                "description": "string",
                "voteCount": 0
            }
        ]
    }
    ```
- **Response HTTP Status:**
    - `200 OK` - Successfully completed request.
    - `201 Created` – Poll created successfully.
    - `400 Bad Request` – Invalid input data.

<br />   

### Get All Polls

- **Endpoint:** `/api/poll/all`
- **Method:** `GET`
- **Description:** Retrieves a list of all polls.
- **Response HTTP Status:**
    ```json
    [
        {
            "pollId": "uuid",
            "title": "string",
            "description": "string",
            "nofAnswersAllowed": 0,
            "creatorId": "string",
            "groupId": "string",
            "timeCreated": "2024-07-27T00:00:00Z",
            "timeUpdated": "2024-07-27T00:00:00Z",
            "deadline": "2024-07-27T00:00:00Z",
            "votingItems": [
                {
                    "votingItemId": "uuid",
                    "votingItemOrdinal": 0,
                    "description": "string",
                    "voteCount": 0
                }
            ]
        }
    ]
    ```
- **Response HTTP Status:**
    - `200 OK` – List of polls successfully retrieved.

<br />   

### Get Poll by ID

- **Endpoint:** `/api/poll/by-id`
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
        "nofAnswersAllowed": 0,
        "creatorId": "string",
        "groupId": "string",
        "timeCreated": "2024-07-27T00:00:00Z",
        "timeUpdated": "2024-07-27T00:00:00Z",
        "deadline": "2024-07-27T00:00:00Z",
        "votingItems": [
            {
                "votingItemId": "uuid",
                "votingItemOrdinal": 0,
                "description": "string",
                "voteCount": 0
            }
        ]
    }
    ```
- **Response HTTP Status:**
    - `200 OK` – Poll successfully retrieved.
    - `404 Not Found` – Poll with the specified ID not found.


<br />   

### Delete a Poll

- **Endpoint:** `/api/poll`
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
- **Response HTTP Status:**
    - `200 OK` – Poll successfully deleted.
    - `404 Not Found` – Poll with the specified ID not found.

<br />   
   
### Health Check

- **URL:** `api/poll/health`
- **Method:** `GET`
- **Description:** Checks the health of the service.
- **Response:**
    ```json
    {
        "status": "Running",
        "message": "Poll Management Service is up and running."
    }
    ```
- **Response HTTP Status:**
    - `200 OK` – Service is healthy.

<br />   
   
## Error Codes
- `400 Bad Request` – The request could not be understood or was missing required parameters.
- `404 Not Found` – The specified resource could not be found.
- `500 Internal Server Error` – An error occurred on the server.

<br />   

## Notes

- Make sure to include the `Content-Type: application/json` header in your requests.
- Use valid UUIDs and ISO-8601 timestamps in your requests.

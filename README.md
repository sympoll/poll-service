# Poll Service

## 1) About

The Poll Service is a RESTful API for creating, retrieving, and deleting polls. It manages poll data and interacts with the Vote Service to keep vote counts up to date.

## 2) Architecture

### 2.1) Ports

- **Service port:**  8082

- **Database port:**  5432

### 2.2) Poll Management Service Schema

The database schema includes the following tables:


```sql
CREATE TABLE polls
(
    poll_id             UUID PRIMARY KEY,
    title               VARCHAR(255),
    description         TEXT,
    nof_answers_allowed INT,
    creator_id          UUID,
    group_id            VARCHAR(255),
    time_created        TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    time_updated        TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deadline            TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE voting_item_options
(
    voting_item_id    SERIAL PRIMARY KEY,
    poll_id           UUID REFERENCES polls (poll_id),
    ordinal           INT,
    description       TEXT,
    vote_count        INT
);
```

### 2.3) Create a Poll

- **Method:**  POST

- **Endpoint:**  `/api/poll`

- **Description:**  Creates a new poll.

- **Request Body:**

```json
{
    "title": "string",
    "description": "string",
    "nofAnswersAllowed": 0,
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

- **Constraints:**
  - `nofAnswersAllowed` - a number between 0 and the number of voting options.

  - `deadline` - a time in the future.

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
  - `200 OK` – Successfully completed request.

  - `201 Created` – Poll created successfully.

  - `400 Bad Request` – Invalid input data.

### 2.4) Get All Polls

- **Method:**  GET

- **Endpoint:**  `/api/poll/all`

- **Description:**  Retrieves a list of all polls.

- **Response:**

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

### 2.5) Get Poll by ID

- **Method:**  GET

- **Endpoint:**  `/api/poll/by-poll-id`

- **Description:**  Retrieves a specific poll by its ID.

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

### 2.6) Delete a Poll

- **Method:**  DELETE

- **Endpoint:**  `/api/poll/by-poll-id`

- **Description:**  Deletes a specific poll by its ID.

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

### 2.7) Health Check

- **Method:**  GET

- **Endpoint:**  `/api/poll/health`

- **Description:**  Checks the health of the service.

- **Response:**

```json
{
    "status": "Running",
    "message": "Poll Management Service is up and running."
}
```

- **Response HTTP Status:**
  - `200 OK` – Service is healthy.

### 2.8) Vote on Poll

- **Method:**  PUT

- **Endpoint:**  `/api/poll/vote`

- **Description:**  Updates the vote count for a specified voting item.

- **Request Body:**

```json
{
    "userId": "uuid",
    "votingItemId": "uuid",
    "action": "add" // or "remove"
}
```

- **Response:**

```json
{
    "votingItemDescription": "string",
    "voteCount": 0
}
```

- **Response HTTP Status:**
  - `200 OK` – Vote count successfully updated.

## 3) Error Codes

- `400 Bad Request` – The request could not be understood or was missing required parameters.

- `404 Not Found` – The specified resource could not be found.

- `500 Internal Server Error` – An error occurred on the server.

## 4) Notes

- Make sure to include the `Content-Type: application/json` header in your requests.

- Use valid UUIDs and ISO-8601 timestamps in your requests.

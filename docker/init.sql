-- Poll Management Service Schema
CREATE TABLE polls
(
    poll_id             SERIAL PRIMARY KEY,
    title               VARCHAR(255),
    description         TEXT,
    num_answers_allowed INT,
    creator_id          INT,
    group_id            INT,
    time_created        TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    time_updated        TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deadline           TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE answer_options
(
    answer_id    SERIAL PRIMARY KEY,
    poll_id      INT REFERENCES polls (poll_id),
    ordinal      INT,
    answer_text  TEXT,
    num_of_votes INT
);
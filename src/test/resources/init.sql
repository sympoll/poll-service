-- Poll Management Service Schema
CREATE TABLE polls
(
    poll_id             UUID PRIMARY KEY,
    title               VARCHAR(255),
    description         TEXT,
    nof_answers_allowed INT,
    creator_id          INT,
    group_id            INT,
    time_created        TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    time_updated        TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deadline           TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE voting_item_options
(
    voting_item_id    SERIAL PRIMARY KEY,
    poll_id      UUID REFERENCES polls (poll_id),
    ordinal      INT,
    description  TEXT,
    vote_count INT
);
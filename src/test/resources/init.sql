-- Poll Management Service Schema
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
    description       TEXT,
    vote_count        INT
);
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

-- Insert sample data into polls table
INSERT INTO polls (poll_id, title, description, nof_answers_allowed, creator_id, group_id, time_created, time_updated, deadline) VALUES
                                                                                                                                     ('71c7f8b5-48b3-4e3e-9f76-dc6bbf1e1234', 'Favorite Social Media Platform', 'Vote for your favorite social media platform.', 1, 'b1f8e925-2129-473d-bc09-b3a2a331f839', 'social', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '30 days'),
                                                                                                                                     ('c3d0f8d2-64d5-4e8c-b9e4-bd92c21e5678', 'Preferred Method of Communication', 'Choose your preferred method of communication.', 1, 'b1f8e925-2129-473d-bc09-b3a2a331f839', 'communication', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '40 days'),
                                                                                                                                     ('d1b2e8f1-29a4-4c2d-8f3d-abc5d4e39999', 'Best Movie Genre', 'Select your favorite movie genre.', 1, 'b1f8e925-2129-473d-bc09-b3a2a331f839', 'movies', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '50 days');


-- Insert sample data into voting_item_options table for 'Favorite Social Media Platform' poll
INSERT INTO voting_item_options (poll_id, description, vote_count) VALUES
                                                                       ('71c7f8b5-48b3-4e3e-9f76-dc6bbf1e1234', 'Facebook', 0),
                                                                       ('71c7f8b5-48b3-4e3e-9f76-dc6bbf1e1234', 'Instagram', 0),
                                                                       ('71c7f8b5-48b3-4e3e-9f76-dc6bbf1e1234', 'Twitter', 0),
                                                                       ('71c7f8b5-48b3-4e3e-9f76-dc6bbf1e1234', 'LinkedIn', 0);

-- Insert sample data into voting_item_options table for 'Preferred Method of Communication' poll
INSERT INTO voting_item_options (poll_id, description, vote_count) VALUES
                                                                       ('c3d0f8d2-64d5-4e8c-b9e4-bd92c21e5678', 'Email', 0),
                                                                       ('c3d0f8d2-64d5-4e8c-b9e4-bd92c21e5678', 'Phone Call', 0),
                                                                       ('c3d0f8d2-64d5-4e8c-b9e4-bd92c21e5678', 'Text Message', 0),
                                                                       ('c3d0f8d2-64d5-4e8c-b9e4-bd92c21e5678', 'Video Call', 0);

-- Insert sample data into voting_item_options table for 'Best Movie Genre' poll
INSERT INTO voting_item_options (poll_id, description, vote_count) VALUES
                                                                       ('d1b2e8f1-29a4-4c2d-8f3d-abc5d4e39999', 'Action', 0),
                                                                       ('d1b2e8f1-29a4-4c2d-8f3d-abc5d4e39999', 'Comedy', 0),
                                                                       ('d1b2e8f1-29a4-4c2d-8f3d-abc5d4e39999', 'Drama', 0),
                                                                       ('d1b2e8f1-29a4-4c2d-8f3d-abc5d4e39999', 'Science Fiction', 0);
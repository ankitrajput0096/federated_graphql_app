CREATE TABLE IF NOT EXISTS campus (
    id BIGINT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

INSERT INTO campus (id, name) VALUES
(1, 'Apollo Campus'),
(2, 'Artemis Campus'),
(3, 'Voyager Campus')
ON CONFLICT (name) DO NOTHING;
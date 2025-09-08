CREATE TABLE campus (
    id BIGINT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

INSERT INTO campus (id, name) VALUES
(100, 'Main Campus'),
(101, 'Satellite Campus');
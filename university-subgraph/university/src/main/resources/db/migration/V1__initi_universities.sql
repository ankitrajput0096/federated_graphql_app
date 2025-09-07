CREATE TABLE IF NOT EXISTS universities (
    id BIGINT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT
);

INSERT INTO universities (id, name, description) VALUES
(1, 'Saturn V', 'The Original Super Heavy-Lift Rocket!'),
(2, 'Lunar Module', NULL),
(3, 'Space Shuttle', NULL),
(4, 'Falcon 9', 'Reusable Medium-Lift Rocket'),
(5, 'Dragon', 'Reusable Medium-Lift Rocket'),
(6, 'Starship', 'Super Heavy-Lift Reusable Launch Vehicle')
ON CONFLICT (id) DO NOTHING;
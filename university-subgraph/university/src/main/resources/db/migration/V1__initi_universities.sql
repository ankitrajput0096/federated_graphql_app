CREATE TABLE IF NOT EXISTS universities (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    description TEXT,
    status VARCHAR(255) NOT NULL,
    created_at DATE NOT NULL,
    ranking INTEGER
);

INSERT INTO universities (name, description, status, created_at, ranking) VALUES
('Saturn V Campus', 'The Original Super Heavy-Lift Rocket!', 'ACTIVE', '2025-01-01', 10),
('Lunar Module', NULL, 'INACTIVE', '2025-01-02', 20),
('Space Shuttle Campus', NULL, 'ACTIVE', '2025-01-03', 30),
('Falcon 9', 'Reusable Medium-Lift Rocket', 'ACTIVE', '2025-01-04', 40),
('Dragon', 'Reusable Medium-Lift Rocket', 'ACTIVE', '2025-01-05', 50),
('Starship', 'Super Heavy-Lift Reusable Launch Vehicle', 'ACTIVE', '2025-01-06', 60)
ON CONFLICT (name) DO NOTHING;
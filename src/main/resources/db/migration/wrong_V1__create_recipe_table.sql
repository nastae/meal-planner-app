CREATE TABLE recipe (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    meal_type VARCHAR(50)
);
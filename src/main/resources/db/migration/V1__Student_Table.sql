CREATE TABLE student(
    id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(25) NOT NULL,
    last_name VARCHAR(25) NOT NULL,
    email VARCHAR(50) NOT NULL,
    age INTEGER NOT NULL
);
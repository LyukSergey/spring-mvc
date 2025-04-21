CREATE TABLE IF NOT EXISTS users_test
(
    id
    SERIAL
    PRIMARY
    KEY,
    name
    VARCHAR
(
    255
),
    email VARCHAR
(
    255
)
    );

INSERT INTO users_test (name, email)
VALUES ('Alice', 'alice@example.com'),
       ('Bob', 'bob@example.com'),
       ('Charlie', 'charlie@example.com'),
       ('David', 'david@example.com'),
       ('Eva', 'eva@example.com'),
       ('Frank', 'frank@example.com'),
       ('Grace', 'grace@example.com'),
       ('Hannah', 'hannah@example.com'),
       ('Ivan', 'ivan@example.com'),
       ('Julia', 'julia@example.com');

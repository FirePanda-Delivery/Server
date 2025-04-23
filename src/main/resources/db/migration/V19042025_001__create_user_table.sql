CREATE SEQUENCE user_id_sequence START WITH 1;

CREATE TABLE users
(
    id         BIGINT PRIMARY KEY DEFAULT  nextval('user_id_sequence'),
    user_name  VARCHAR(255) NOT NULL,
    password   VARCHAR(255),
    role       VARCHAR(255),
    first_name VARCHAR(255) NOT NULL,
    last_name  VARCHAR(255) NOT NULL,
    phone      VARCHAR(255) NOT NULL UNIQUE,
    email      VARCHAR(255) UNIQUE,
    is_deleted BOOLEAN
);

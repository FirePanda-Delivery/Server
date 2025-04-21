CREATE SEQUENCE courier_id_sequence START WITH 1;

CREATE TABLE courier
(
    id         BIGINT PRIMARY KEY DEFAULT nextval('courier_id_sequence'),
    first_name VARCHAR(255) NOT NULL,
    last_name  VARCHAR(255) NOT NULL,
    phone      VARCHAR(255) NOT NULL UNIQUE,
    email      VARCHAR(255) UNIQUE,
    rating     FLOAT,
    status     VARCHAR,
    city_id    BIGINT       NOT NULL,
    is_deleted BOOLEAN,
    CONSTRAINT FK_COURIER_ON_CITY FOREIGN KEY (city_id) REFERENCES city (id)
);

CREATE SEQUENCE city_id_sequence START WITH 1;

CREATE TABLE city
(
    id   BIGINT PRIMARY KEY DEFAULT nextval('city_id_sequence'),
    citi VARCHAR(255)
);
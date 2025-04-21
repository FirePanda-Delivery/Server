CREATE SEQUENCE restaurant_id_sequence START WITH 1;

CREATE TABLE restaurant
(
    id                  BIGINT PRIMARY KEY              DEFAULT nextval('restaurant_id_sequence'),
    name                VARCHAR(255)           NOT NULL UNIQUE,
    description         VARCHAR(1000),
    working_hours_start time WITHOUT TIME ZONE NOT NULL,
    working_hours_end   time WITHOUT TIME ZONE NOT NULL,
    min_price           DOUBLE PRECISION                default 0.0,
    rating              FLOAT,
    own_delivery        BOOLEAN,
    is_deleted          BOOLEAN,
    img                 VARCHAR(255),
    published           BOOLEAN                NOT NULL DEFAULT FALSE
);

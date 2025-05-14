CREATE SEQUENCE avg_delivery_time_id_sequence START WITH 1;

CREATE TABLE IF NOT EXISTS average_delivery_time
(
    id                BIGINT PRIMARY KEY DEFAULT nextval('avg_delivery_time_id_sequence'),
    restaurant_id     BIGINT UNIQUE NOT NULL references restaurant (id),
    avg_delivery_time SMALLINT,
    city_id           BIGINT        NOT NULL REFERENCES city (id)
);

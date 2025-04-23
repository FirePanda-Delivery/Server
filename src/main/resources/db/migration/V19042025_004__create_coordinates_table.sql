CREATE SEQUENCE coordinates_id_sequence START WITH 1;

CREATE TABLE coordinates
(
    id      BIGINT PRIMARY KEY DEFAULT nextval('coordinates_id_sequence'),
    x       DOUBLE PRECISION NOT NULL,
    y       DOUBLE PRECISION NOT NULL,
    index   INTEGER          NOT NULL,
    city_id BIGINT           NOT NULL,
    CONSTRAINT FK_COORDINATES_ON_CITYID FOREIGN KEY (city_id) REFERENCES city (id)
);
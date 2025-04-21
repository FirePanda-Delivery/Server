CREATE SEQUENCE category_id_sequence START WITH 1;

CREATE TABLE category
(
    id                    BIGINT PRIMARY KEY DEFAULT nextval('category_id_sequence'),
    restaurant_id         BIGINT                      NOT NULL,
    name               VARCHAR(255)                NOT NULL,
    is_deleted BOOLEAN,
    CONSTRAINT FK_ORDERS_ON_RESTAURANT FOREIGN KEY (restaurant_id) REFERENCES restaurant(id)
);


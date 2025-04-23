CREATE SEQUENCE order_id_sequence START WITH 1;

CREATE TABLE "order"
(
    id                    BIGINT PRIMARY KEY DEFAULT nextval('order_id_sequence'),
    restaurant_id         BIGINT                      NOT NULL,
    user_id               BIGINT                      NOT NULL,
    date                  TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    status                VARCHAR                     NOT NULL,
    courier_id            BIGINT,
    time_start            time WITHOUT TIME ZONE      NOT NULL,
    time_end              time WITHOUT TIME ZONE,
    address               VARCHAR(255)                NOT NULL,
    cities_id             BIGINT                      NOT NULL,
    restaurant_address_id BIGINT                      NOT NULL,
    CONSTRAINT FK_ORDERS_ON_CITIES FOREIGN KEY (cities_id) REFERENCES city (id),
    CONSTRAINT FK_ORDERS_ON_COURIER FOREIGN KEY (courier_id) REFERENCES courier (id),
    CONSTRAINT FK_ORDERS_ON_RESTAURANT FOREIGN KEY (restaurant_id) REFERENCES restaurant (id),
    CONSTRAINT FK_ORDERS_ON_RESTAURANT_ADDRESS FOREIGN KEY (restaurant_address_id) REFERENCES restaurant_address (id),
    CONSTRAINT FK_ORDERS_ON_USER FOREIGN KEY (user_id) REFERENCES "users" (id)
);
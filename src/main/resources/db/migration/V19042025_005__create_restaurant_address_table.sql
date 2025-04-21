CREATE SEQUENCE restaurant_address_id_sequence START WITH 1;

CREATE TABLE restaurant_address
(
    id            BIGINT PRIMARY KEY DEFAULT nextval('restaurant_address_id_sequence'),
    address       VARCHAR(255) NOT NULL,
    city_id       BIGINT       NOT NULL,
    restaurant_id BIGINT       NOT NULL,
    CONSTRAINT FK_RESTAURANTADDRESS_ON_CITY FOREIGN KEY (city_id) REFERENCES city (id),
    CONSTRAINT FK_RESTAURANTADDRESS_ON_RESTAURANT FOREIGN KEY (restaurant_id) REFERENCES restaurant (id)
);
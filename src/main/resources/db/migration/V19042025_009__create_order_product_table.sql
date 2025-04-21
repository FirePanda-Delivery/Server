CREATE SEQUENCE order_product_id_sequence START WITH 1;

CREATE TABLE order_product
(
    id         BIGINT PRIMARY KEY DEFAULT nextval('order_product_id_sequence'),
    count      INTEGER                                 NOT NULL,
    product_id BIGINT                                  NOT NULL,
    order_id   BIGINT                                  NOT NULL,
    CONSTRAINT FK_ORDER_PRODUCT_ON_ORDER FOREIGN KEY (order_id) REFERENCES "order" (id),
    CONSTRAINT FK_ORDER_PRODUCT_ON_PRODUCT FOREIGN KEY (product_id) REFERENCES product (id)
);

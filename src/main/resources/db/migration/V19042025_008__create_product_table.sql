CREATE SEQUENCE product_id_sequence START WITH 1;

CREATE TABLE product
(
    id              BIGINT PRIMARY KEY DEFAULT nextval('product_id_sequence'),
    name            VARCHAR(255)                            NOT NULL,
    category_id     BIGINT,
    description     VARCHAR(1000),
    price           DOUBLE PRECISION                        NOT NULL,
    weight          INTEGER                                 NOT NULL,
    popularity      FLOAT,
    img             VARCHAR(255),
    is_deleted      BOOLEAN,
    CONSTRAINT FK_PRODUCT_ON_CATEGORY FOREIGN KEY (category_id) REFERENCES category (id)
);
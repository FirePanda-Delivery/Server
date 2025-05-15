ALTER TABLE restaurant
    ALTER COLUMN min_price TYPE INT4 USING int4(min_price);

ALTER TABLE product
    ALTER COLUMN price TYPE INT4 USING int4(price);
ALTER TABLE category
ADD CONSTRAINT unique_name_index UNIQUE("name", restaurant_id);
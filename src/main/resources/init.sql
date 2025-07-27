INSERT INTO customer (id) VALUES ('cus_1');

INSERT INTO company (id, name, customer_id)
VALUES ('com_1', 'A 커머스', 'cus_1');
INSERT INTO company (id, name, customer_id)
VALUES ('com_2', 'B 커머스', 'cus_1');
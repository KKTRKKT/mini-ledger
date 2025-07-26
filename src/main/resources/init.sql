INSERT INTO customer (id) VALUES ('cus_1');

INSERT INTO company (id, name, customer_id)
VALUES ('com_1', '회사A', 'cus_1');
INSERT INTO company (id, name, customer_id)
VALUES ('com_2', '회사B', 'cus_1');
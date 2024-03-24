DROP TABLE employees IF EXISTS;
DROP SEQUENCE EMPLOYEES_SEQ;

CREATE SEQUENCE EMPLOYEES_SEQ;
CREATE TABLE employees  (
    id NUMBER,
    first_name VARCHAR(20),
    last_name VARCHAR(20),
    email_address VARCHAR(20),
);
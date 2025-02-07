CREATE TABLE departments
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE employees
(
    id            SERIAL PRIMARY KEY,
    department_id INTEGER     NOT NULL REFERENCES departments (id) ON DELETE CASCADE,
    name          VARCHAR(50) NOT NULL,
    salary        NUMERIC(15, 2) NULL,
    isManager     BOOLEAN     NOT NULL DEFAULT TRUE
);

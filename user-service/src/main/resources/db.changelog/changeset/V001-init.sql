CREATE TABLE IF NOT EXISTS users
(
    id SERIAL PRIMARY KEY,
    username varchar(50) UNIQUE,
    password varchar(255),
    email varchar(255) UNIQUE,
    first_name varchar(50),
    last_name varchar(50),
    birthday DATE,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS roles
(
    id SERIAL PRIMARY KEY,
    role varchar(10) UNIQUE
);

CREATE TABLE IF NOT EXISTS user_role(
    user_id INTEGER,
    role_id INTEGER,
    CONSTRAINT fk_users
        FOREIGN KEY (user_id)
        REFERENCES users(id),
    CONSTRAINT fk_roles
        FOREIGN KEY (role_id)
        REFERENCES roles(id)
);

INSERT INTO roles (role)
VALUES ('USER'),
       ('ADMIN');
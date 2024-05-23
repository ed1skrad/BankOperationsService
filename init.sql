CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       username VARCHAR(20) NOT NULL,
                       email VARCHAR(50) UNIQUE,
                       password VARCHAR(120) NOT NULL,
                       full_name VARCHAR(255) NOT NULL,
                       date_of_birth DATE NOT NULL,
                        phone_number VARCHAR(32)
);

CREATE TABLE roles (
                       id SERIAL PRIMARY KEY,
                       name VARCHAR(20) UNIQUE NOT NULL
);

CREATE TABLE user_roles (
                            user_id INT,
                            role_id INT,
                            PRIMARY KEY (user_id, role_id),
                            FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
                            FOREIGN KEY (role_id) REFERENCES roles (id)
);

INSERT INTO roles (name) VALUES ('ROLE_USER');
INSERT INTO roles (name) VALUES ('ROLE_ADMIN');

CREATE TABLE account (
                         id SERIAL PRIMARY KEY,
                         balance NUMERIC(19,2) NOT NULL,
                         user_id BIGINT UNIQUE,
                         initial_balance NUMERIC(19,2) NOT NULL,
                         FOREIGN KEY (user_id) REFERENCES users (id)
);


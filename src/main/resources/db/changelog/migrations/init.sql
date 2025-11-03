CREATE TABLE IF NOT EXISTS product_entity (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR NOT NULL UNIQUE,
    description VARCHAR,
    category VARCHAR NOT NULL,
    quantity INTEGER NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE,
    updated_at TIMESTAMP WITH TIME ZONE
);
--rollback DROP TABLE product_entity

CREATE TABLE IF NOT EXISTS users (
    username VARCHAR NOT NULL PRIMARY KEY,
    password VARCHAR NOT NULL,
    enabled BOOLEAN NOT NULL
);
--rollback DROP TABLE users

CREATE TABLE IF NOT EXISTS authorities (
    username VARCHAR NOT NULL REFERENCES users(username),
    authority VARCHAR NOT NULL
);
--rollback DROP TABLE authorities


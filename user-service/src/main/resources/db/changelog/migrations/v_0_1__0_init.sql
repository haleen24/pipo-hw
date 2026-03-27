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

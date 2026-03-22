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

CREATE TABLE IF NOT EXISTS supplier(
    id          BIGSERIAL                   PRIMARY KEY,
    name        VARCHAR                     NOT NULL,
    code        VARCHAR                     NOT NULL UNIQUE,
    created_at  TIMESTAMP WITH TIME ZONE    NOT NULL,
    updated_at  TIMESTAMP WITH TIME ZONE    NOT NULL DEFAULT now()
);

CREATE TABLE IF NOT EXISTS product(
    id              BIGSERIAL                   PRIMARY KEY,
    code            VARCHAR                     NOT NULL UNIQUE,
    name            VARCHAR                     NOT NULL,
    length_cm       INTEGER                     NOT NULL,
    width_cm        INTEGER                     NOT NULL,
    height_cm       INTEGER                     NOT NULL,
    created_at      TIMESTAMP WITH TIME ZONE    NOT NULL,
    updated_at      TIMESTAMP WITH TIME ZONE    NOT NULL DEFAULT now()
);

CREATE TABLE IF NOT EXISTS shipment(
    id                      BIGSERIAL                   PRIMARY KEY,
    supplier_id             BIGINT                      NOT NULL REFERENCES supplier(id),
    external_shipment_id    BIGINT,
    product_id              BIGINT                      NOT NULL REFERENCES product(id),
    created_at              TIMESTAMP WITH TIME ZONE    NOT NULL,
    updated_at              TIMESTAMP WITH TIME ZONE    NOT NULL DEFAULT now()
);

CREATE TABLE IF NOT EXISTS shipment_unit(
    id              BIGSERIAL                   PRIMARY KEY,
    shipment_id     BIGINT                      NOT NULL REFERENCES shipment(id),
    amount          BIGINT                      NOT NULL,
    length_cm       INTEGER                     NOT NULL,
    width_cm        INTEGER                     NOT NULL,
    height_cm       INTEGER                     NOT NULL,
    created_at      TIMESTAMP WITH TIME ZONE    NOT NULL,
    updated_at      TIMESTAMP WITH TIME ZONE    NOT NULL DEFAULT now()
);

CREATE TABLE IF NOT EXISTS location_type(
    id              BIGSERIAL                   PRIMARY KEY,
    code            VARCHAR                     NOT NULL UNIQUE,
    length_cm       INTEGER                     NOT NULL,
    width_cm        INTEGER                     NOT NULL,
    height_cm       INTEGER                     NOT NULL,
    created_at      TIMESTAMP WITH TIME ZONE    NOT NULL,
    updated_at      TIMESTAMP WITH TIME ZONE    NOT NULL DEFAULT now()
);

CREATE TABLE IF NOT EXISTS zone(
    id              BIGSERIAL                   PRIMARY KEY,
    code            VARCHAR                     NOT NULL UNIQUE,
    name            VARCHAR                     NOT NULL,
    created_at      TIMESTAMP WITH TIME ZONE    NOT NULL,
    updated_at      TIMESTAMP WITH TIME ZONE    NOT NULL DEFAULT now()
);

INSERT INTO zone (code, name, created_at, updated_at) VALUES
    ('INBOUND', 'Зона приемки', now(), now()),
    ('STORAGE', 'Зона хранения', now(), now());

CREATE TABLE IF NOT EXISTS location(
    id              BIGSERIAL                   PRIMARY KEY,
    code            VARCHAR                     NOT NULL UNIQUE,
    zone_id         BIGINT                      NOT NULL REFERENCES zone(id),
    created_at      TIMESTAMP WITH TIME ZONE    NOT NULL,
    updated_at      TIMESTAMP WITH TIME ZONE    NOT NULL DEFAULT now()
);

CREATE TABLE IF NOT EXISTS stock(
    id              BIGSERIAL                   PRIMARY KEY,
    location_id     BIGINT                      NOT NULL REFERENCES location(id),
    product_id      BIGINT                      NOT NULL REFERENCES product(id),
    amount          BIGINT                      NOT NULL,
    created_at      TIMESTAMP WITH TIME ZONE    NOT NULL,
    updated_at      TIMESTAMP WITH TIME ZONE    NOT NULL DEFAULT now(),
    UNIQUE(product_id, location_id)
);

CREATE TABLE IF NOT EXISTS withdrawal(
    id                      BIGSERIAL                   PRIMARY KEY,
    product_location_code   VARCHAR                     NOT NULL,
    product_code            VARCHAR                     NOT NULL,
    amount                  BIGINT                      NOT NULL,
    status                  VARCHAR                     NOT NULL,
    created_at              TIMESTAMP WITH TIME ZONE    NOT NULL,
    updated_at              TIMESTAMP WITH TIME ZONE    NOT NULL DEFAULT now()
);

CREATE TABLE IF NOT EXISTS receiver(
    id          BIGSERIAL                   PRIMARY KEY,
    name        VARCHAR                     NOT NULL,
    code        VARCHAR                     NOT NULL UNIQUE,
    created_at  TIMESTAMP WITH TIME ZONE    NOT NULL,
    updated_at  TIMESTAMP WITH TIME ZONE    NOT NULL DEFAULT now()
);

CREATE TABLE IF NOT EXISTS outbound_shipment(
    id                      BIGSERIAL                   PRIMARY KEY,
    code                    VARCHAR                     NOT NULL UNIQUE,
    external_shipment_id    BIGINT,
    receiver_id             BIGINT                      NOT NULL REFERENCES receiver(id),
    status                  VARCHAR                     NOT NULL,
    created_at              TIMESTAMP WITH TIME ZONE    NOT NULL,
    updated_at              TIMESTAMP WITH TIME ZONE    NOT NULL DEFAULT now()
);
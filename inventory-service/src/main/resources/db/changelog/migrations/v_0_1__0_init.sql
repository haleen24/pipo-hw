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
    length_cm       BIGINT                      NOT NULL,
    width_cm        BIGINT                      NOT NULL,
    height_cm       BIGINT                      NOT NULL,
    created_at      TIMESTAMP WITH TIME ZONE    NOT NULL,
    updated_at      TIMESTAMP WITH TIME ZONE    NOT NULL DEFAULT now()
);

CREATE TABLE IF NOT EXISTS shipment(
    id                      BIGSERIAL                   PRIMARY KEY,
    supplier_id             BIGINT                      NOT NULL REFERENCES supplier(id),
    external_shipment_id    VARCHAR,
    product_id              BIGINT                      NOT NULL REFERENCES product(id),
    status                  VARCHAR                     NOT NULL,
    shipment_unit_count     BIGINT                      NOT NULL,
    created_at              TIMESTAMP WITH TIME ZONE    NOT NULL,
    updated_at              TIMESTAMP WITH TIME ZONE    NOT NULL DEFAULT now()
);
CREATE INDEX shipment_external_shipment_id ON shipment(external_shipment_id);


CREATE TABLE IF NOT EXISTS location_type(
    id              BIGSERIAL                   PRIMARY KEY,
    code            VARCHAR                     NOT NULL UNIQUE,
    length_cm       BIGINT,
    width_cm        BIGINT,
    height_cm       BIGINT,
    unlimited       BOOLEAN                     NOT NULL DEFAULT false,
    created_at      TIMESTAMP WITH TIME ZONE    NOT NULL,
    updated_at      TIMESTAMP WITH TIME ZONE    NOT NULL DEFAULT now()
);
ALTER TABLE location_type
ADD CONSTRAINT location_type_dimensions_check
CHECK (
    (unlimited = true)
    OR
    (unlimited = false AND length_cm IS NOT NULL AND width_cm IS NOT NULL AND height_cm IS NOT NULL)
);
INSERT INTO location_type(code, unlimited, created_at, updated_at) VALUES
    ('UNLIMITED', true, now(), now());

CREATE TABLE IF NOT EXISTS zone(
    id          BIGSERIAL                   PRIMARY KEY,
    code        VARCHAR                     NOT NULL UNIQUE,
    type        VARCHAR                     NOT NULL,
    created_at  TIMESTAMP WITH TIME ZONE    NOT NULL,
    updated_at  TIMESTAMP WITH TIME ZONE    NOT NULL DEFAULT now()
);
INSERT INTO zone (code, type, created_at, updated_at) VALUES
    ('DROPPING_INBOUND', 'INBOUND', now(), now()),
    ('DROPPING_STORAGE', 'STORAGE', now(), now());


CREATE TABLE IF NOT EXISTS location(
    id                  BIGSERIAL                   PRIMARY KEY,
    code                VARCHAR                     NOT NULL UNIQUE,
    location_type_id    BIGINT                      NOT NULL REFERENCES location_type(id),
    zone_id             BIGINT                      NOT NULL REFERENCES zone(id),
    created_at          TIMESTAMP WITH TIME ZONE    NOT NULL,
    updated_at          TIMESTAMP WITH TIME ZONE    NOT NULL DEFAULT now()
);
INSERT INTO location(code, location_type_id, zone_id, created_at, updated_at) VALUES
    ('INBOUND_DROPPING', 1, 1, now(), now()),
    ('STORAGE_DROPPING', 1, 2, now(), now());


CREATE TABLE IF NOT EXISTS shipment_unit(
    id              BIGSERIAL                   PRIMARY KEY,
    shipment_id     BIGINT                      NOT NULL REFERENCES shipment(id),
    amount          BIGINT                      NOT NULL,
    length_cm       BIGINT                      NOT NULL,
    width_cm        BIGINT                      NOT NULL,
    height_cm       BIGINT                      NOT NULL,
    location_id     BIGINT                      REFERENCES location(id),
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

CREATE TABLE IF NOT EXISTS receiver(
    id          BIGSERIAL                   PRIMARY KEY,
    name        VARCHAR                     NOT NULL,
    code        VARCHAR                     NOT NULL UNIQUE,
    created_at  TIMESTAMP WITH TIME ZONE    NOT NULL,
    updated_at  TIMESTAMP WITH TIME ZONE    NOT NULL DEFAULT now()
);

CREATE TABLE IF NOT EXISTS outbound_shipment(
    id                      BIGSERIAL                   PRIMARY KEY,
    external_shipment_id    VARCHAR,
    receiver_id             BIGINT                      NOT NULL REFERENCES receiver(id),
    status                  VARCHAR                     NOT NULL,
    created_at              TIMESTAMP WITH TIME ZONE    NOT NULL,
    updated_at              TIMESTAMP WITH TIME ZONE    NOT NULL DEFAULT now()
);
CREATE INDEX outbound_shipment_external_shipment_id ON outbound_shipment(external_shipment_id);

CREATE TABLE IF NOT EXISTS withdrawal(
    id                      BIGSERIAL                   PRIMARY KEY,
    product_code            VARCHAR                     NOT NULL,
    amount                  BIGINT                      NOT NULL,
    status                  VARCHAR                     NOT NULL,
    outbound_shipment_id    BIGINT                      NOT NULL REFERENCES outbound_shipment(id),
    created_at              TIMESTAMP WITH TIME ZONE    NOT NULL,
    updated_at              TIMESTAMP WITH TIME ZONE    NOT NULL DEFAULT now()
);

CREATE TABLE "user_roles"
(
    "id"   integer primary key,
    "name" varchar UNIQUE NOT NULL
);



CREATE TABLE "fsc_type"
(
    "id"   serial PRIMARY KEY,
    "name" varchar UNIQUE NOT NULL
);

CREATE TABLE "services"
(
    "id"   serial PRIMARY KEY,
    "name" varchar,
    "host" varchar
);

CREATE TABLE "accounts"
(
    "id"             serial PRIMARY KEY,
    "balance"        int NOT NULL,
    "account_number" varchar(12) unique NOT NULL ,
    "disabled"       boolean
);

CREATE TABLE "users"
(
    "id"         serial PRIMARY KEY,
    "username"   varchar UNIQUE,
    "name"       varchar        NOT NULL,
    "surname"    varchar        NOT NULL,
    "lastname"   varchar,
    "password"   varchar        NOT NULL,
    "email"      varchar UNIQUE NOT NULL,
    "role_id"    integer,
    "account_id" integer,
    "created_at" timestamp      NOT NULL,
    "disabled"   boolean        NOT NULL
);

CREATE TABLE "fsc"
(
    "id"          serial PRIMARY KEY,
    "name"        varchar UNIQUE NOT NULL,
    "account_id"  integer,
    "created_at"  timestamp      NOT NULL,
    "fsc_type_id" integer,
    "owner_id"    integer,
    "disabled"    boolean        NOT NULL,
    "service_id"  integer
);

CREATE TABLE "transactions"
(
    "id"              bigserial PRIMARY KEY,
    "from_number"     varchar(12) NOT NULL,
    "to_number"       varchar(12) NOT NULL,
    "time"            timestamp   NOT NULL,
    "payment_purpose" bigint,
    "payment_comment" varchar,
    "amount"          bigint
);

CREATE TABLE "fsc_teams"
(
    "id"      bigserial PRIMARY KEY,
    "user_id" bigint,
    "fsc_id"  bigint,
    UNIQUE ("user_id", "fsc_id")
);

CREATE TABLE "payment_purposes"
(
    "id"   bigserial PRIMARY KEY,
    "name" varchar NOT NULL
);

CREATE TABLE "bills"
(
    "id"             serial PRIMARY KEY,
    "fsc_id"         bigint,
    "user_id"        bigint,
    "amount"         bigint,
    "status_id"      integer,
    "created_at"     timestamp NOT NULL,
    "transaction_id" bigint UNIQUE
);

CREATE TABLE "bill_statuses"
(
    "id"   serial PRIMARY KEY,
    "name" varchar NOT NULL
);

ALTER TABLE "users"
    ADD FOREIGN KEY ("role_id") REFERENCES "user_roles" ("id");

ALTER TABLE "users"
    ADD FOREIGN KEY ("account_id") REFERENCES "accounts" ("id");

ALTER TABLE "fsc"
    ADD FOREIGN KEY ("owner_id") REFERENCES "users" ("id");

ALTER TABLE "fsc"
    ADD FOREIGN KEY ("fsc_type_id") REFERENCES "fsc_type" ("id");

ALTER TABLE "fsc"
    ADD FOREIGN KEY ("account_id") REFERENCES "accounts" ("id");

ALTER TABLE "fsc"
    ADD FOREIGN KEY ("service_id") REFERENCES "services" ("id");

ALTER TABLE "transactions"
    ADD FOREIGN KEY ("from_number") REFERENCES "accounts" ("account_number");

ALTER TABLE "transactions"
    ADD FOREIGN KEY ("to_number") REFERENCES "accounts" ("account_number");

ALTER TABLE "transactions"
    ADD FOREIGN KEY ("payment_purpose") REFERENCES "payment_purposes" ("id");

ALTER TABLE "fsc_teams"
    ADD FOREIGN KEY ("user_id") REFERENCES "users" ("id");

ALTER TABLE "fsc_teams"
    ADD FOREIGN KEY ("fsc_id") REFERENCES "fsc" ("id");

ALTER TABLE "bills"
    ADD FOREIGN KEY ("fsc_id") REFERENCES "fsc" ("id");

ALTER TABLE "bills"
    ADD FOREIGN KEY ("user_id") REFERENCES "users" ("id");

ALTER TABLE "bills"
    ADD FOREIGN KEY ("status_id") REFERENCES "bill_statuses" ("id");

ALTER TABLE "bills"
    ADD FOREIGN KEY ("transaction_id") REFERENCES "transactions" ("id");


INSERT INTO user_roles (id, name)
VALUES (0, 'ROLE_ADMIN');
INSERT INTO user_roles (id, name)
VALUES (1, 'ROLE_FSC_OWNER');
INSERT INTO user_roles (id, name)
VALUES (2, 'ROLE_USER');

INSERT INTO payment_purposes(name)
VALUES ('Благодарность'), ('На конфеты') ,('Другое');






CREATE TABLE "user_roles" (
                              "id" integer primary key ,
                              "name" varchar UNIQUE NOT NULL
);



CREATE TABLE "fsc_type" (
                            "id" serial PRIMARY KEY,
                            "name" varchar UNIQUE NOT NULL
);

CREATE TABLE "services" (
                            "id" serial PRIMARY KEY,
                            "name" varchar,
                            "host" varchar
);

CREATE TABLE "accounts" (
                            "id" serial PRIMARY KEY,
                            "balance" int NOT NULL,
                            "user_account" boolean NOT NULL,
                            "disabled" boolean
);

CREATE TABLE "users" (
                         "id" serial PRIMARY KEY,
                         "username" varchar UNIQUE,
                         "name" varchar NOT NULL,
                         "surname" varchar NOT NULL,
                         "lastname" varchar,
                        "password" varchar NOT NULL ,
                         "email" varchar UNIQUE NOT NULL,
                         "role_id" integer,
                         "account_id" integer,
                         "created_at" timestamp NOT NULL,
                         "disabled" boolean NOT NULL
);

CREATE TABLE "fsc" (
                       "id" serial PRIMARY KEY,
                       "name" varchar UNIQUE NOT NULL,
                       "account_id" integer,
                       "created_at" timestamp NOT NULL,
                       "fsc_type_id" integer,
                       "owner_id" integer,
                       "disabled" boolean NOT NULL,
                       "service_id" integer
);

CREATE TABLE "transactions" (
                                "id" serial PRIMARY KEY,
                                "from_id" integer NOT NULL,
                                "to_id" integer NOT NULL,
                                "time" timestamp NOT NULL,
                                "payment_purpose" integer,
                                "payment_comment" varchar,
                                "amount" integer
);

CREATE TABLE "fsc_teams" (
                             "id" serial PRIMARY KEY,
                             "user_id" integer,
                             "fsc_id" integer,
                             UNIQUE ("user_id", "fsc_id")
);

CREATE TABLE "payment_purposes" (
                                    "id" serial PRIMARY KEY,
                                    "name" varchar NOT NULL
);

CREATE TABLE "bills" (
                         "id" serial PRIMARY KEY,
                         "fsc_id" integer,
                         "user_id" integer,
                         "amount" integer,
                         "status_id" integer,
                         "created_at" timestamp NOT NULL,
                         "transaction_id" integer UNIQUE
);

CREATE TABLE "bill_statuses" (
                                 "id" serial PRIMARY KEY,
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
    ADD FOREIGN KEY ("from_id") REFERENCES "accounts" ("id");

ALTER TABLE "transactions"
    ADD FOREIGN KEY ("to_id") REFERENCES "accounts" ("id");

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


INSERT INTO user_roles (id,name) VALUES (0,'ROLE_ADMIN');
INSERT INTO user_roles (id,name) VALUES(1,'ROLE_FSC_OWNER');
INSERT INTO user_roles (id,name) VALUES(2,'ROLE_USER');





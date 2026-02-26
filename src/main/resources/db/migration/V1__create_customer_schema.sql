-- ============================================================
-- V1__create_customer_schema.sql
-- Creates the customer table schema for the Customer MS.
-- Replaces JPA ddl-auto schema generation.
-- ============================================================

CREATE TABLE IF NOT EXISTS customer
(
    customer_id     INT          NOT NULL AUTO_INCREMENT,
    first_name      VARCHAR(30)  NOT NULL,
    last_name       VARCHAR(30)  NOT NULL,
    document_number VARCHAR(15)  NOT NULL,
    email           VARCHAR(50)  NOT NULL,
    phone_number    VARCHAR(12)  NOT NULL,
    address         VARCHAR(150) NOT NULL,
    creation_date   DATETIME     NOT NULL,
    update_date     DATETIME     NOT NULL,
    active          TINYINT(1)   NOT NULL DEFAULT 1,

    CONSTRAINT pk_customer PRIMARY KEY (customer_id),
    CONSTRAINT uq_customer_document_number UNIQUE (document_number)
);
-- ============================================================
-- V2__seed_customer_data.sql
-- Inserts initial customer records.
-- creation_date and update_date are set to a fixed timestamp
-- so migrations are deterministic and repeatable across envs.
-- ============================================================

INSERT INTO customer (first_name, last_name, document_number, email, phone_number, address, creation_date, update_date,
                      active)
VALUES ('Julia', 'Mendez', '11111111', 'jmendez@mail.com', '963852741', 'Lima, Perú', NOW(), NOW(), 1),
       ('Alicia', 'Ramirez', '22222222', 'aramirez@mail.com', '741852963', 'Lima, Perú', NOW(), NOW(), 1),
       ('Jose', 'Melendez', '33333333', 'jmelendez@mail.com', '863214569', 'Lima, Perú', NOW(), NOW(), 1),
       ('Carla', 'Gomez', '44444444', 'cgomez@mail.com', '963963852', 'Lima, Perú', NOW(), NOW(), 1),
       ('Juan', 'Perez', '55555555', 'jperez@mail.com', '741874521', 'Lima, Perú', NOW(), NOW(), 1);
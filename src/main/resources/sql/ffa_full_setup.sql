-- ============================================================================
-- FFA Platform - Full Database Setup Script
-- ============================================================================
-- PostgreSQL compatible script that provisions the full schema and seed data
-- in a single execution. Run with a superuser or a role with sufficient rights.
-- ============================================================================

-- Optional: create database (uncomment when needed)
-- CREATE DATABASE ffa_platform;
-- \c ffa_platform;

BEGIN;

-- ----------------------------------------------------------------------------
-- Extensions
-- ----------------------------------------------------------------------------
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- ----------------------------------------------------------------------------
-- Drop existing objects (idempotent execution support)
-- ----------------------------------------------------------------------------
DROP TABLE IF EXISTS documents_submitted CASCADE;
DROP TABLE IF EXISTS documents_need_for_project CASCADE;
DROP TABLE IF EXISTS application CASCADE;
DROP TABLE IF EXISTS message CASCADE;
DROP TABLE IF EXISTS alert CASCADE;
DROP TABLE IF EXISTS document_type CASCADE;
DROP TABLE IF EXISTS project CASCADE;
DROP TABLE IF EXISTS institution CASCADE;
DROP TABLE IF EXISTS embassy CASCADE;
DROP TABLE IF EXISTS intervener CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS person CASCADE;
DROP TABLE IF EXISTS city CASCADE;
DROP TABLE IF EXISTS department CASCADE;
DROP TABLE IF EXISTS region CASCADE;
DROP TABLE IF EXISTS country CASCADE;
DROP TABLE IF EXISTS continent CASCADE;
DROP TABLE IF EXISTS role CASCADE;

-- ----------------------------------------------------------------------------
-- Schema definition
-- ----------------------------------------------------------------------------
CREATE TABLE role (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    creation_date DATE DEFAULT CURRENT_DATE
);

CREATE TABLE continent (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE country (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    phone_number_indicator VARCHAR(10),
    continent_id BIGINT REFERENCES continent(id),
    creation_date DATE DEFAULT CURRENT_DATE,
    last_modification_date DATE DEFAULT CURRENT_DATE,
    creator_user BIGINT,
    last_modificator_user BIGINT,
    is_deleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE region (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    country_id BIGINT REFERENCES country(id),
    creation_date DATE DEFAULT CURRENT_DATE,
    last_modification_date DATE DEFAULT CURRENT_DATE,
    creator_user BIGINT,
    last_modificator_user BIGINT,
    is_deleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE department (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    region_id BIGINT REFERENCES region(id),
    creation_date DATE DEFAULT CURRENT_DATE,
    last_modification_date DATE DEFAULT CURRENT_DATE,
    creator_user BIGINT,
    last_modificator_user BIGINT,
    is_deleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE city (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    postal_code INTEGER,
    department_id BIGINT REFERENCES department(id),
    creation_date DATE DEFAULT CURRENT_DATE,
    last_modification_date DATE DEFAULT CURRENT_DATE,
    creator_user BIGINT,
    last_modificator_user BIGINT,
    is_deleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE person (
    id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    address TEXT,
    login VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role_id BIGINT REFERENCES role(id),
    city_id BIGINT REFERENCES city(id),
    creation_date DATE DEFAULT CURRENT_DATE,
    last_modification_date DATE DEFAULT CURRENT_DATE,
    creator_user BIGINT,
    last_modificator_user BIGINT,
    is_deleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE embassy (
    id BIGSERIAL PRIMARY KEY,
    address TEXT NOT NULL,
    embassy_of_country_id BIGINT REFERENCES country(id),
    embassy_in_country_id BIGINT REFERENCES country(id),
    city_id BIGINT REFERENCES city(id),
    creation_date DATE DEFAULT CURRENT_DATE,
    last_modification_date DATE DEFAULT CURRENT_DATE,
    creator_user BIGINT,
    last_modificator_user BIGINT,
    is_deleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE institution (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    address TEXT,
    city_id BIGINT REFERENCES city(id),
    creation_date DATE DEFAULT CURRENT_DATE,
    last_modification_date DATE DEFAULT CURRENT_DATE,
    creator_user BIGINT,
    last_modificator_user BIGINT,
    is_deleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE project (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    description TEXT,
    submission_date DATE,
    intervener_id BIGINT REFERENCES person(id),
    winner_user_id BIGINT REFERENCES person(id),
    creation_date DATE DEFAULT CURRENT_DATE,
    last_modification_date DATE DEFAULT CURRENT_DATE,
    creator_user BIGINT,
    last_modificator_user BIGINT,
    is_deleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE application (
    id BIGSERIAL PRIMARY KEY,
    date_application TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    motivation TEXT,
    user_id BIGINT REFERENCES person(id),
    project_id BIGINT REFERENCES project(id),
    creation_date DATE DEFAULT CURRENT_DATE,
    last_modification_date DATE DEFAULT CURRENT_DATE,
    creator_user BIGINT,
    last_modificator_user BIGINT,
    is_deleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE document_type (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    project_id BIGINT REFERENCES project(id),
    last_modification_date DATE DEFAULT CURRENT_DATE,
    creator_user BIGINT,
    last_modificator_user BIGINT,
    is_deleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE documents_submitted (
    id BIGSERIAL PRIMARY KEY,
    path VARCHAR(500) NOT NULL,
    document_type_id BIGINT REFERENCES document_type(id),
    application_id BIGINT REFERENCES application(id),
    creation_date DATE DEFAULT CURRENT_DATE,
    last_modification_date DATE DEFAULT CURRENT_DATE,
    creator_user BIGINT,
    last_modificator_user BIGINT,
    is_deleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE documents_need_for_project (
    id BIGSERIAL PRIMARY KEY,
    is_mandatory BOOLEAN DEFAULT FALSE,
    max_number INTEGER DEFAULT 1,
    min_number INTEGER DEFAULT 1,
    document_type_id BIGINT REFERENCES document_type(id),
    project_id BIGINT REFERENCES project(id),
    creation_date DATE DEFAULT CURRENT_DATE,
    last_modification_date DATE DEFAULT CURRENT_DATE,
    creator_user BIGINT,
    last_modificator_user BIGINT,
    is_deleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE message (
    id BIGSERIAL PRIMARY KEY,
    content TEXT NOT NULL,
    sender_id BIGINT REFERENCES person(id),
    receiver_id BIGINT REFERENCES person(id),
    create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_read BOOLEAN DEFAULT FALSE,
    read_date TIMESTAMP,
    reply_to BIGINT REFERENCES message(id),
    is_deleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE alert (
    id BIGSERIAL PRIMARY KEY,
    content TEXT NOT NULL,
    receiver_id BIGINT REFERENCES person(id),
    is_read BOOLEAN DEFAULT FALSE,
    read_date TIMESTAMP,
    is_deleted BOOLEAN DEFAULT FALSE,
    alert_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE users (
    person_id BIGINT PRIMARY KEY REFERENCES person(id)
);

CREATE TABLE intervener (
    person_id BIGINT PRIMARY KEY REFERENCES person(id),
    embassy_id BIGINT REFERENCES embassy(id)
);

-- Indexes
CREATE INDEX idx_person_email ON person(email);
CREATE INDEX idx_person_login ON person(login);
CREATE INDEX idx_person_role_id ON person(role_id);
CREATE INDEX idx_person_city_id ON person(city_id);
CREATE INDEX idx_person_is_deleted ON person(is_deleted);

CREATE INDEX idx_country_continent_id ON country(continent_id);
CREATE INDEX idx_country_is_deleted ON country(is_deleted);

CREATE INDEX idx_embassy_embassy_of_country_id ON embassy(embassy_of_country_id);
CREATE INDEX idx_embassy_embassy_in_country_id ON embassy(embassy_in_country_id);
CREATE INDEX idx_embassy_city_id ON embassy(city_id);
CREATE INDEX idx_embassy_is_deleted ON embassy(is_deleted);

CREATE INDEX idx_project_intervener_id ON project(intervener_id);
CREATE INDEX idx_project_winner_user_id ON project(winner_user_id);
CREATE INDEX idx_project_submission_date ON project(submission_date);
CREATE INDEX idx_project_is_deleted ON project(is_deleted);

CREATE INDEX idx_application_user_id ON application(user_id);
CREATE INDEX idx_application_project_id ON application(project_id);
CREATE INDEX idx_application_date_application ON application(date_application);
CREATE INDEX idx_application_is_deleted ON application(is_deleted);

CREATE INDEX idx_message_sender_id ON message(sender_id);
CREATE INDEX idx_message_receiver_id ON message(receiver_id);
CREATE INDEX idx_message_create_date ON message(create_date);
CREATE INDEX idx_message_is_read ON message(is_read);

CREATE INDEX idx_alert_receiver_id ON alert(receiver_id);
CREATE INDEX idx_alert_is_read ON alert(is_read);
CREATE INDEX idx_alert_alert_date ON alert(alert_date);

-- Constraints
ALTER TABLE person ADD CONSTRAINT chk_person_email CHECK (email ~* '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$');
ALTER TABLE person ADD CONSTRAINT chk_person_login CHECK (LENGTH(login) >= 3);
ALTER TABLE person ADD CONSTRAINT chk_person_password CHECK (LENGTH(password) >= 6);

ALTER TABLE country ADD CONSTRAINT uk_country_name UNIQUE (name);
ALTER TABLE person ADD CONSTRAINT uk_person_email UNIQUE (email);
ALTER TABLE person ADD CONSTRAINT uk_person_login UNIQUE (login);

-- Comments
COMMENT ON TABLE role IS 'User roles in the system';
COMMENT ON TABLE continent IS 'Continents of the world';
COMMENT ON TABLE country IS 'Countries information';
COMMENT ON TABLE region IS 'Regions within countries';
COMMENT ON TABLE department IS 'Departments within regions';
COMMENT ON TABLE city IS 'Cities within departments';
COMMENT ON TABLE person IS 'Base person/user information';
COMMENT ON TABLE embassy IS 'Embassy information';
COMMENT ON TABLE institution IS 'Institution information';
COMMENT ON TABLE project IS 'Project information';
COMMENT ON TABLE application IS 'Project applications';
COMMENT ON TABLE document_type IS 'Types of documents';
COMMENT ON TABLE documents_submitted IS 'Submitted documents';
COMMENT ON TABLE documents_need_for_project IS 'Required documents for projects';
COMMENT ON TABLE message IS 'User messages';
COMMENT ON TABLE alert IS 'System alerts';
COMMENT ON TABLE users IS 'Regular users (extends person)';
COMMENT ON TABLE intervener IS 'Interveners (extends person)';

-- ----------------------------------------------------------------------------
-- Seed data
-- ----------------------------------------------------------------------------
INSERT INTO role (name) VALUES 
('ADMIN'),
('INTERVENER'),
('USER');

INSERT INTO continent (name) VALUES 
('Africa'),
('Asia'),
('Europe'),
('North America'),
('South America'),
('Oceania'),
('Antarctica');

INSERT INTO country (name, phone_number_indicator, continent_id) VALUES 
('Nigeria', '+234', 1),
('South Africa', '+27', 1),
('Kenya', '+254', 1),
('Ghana', '+233', 1),
('Morocco', '+212', 1),
('Egypt', '+20', 1),
('Ethiopia', '+251', 1),
('Tanzania', '+255', 1),
('Uganda', '+256', 1),
('Algeria', '+213', 1),
('France', '+33', 3),
('Germany', '+49', 3),
('United Kingdom', '+44', 3),
('Italy', '+39', 3),
('Spain', '+34', 3),
('Netherlands', '+31', 3),
('Belgium', '+32', 3),
('Switzerland', '+41', 3),
('Austria', '+43', 3),
('Sweden', '+46', 3),
('China', '+86', 2),
('India', '+91', 2),
('Japan', '+81', 2),
('South Korea', '+82', 2),
('Thailand', '+66', 2),
('Singapore', '+65', 2),
('Malaysia', '+60', 2),
('Indonesia', '+62', 2),
('Philippines', '+63', 2),
('Vietnam', '+84', 2),
('United States', '+1', 4),
('Canada', '+1', 4),
('Mexico', '+52', 4),
('Brazil', '+55', 5),
('Argentina', '+54', 5),
('Chile', '+56', 5),
('Colombia', '+57', 5),
('Peru', '+51', 5);

INSERT INTO region (name, country_id) VALUES 
('Lagos State', 1),
('Abuja FCT', 1),
('Kano State', 1),
('Rivers State', 1),
('Ogun State', 1),
('Île-de-France', 11),
('Provence-Alpes-Côte d''Azur', 11),
('Auvergne-Rhône-Alpes', 11),
('Occitanie', 11),
('Nouvelle-Aquitaine', 11);

INSERT INTO department (name, region_id) VALUES 
('Lagos Island', 1),
('Lagos Mainland', 1),
('Ikeja', 1),
('Eti-Osa', 1),
('Surulere', 1),
('Paris', 6),
('Seine-et-Marne', 6),
('Yvelines', 6),
('Essonne', 6),
('Hauts-de-Seine', 6);

INSERT INTO city (name, postal_code, department_id) VALUES 
('Victoria Island', 101241, 1),
('Ikoyi', 101233, 1),
('Lagos Island', 101223, 1),
('Yaba', 101212, 2),
('Surulere', 101283, 2),
('Ikeja', 100001, 3),
('Maryland', 100002, 3),
('Lekki', 105102, 4),
('Ajah', 105101, 4),
('Paris 1er', 75001, 6),
('Paris 2e', 75002, 6),
('Paris 3e', 75003, 6),
('Paris 4e', 75004, 6),
('Paris 5e', 75005, 6),
('Versailles', 78000, 7),
('Boulogne-Billancourt', 92100, 8),
('Nanterre', 92000, 8);

INSERT INTO person (first_name, last_name, email, address, login, password, role_id, city_id) VALUES 
('Admin', 'User', 'admin@ffa.com', '123 Admin Street, Lagos', 'admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 1, 1),
('System', 'Administrator', 'sysadmin@ffa.com', '456 System Ave, Lagos', 'sysadmin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 1, 2),
('John', 'Smith', 'john.smith@embassy.com', '789 Embassy Road, Lagos', 'johnsmith', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 2, 1),
('Marie', 'Dubois', 'marie.dubois@consulate.com', '321 Consulate Street, Paris', 'mariedubois', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 2, 6),
('Ahmed', 'Hassan', 'ahmed.hassan@embassy.com', '654 Embassy Lane, Lagos', 'ahmedhassan', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 2, 3),
('Alice', 'Johnson', 'alice.johnson@email.com', '111 User Street, Lagos', 'alicejohnson', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 3, 4),
('Bob', 'Wilson', 'bob.wilson@email.com', '222 User Avenue, Lagos', 'bobwilson', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 3, 5),
('Carol', 'Brown', 'carol.brown@email.com', '333 User Road, Paris', 'carolbrown', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 3, 7),
('David', 'Davis', 'david.davis@email.com', '444 User Lane, Lagos', 'daviddavis', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 3, 6),
('Eva', 'Miller', 'eva.miller@email.com', '555 User Boulevard, Paris', 'evamiller', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 3, 8);

INSERT INTO embassy (address, embassy_of_country_id, embassy_in_country_id, city_id) VALUES 
('123 Diplomatic Avenue, Victoria Island, Lagos', 11, 1, 1),
('456 Consular Street, Ikoyi, Lagos', 12, 1, 2),
('789 Embassy Road, Paris 7e, Paris', 1, 11, 6),
('321 Diplomatic Lane, Paris 8e, Paris', 2, 11, 6);

INSERT INTO institution (name, address, city_id) VALUES 
('African Development Bank', 'Avenue Joseph Anoma, Abidjan', 1),
('World Bank Nigeria Office', 'Plot 1234, Central Business District, Abuja', 2),
('UNESCO Regional Office', '1 Rue Miollis, Paris 15e', 6),
('African Union Commission', 'Roosevelt Street, Addis Ababa', 3);

INSERT INTO project (name, description, submission_date, intervener_id, winner_user_id) VALUES 
('Agricultural Innovation Program', 'Support for modern farming techniques in rural communities', '2024-01-15', 3, 5),
('Education Technology Initiative', 'Digital learning platforms for schools in underserved areas', '2024-02-01', 4, 6),
('Clean Water Access Project', 'Installation of water purification systems in villages', '2024-01-20', 5, 7),
('Youth Entrepreneurship Program', 'Training and funding for young entrepreneurs', '2024-02-10', 3, 8),
('Women Empowerment Initiative', 'Skills development and microfinance for women', '2024-01-25', 4, 9);

INSERT INTO application (date_application, motivation, user_id, project_id) VALUES 
('2024-01-16 10:30:00', 'I am passionate about sustainable agriculture and have experience in community development. I believe this project will help improve food security in our region.', 5, 1),
('2024-01-17 14:20:00', 'As an educator, I have seen the challenges students face with limited access to technology. This initiative aligns perfectly with my goals to improve education quality.', 6, 2),
('2024-01-18 09:15:00', 'Clean water is a fundamental right. I want to contribute to this project to help communities access safe drinking water.', 7, 3),
('2024-01-19 16:45:00', 'I am a young entrepreneur with innovative ideas. This program will help me develop my business skills and create employment opportunities.', 8, 4),
('2024-01-20 11:30:00', 'Empowering women is crucial for community development. I want to be part of this initiative to help other women achieve their potential.', 9, 5);

INSERT INTO document_type (name, project_id) VALUES 
('Project Proposal', 1),
('Budget Plan', 1),
('Timeline', 1),
('Technical Specification', 2),
('Implementation Plan', 2),
('Financial Report', 2),
('Community Impact Assessment', 3),
('Environmental Impact Study', 3),
('Business Plan', 4),
('Market Analysis', 4),
('Training Curriculum', 5),
('Impact Measurement Framework', 5);

INSERT INTO documents_need_for_project (is_mandatory, max_number, min_number, document_type_id, project_id) VALUES 
(TRUE, 1, 1, 1, 1),
(TRUE, 1, 1, 2, 1),
(FALSE, 1, 0, 3, 1),
(TRUE, 1, 1, 4, 2),
(TRUE, 1, 1, 5, 2),
(FALSE, 1, 0, 6, 2),
(TRUE, 1, 1, 7, 3),
(TRUE, 1, 1, 8, 3),
(TRUE, 1, 1, 9, 4),
(TRUE, 1, 1, 10, 4),
(TRUE, 1, 1, 11, 5),
(TRUE, 1, 1, 12, 5);

INSERT INTO message (content, sender_id, receiver_id, create_date) VALUES 
('Welcome to the FFA platform! We are excited to have you join our community.', 1, 5, '2024-01-15 08:00:00'),
('Your application for the Agricultural Innovation Program has been received. We will review it and get back to you soon.', 3, 5, '2024-01-16 10:35:00'),
('Thank you for your interest in our Education Technology Initiative. We will contact you for the next steps.', 4, 6, '2024-01-17 14:25:00'),
('Congratulations! Your application has been approved. Please check your email for further instructions.', 3, 5, '2024-01-20 09:00:00'),
('We have some questions about your project proposal. Could you please provide more details about the implementation timeline?', 4, 6, '2024-01-18 15:30:00');

INSERT INTO alert (content, receiver_id, alert_date) VALUES 
('New project available: Youth Entrepreneurship Program', 5, '2024-01-15 10:00:00'),
('Application deadline approaching: Clean Water Access Project', 7, '2024-01-19 14:00:00'),
('Your application status has been updated', 6, '2024-01-17 16:00:00'),
('New message received from project coordinator', 8, '2024-01-18 11:00:00'),
('System maintenance scheduled for tonight at 2 AM', 5, '2024-01-20 20:00:00');

INSERT INTO users (person_id) VALUES (5), (6), (7), (8), (9);

INSERT INTO intervener (person_id, embassy_id) VALUES 
(3, 1),
(4, 2),
(5, 3);

-- Align sequences with inserted data
SELECT setval('role_id_seq', (SELECT COALESCE(MAX(id), 1) FROM role));
SELECT setval('continent_id_seq', (SELECT COALESCE(MAX(id), 1) FROM continent));
SELECT setval('country_id_seq', (SELECT COALESCE(MAX(id), 1) FROM country));
SELECT setval('region_id_seq', (SELECT COALESCE(MAX(id), 1) FROM region));
SELECT setval('department_id_seq', (SELECT COALESCE(MAX(id), 1) FROM department));
SELECT setval('city_id_seq', (SELECT COALESCE(MAX(id), 1) FROM city));
SELECT setval('person_id_seq', (SELECT COALESCE(MAX(id), 1) FROM person));
SELECT setval('embassy_id_seq', (SELECT COALESCE(MAX(id), 1) FROM embassy));
SELECT setval('institution_id_seq', (SELECT COALESCE(MAX(id), 1) FROM institution));
SELECT setval('project_id_seq', (SELECT COALESCE(MAX(id), 1) FROM project));
SELECT setval('application_id_seq', (SELECT COALESCE(MAX(id), 1) FROM application));
SELECT setval('document_type_id_seq', (SELECT COALESCE(MAX(id), 1) FROM document_type));
SELECT setval('documents_submitted_id_seq', (SELECT COALESCE(MAX(id), 1) FROM documents_submitted));
SELECT setval('documents_need_for_project_id_seq', (SELECT COALESCE(MAX(id), 1) FROM documents_need_for_project));
SELECT setval('message_id_seq', (SELECT COALESCE(MAX(id), 1) FROM message));
SELECT setval('alert_id_seq', (SELECT COALESCE(MAX(id), 1) FROM alert));

COMMIT;

-- ----------------------------------------------------------------------------
-- Verification queries (optional)
-- ----------------------------------------------------------------------------
SELECT schemaname, tablename, tableowner
FROM pg_tables
WHERE schemaname = 'public'
ORDER BY tablename;

SELECT 'role' AS table_name, COUNT(*) AS row_count FROM role
UNION ALL SELECT 'continent', COUNT(*) FROM continent
UNION ALL SELECT 'country', COUNT(*) FROM country
UNION ALL SELECT 'person', COUNT(*) FROM person
UNION ALL SELECT 'project', COUNT(*) FROM project
UNION ALL SELECT 'application', COUNT(*) FROM application
ORDER BY table_name;

SELECT 'FFA Platform database initialization completed successfully!' AS status;


-- FFA Platform Database Schema
-- PostgreSQL Database Script

-- Create database (run this separately)
-- CREATE DATABASE ffa_platform;
-- \c ffa_platform;

-- Enable UUID extension
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- ==================== CORE TABLES ====================

-- Role table
CREATE TABLE role (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    creation_date DATE DEFAULT CURRENT_DATE
);

-- Continent table
CREATE TABLE continent (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);

-- Country table
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

-- Region table
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

-- Department table
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

-- City table
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

-- Person table (base user table)
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

-- Embassy table
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

-- Institution table
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

-- Project table
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

-- Application table
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

-- Document Type table
CREATE TABLE document_type (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    project_id BIGINT REFERENCES project(id),
    last_modification_date DATE DEFAULT CURRENT_DATE,
    creator_user BIGINT,
    last_modificator_user BIGINT,
    is_deleted BOOLEAN DEFAULT FALSE
);

-- Documents Submitted table
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

-- Documents Need For Project table
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

-- Message table
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

-- Alert table
CREATE TABLE alert (
    id BIGSERIAL PRIMARY KEY,
    content TEXT NOT NULL,
    receiver_id BIGINT REFERENCES person(id),
    is_read BOOLEAN DEFAULT FALSE,
    read_date TIMESTAMP,
    is_deleted BOOLEAN DEFAULT FALSE,
    alert_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ==================== USER ROLE TABLES ====================

-- User table (extends person)
CREATE TABLE users (
    person_id BIGINT PRIMARY KEY REFERENCES person(id)
);

-- Intervener table (extends person)
CREATE TABLE intervener (
    person_id BIGINT PRIMARY KEY REFERENCES person(id),
    embassy_id BIGINT REFERENCES embassy(id)
);

-- ==================== INDEXES ====================

-- Person indexes
CREATE INDEX idx_person_email ON person(email);
CREATE INDEX idx_person_login ON person(login);
CREATE INDEX idx_person_role_id ON person(role_id);
CREATE INDEX idx_person_city_id ON person(city_id);
CREATE INDEX idx_person_is_deleted ON person(is_deleted);

-- Country indexes
CREATE INDEX idx_country_continent_id ON country(continent_id);
CREATE INDEX idx_country_is_deleted ON country(is_deleted);

-- Embassy indexes
CREATE INDEX idx_embassy_embassy_of_country_id ON embassy(embassy_of_country_id);
CREATE INDEX idx_embassy_embassy_in_country_id ON embassy(embassy_in_country_id);
CREATE INDEX idx_embassy_city_id ON embassy(city_id);
CREATE INDEX idx_embassy_is_deleted ON embassy(is_deleted);

-- Project indexes
CREATE INDEX idx_project_intervener_id ON project(intervener_id);
CREATE INDEX idx_project_winner_user_id ON project(winner_user_id);
CREATE INDEX idx_project_submission_date ON project(submission_date);
CREATE INDEX idx_project_is_deleted ON project(is_deleted);

-- Application indexes
CREATE INDEX idx_application_user_id ON application(user_id);
CREATE INDEX idx_application_project_id ON application(project_id);
CREATE INDEX idx_application_date_application ON application(date_application);
CREATE INDEX idx_application_is_deleted ON application(is_deleted);

-- Message indexes
CREATE INDEX idx_message_sender_id ON message(sender_id);
CREATE INDEX idx_message_receiver_id ON message(receiver_id);
CREATE INDEX idx_message_create_date ON message(create_date);
CREATE INDEX idx_message_is_read ON message(is_read);

-- Alert indexes
CREATE INDEX idx_alert_receiver_id ON alert(receiver_id);
CREATE INDEX idx_alert_is_read ON alert(is_read);
CREATE INDEX idx_alert_alert_date ON alert(alert_date);

-- ==================== CONSTRAINTS ====================

-- Add check constraints
ALTER TABLE person ADD CONSTRAINT chk_person_email CHECK (email ~* '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$');
ALTER TABLE person ADD CONSTRAINT chk_person_login CHECK (LENGTH(login) >= 3);
ALTER TABLE person ADD CONSTRAINT chk_person_password CHECK (LENGTH(password) >= 6);

-- Add unique constraints
ALTER TABLE country ADD CONSTRAINT uk_country_name UNIQUE (name);
ALTER TABLE person ADD CONSTRAINT uk_person_email UNIQUE (email);
ALTER TABLE person ADD CONSTRAINT uk_person_login UNIQUE (login);

-- ==================== COMMENTS ====================

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

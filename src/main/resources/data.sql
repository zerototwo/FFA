-- FFA Platform Database Initialization Script

-- Insert default roles
INSERT INTO role (id, name, creation_date) VALUES 
(1, 'ADMIN', CURRENT_DATE),
(2, 'INTERVENER', CURRENT_DATE),
(3, 'USER', CURRENT_DATE)
ON CONFLICT (id) DO NOTHING;

-- Insert default admin user
INSERT INTO person (id, first_name, last_name, email, address, login, role_id, city_id, creation_date, last_modification_date, creator_user, last_modificator_user, is_deleted) VALUES 
(1, 'Admin', 'User', 'admin@ffa.com', 'ISEP Paris', 'admin', 1, NULL, CURRENT_DATE, CURRENT_DATE, 1, 1, false)
ON CONFLICT (id) DO NOTHING;

-- Insert sample countries
INSERT INTO country (id, name, phone_number_indicator, continent_id, creation_date, last_modification_date, creator_user, last_modificator_user, is_deleted) VALUES 
(1, 'France', '+33', NULL, CURRENT_DATE, CURRENT_DATE, 1, 1, false),
(2, 'United States', '+1', NULL, CURRENT_DATE, CURRENT_DATE, 1, 1, false),
(3, 'Germany', '+49', NULL, CURRENT_DATE, CURRENT_DATE, 1, 1, false),
(4, 'United Kingdom', '+44', NULL, CURRENT_DATE, CURRENT_DATE, 1, 1, false),
(5, 'Spain', '+34', NULL, CURRENT_DATE, CURRENT_DATE, 1, 1, false)
ON CONFLICT (id) DO NOTHING;

-- Insert sample regions
INSERT INTO region (id, name, country_id, creation_date, last_modification_date, creator_user, last_modificator_user, is_deleted) VALUES 
(1, 'Île-de-France', 1, CURRENT_DATE, CURRENT_DATE, 1, 1, false),
(2, 'California', 2, CURRENT_DATE, CURRENT_DATE, 1, 1, false),
(3, 'Bavaria', 3, CURRENT_DATE, CURRENT_DATE, 1, 1, false),
(4, 'England', 4, CURRENT_DATE, CURRENT_DATE, 1, 1, false),
(5, 'Madrid', 5, CURRENT_DATE, CURRENT_DATE, 1, 1, false)
ON CONFLICT (id) DO NOTHING;

-- Insert sample departments
INSERT INTO department (id, name, region_id, creation_date, last_modification_date, creator_user, last_modificator_user, is_deleted) VALUES 
(1, 'Paris', 1, CURRENT_DATE, CURRENT_DATE, 1, 1, false),
(2, 'Los Angeles', 2, CURRENT_DATE, CURRENT_DATE, 1, 1, false),
(3, 'Munich', 3, CURRENT_DATE, CURRENT_DATE, 1, 1, false),
(4, 'London', 4, CURRENT_DATE, CURRENT_DATE, 1, 1, false),
(5, 'Madrid', 5, CURRENT_DATE, CURRENT_DATE, 1, 1, false)
ON CONFLICT (id) DO NOTHING;

-- Insert sample cities
INSERT INTO city (id, name, postal_code, department_id, creation_date, last_modification_date, creator_user, last_modificator_user, is_deleted) VALUES 
(1, 'Paris', 75001, 1, CURRENT_DATE, CURRENT_DATE, 1, 1, false),
(2, 'Los Angeles', 90210, 2, CURRENT_DATE, CURRENT_DATE, 1, 1, false),
(3, 'Munich', 80331, 3, CURRENT_DATE, CURRENT_DATE, 1, 1, false),
(4, 'London', 10001, 4, CURRENT_DATE, CURRENT_DATE, 1, 1, false),
(5, 'Madrid', 28001, 5, CURRENT_DATE, CURRENT_DATE, 1, 1, false)
ON CONFLICT (id) DO NOTHING;

-- Insert sample embassies
INSERT INTO embassy (id, address, embassy_of_country_id, embassy_in_country_id, city_id, creation_date, last_modification_date, creator_user, last_modificator_user, is_deleted) VALUES 
(1, '123 Avenue des Champs-Élysées, 75008 Paris', 1, 2, 1, CURRENT_DATE, CURRENT_DATE, 1, 1, false),
(2, '456 Wilshire Boulevard, Los Angeles, CA 90210', 2, 1, 2, CURRENT_DATE, CURRENT_DATE, 1, 1, false),
(3, '789 Maximilianstraße, 80539 Munich', 3, 1, 3, CURRENT_DATE, CURRENT_DATE, 1, 1, false),
(4, '321 Grosvenor Square, London W1K 2AH', 4, 1, 4, CURRENT_DATE, CURRENT_DATE, 1, 1, false),
(5, '654 Calle de Serrano, 28006 Madrid', 5, 1, 5, CURRENT_DATE, CURRENT_DATE, 1, 1, false)
ON CONFLICT (id) DO NOTHING;

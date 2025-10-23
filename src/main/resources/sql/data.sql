-- FFA Platform Test Data
-- PostgreSQL Database Script

-- ==================== ROLES ====================

INSERT INTO role (name) VALUES 
('ADMIN'),
('INTERVENER'),
('USER');

-- ==================== CONTINENTS ====================

INSERT INTO continent (name) VALUES 
('Africa'),
('Asia'),
('Europe'),
('North America'),
('South America'),
('Oceania'),
('Antarctica');

-- ==================== COUNTRIES ====================

INSERT INTO country (name, phone_number_indicator, continent_id) VALUES 
-- African countries
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

-- European countries
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

-- Asian countries
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

-- North American countries
('United States', '+1', 4),
('Canada', '+1', 4),
('Mexico', '+52', 4),

-- South American countries
('Brazil', '+55', 5),
('Argentina', '+54', 5),
('Chile', '+56', 5),
('Colombia', '+57', 5),
('Peru', '+51', 5);

-- ==================== REGIONS ====================

-- Nigeria regions
INSERT INTO region (name, country_id) VALUES 
('Lagos State', 1),
('Abuja FCT', 1),
('Kano State', 1),
('Rivers State', 1),
('Ogun State', 1);

-- France regions
INSERT INTO region (name, country_id) VALUES 
('Île-de-France', 11),
('Provence-Alpes-Côte d''Azur', 11),
('Auvergne-Rhône-Alpes', 11),
('Occitanie', 11),
('Nouvelle-Aquitaine', 11);

-- ==================== DEPARTMENTS ====================

-- Lagos State departments
INSERT INTO department (name, region_id) VALUES 
('Lagos Island', 1),
('Lagos Mainland', 1),
('Ikeja', 1),
('Eti-Osa', 1),
('Surulere', 1);

-- Île-de-France departments
INSERT INTO department (name, region_id) VALUES 
('Paris', 6),
('Seine-et-Marne', 6),
('Yvelines', 6),
('Essonne', 6),
('Hauts-de-Seine', 6);

-- ==================== CITIES ====================

-- Lagos cities
INSERT INTO city (name, postal_code, department_id) VALUES 
('Victoria Island', 101241, 1),
('Ikoyi', 101233, 1),
('Lagos Island', 101223, 1),
('Yaba', 101212, 2),
('Surulere', 101283, 2),
('Ikeja', 100001, 3),
('Maryland', 100002, 3),
('Lekki', 105102, 4),
('Ajah', 105101, 4);

-- Paris cities
INSERT INTO city (name, postal_code, department_id) VALUES 
('Paris 1er', 75001, 6),
('Paris 2e', 75002, 6),
('Paris 3e', 75003, 6),
('Paris 4e', 75004, 6),
('Paris 5e', 75005, 6),
('Versailles', 78000, 7),
('Boulogne-Billancourt', 92100, 8),
('Nanterre', 92000, 8);

-- ==================== PERSONS (USERS) ====================

-- Admin users
INSERT INTO person (first_name, last_name, email, address, login, password, role_id, city_id) VALUES 
('Admin', 'User', 'admin@ffa.com', '123 Admin Street, Lagos', 'admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 1, 1),
('System', 'Administrator', 'sysadmin@ffa.com', '456 System Ave, Lagos', 'sysadmin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 1, 2);

-- Intervener users
INSERT INTO person (first_name, last_name, email, address, login, password, role_id, city_id) VALUES 
('John', 'Smith', 'john.smith@embassy.com', '789 Embassy Road, Lagos', 'johnsmith', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 2, 1),
('Marie', 'Dubois', 'marie.dubois@consulate.com', '321 Consulate Street, Paris', 'mariedubois', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 2, 6),
('Ahmed', 'Hassan', 'ahmed.hassan@embassy.com', '654 Embassy Lane, Lagos', 'ahmedhassan', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 2, 3);

-- Regular users
INSERT INTO person (first_name, last_name, email, address, login, password, role_id, city_id) VALUES 
('Alice', 'Johnson', 'alice.johnson@email.com', '111 User Street, Lagos', 'alicejohnson', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 3, 4),
('Bob', 'Wilson', 'bob.wilson@email.com', '222 User Avenue, Lagos', 'bobwilson', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 3, 5),
('Carol', 'Brown', 'carol.brown@email.com', '333 User Road, Paris', 'carolbrown', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 3, 7),
('David', 'Davis', 'david.davis@email.com', '444 User Lane, Lagos', 'daviddavis', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 3, 6),
('Eva', 'Miller', 'eva.miller@email.com', '555 User Boulevard, Paris', 'evamiller', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 3, 8);

-- ==================== EMBASSIES ====================

INSERT INTO embassy (address, embassy_of_country_id, embassy_in_country_id, city_id) VALUES 
('123 Diplomatic Avenue, Victoria Island, Lagos', 11, 1, 1), -- French Embassy in Nigeria
('456 Consular Street, Ikoyi, Lagos', 12, 1, 2), -- German Embassy in Nigeria
('789 Embassy Road, Paris 7e, Paris', 1, 11, 6), -- Nigerian Embassy in France
('321 Diplomatic Lane, Paris 8e, Paris', 2, 11, 6); -- South African Embassy in France

-- ==================== INSTITUTIONS ====================

INSERT INTO institution (name, address, city_id) VALUES 
('African Development Bank', 'Avenue Joseph Anoma, Abidjan', 1),
('World Bank Nigeria Office', 'Plot 1234, Central Business District, Abuja', 2),
('UNESCO Regional Office', '1 Rue Miollis, Paris 15e', 6),
('African Union Commission', 'Roosevelt Street, Addis Ababa', 3);

-- ==================== PROJECTS ====================

INSERT INTO project (name, description, submission_date, intervener_id, winner_user_id) VALUES 
('Agricultural Innovation Program', 'Support for modern farming techniques in rural communities', '2024-01-15', 3, 5),
('Education Technology Initiative', 'Digital learning platforms for schools in underserved areas', '2024-02-01', 4, 6),
('Clean Water Access Project', 'Installation of water purification systems in villages', '2024-01-20', 5, 7),
('Youth Entrepreneurship Program', 'Training and funding for young entrepreneurs', '2024-02-10', 3, 8),
('Women Empowerment Initiative', 'Skills development and microfinance for women', '2024-01-25', 4, 9);

-- ==================== APPLICATIONS ====================

INSERT INTO application (date_application, motivation, user_id, project_id) VALUES 
('2024-01-16 10:30:00', 'I am passionate about sustainable agriculture and have experience in community development. I believe this project will help improve food security in our region.', 5, 1),
('2024-01-17 14:20:00', 'As an educator, I have seen the challenges students face with limited access to technology. This initiative aligns perfectly with my goals to improve education quality.', 6, 2),
('2024-01-18 09:15:00', 'Clean water is a fundamental right. I want to contribute to this project to help communities access safe drinking water.', 7, 3),
('2024-01-19 16:45:00', 'I am a young entrepreneur with innovative ideas. This program will help me develop my business skills and create employment opportunities.', 8, 4),
('2024-01-20 11:30:00', 'Empowering women is crucial for community development. I want to be part of this initiative to help other women achieve their potential.', 9, 5);

-- ==================== DOCUMENT TYPES ====================

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

-- ==================== DOCUMENTS NEEDED FOR PROJECTS ====================

INSERT INTO documents_need_for_project (is_mandatory, max_number, min_number, document_type_id, project_id) VALUES 
(TRUE, 1, 1, 1, 1), -- Project Proposal for Agricultural Innovation
(TRUE, 1, 1, 2, 1), -- Budget Plan for Agricultural Innovation
(FALSE, 1, 0, 3, 1), -- Timeline for Agricultural Innovation
(TRUE, 1, 1, 4, 2), -- Technical Specification for Education Technology
(TRUE, 1, 1, 5, 2), -- Implementation Plan for Education Technology
(FALSE, 1, 0, 6, 2), -- Financial Report for Education Technology
(TRUE, 1, 1, 7, 3), -- Community Impact Assessment for Clean Water
(TRUE, 1, 1, 8, 3), -- Environmental Impact Study for Clean Water
(TRUE, 1, 1, 9, 4), -- Business Plan for Youth Entrepreneurship
(TRUE, 1, 1, 10, 4), -- Market Analysis for Youth Entrepreneurship
(TRUE, 1, 1, 11, 5), -- Training Curriculum for Women Empowerment
(TRUE, 1, 1, 12, 5); -- Impact Measurement Framework for Women Empowerment

-- ==================== MESSAGES ====================

INSERT INTO message (content, sender_id, receiver_id, create_date) VALUES 
('Welcome to the FFA platform! We are excited to have you join our community.', 1, 5, '2024-01-15 08:00:00'),
('Your application for the Agricultural Innovation Program has been received. We will review it and get back to you soon.', 3, 5, '2024-01-16 10:35:00'),
('Thank you for your interest in our Education Technology Initiative. We will contact you for the next steps.', 4, 6, '2024-01-17 14:25:00'),
('Congratulations! Your application has been approved. Please check your email for further instructions.', 3, 5, '2024-01-20 09:00:00'),
('We have some questions about your project proposal. Could you please provide more details about the implementation timeline?', 4, 6, '2024-01-18 15:30:00');

-- ==================== ALERTS ====================

INSERT INTO alert (content, receiver_id, alert_date) VALUES 
('New project available: Youth Entrepreneurship Program', 5, '2024-01-15 10:00:00'),
('Application deadline approaching: Clean Water Access Project', 7, '2024-01-19 14:00:00'),
('Your application status has been updated', 6, '2024-01-17 16:00:00'),
('New message received from project coordinator', 8, '2024-01-18 11:00:00'),
('System maintenance scheduled for tonight at 2 AM', 5, '2024-01-20 20:00:00');

-- ==================== USER ROLE MAPPINGS ====================

-- Regular users
INSERT INTO users (person_id) VALUES 
(5), (6), (7), (8), (9);

-- Interveners
INSERT INTO intervener (person_id, embassy_id) VALUES 
(3, 1), -- John Smith - French Embassy
(4, 2), -- Marie Dubois - German Embassy
(5, 3); -- Ahmed Hassan - Nigerian Embassy

-- ==================== UPDATE SEQUENCES ====================

-- Update sequences to avoid conflicts
SELECT setval('role_id_seq', (SELECT MAX(id) FROM role));
SELECT setval('continent_id_seq', (SELECT MAX(id) FROM continent));
SELECT setval('country_id_seq', (SELECT MAX(id) FROM country));
SELECT setval('region_id_seq', (SELECT MAX(id) FROM region));
SELECT setval('department_id_seq', (SELECT MAX(id) FROM department));
SELECT setval('city_id_seq', (SELECT MAX(id) FROM city));
SELECT setval('person_id_seq', (SELECT MAX(id) FROM person));
SELECT setval('embassy_id_seq', (SELECT MAX(id) FROM embassy));
SELECT setval('institution_id_seq', (SELECT MAX(id) FROM institution));
SELECT setval('project_id_seq', (SELECT MAX(id) FROM project));
SELECT setval('application_id_seq', (SELECT MAX(id) FROM application));
SELECT setval('document_type_id_seq', (SELECT MAX(id) FROM document_type));
SELECT setval('documents_submitted_id_seq', (SELECT MAX(id) FROM documents_submitted));
SELECT setval('documents_need_for_project_id_seq', (SELECT MAX(id) FROM documents_need_for_project));
SELECT setval('message_id_seq', (SELECT MAX(id) FROM message));
SELECT setval('alert_id_seq', (SELECT MAX(id) FROM alert));

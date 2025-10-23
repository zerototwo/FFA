-- FFA Platform Database Cleanup Script
-- PostgreSQL Database Script

-- WARNING: This script will delete all data from the FFA platform database
-- Use with caution in production environments

-- ==================== DISABLE FOREIGN KEY CHECKS ====================

-- Disable triggers temporarily
SET session_replication_role = replica;

-- ==================== DELETE DATA IN REVERSE ORDER ====================

-- Delete from dependent tables first
DELETE FROM documents_submitted;
DELETE FROM documents_need_for_project;
DELETE FROM application;
DELETE FROM message;
DELETE FROM alert;
DELETE FROM document_type;
DELETE FROM project;
DELETE FROM institution;
DELETE FROM embassy;
DELETE FROM intervener;
DELETE FROM users;
DELETE FROM person;
DELETE FROM city;
DELETE FROM department;
DELETE FROM region;
DELETE FROM country;
DELETE FROM continent;
DELETE FROM role;

-- ==================== RESET SEQUENCES ====================

-- Reset all sequences to start from 1
ALTER SEQUENCE role_id_seq RESTART WITH 1;
ALTER SEQUENCE continent_id_seq RESTART WITH 1;
ALTER SEQUENCE country_id_seq RESTART WITH 1;
ALTER SEQUENCE region_id_seq RESTART WITH 1;
ALTER SEQUENCE department_id_seq RESTART WITH 1;
ALTER SEQUENCE city_id_seq RESTART WITH 1;
ALTER SEQUENCE person_id_seq RESTART WITH 1;
ALTER SEQUENCE embassy_id_seq RESTART WITH 1;
ALTER SEQUENCE institution_id_seq RESTART WITH 1;
ALTER SEQUENCE project_id_seq RESTART WITH 1;
ALTER SEQUENCE application_id_seq RESTART WITH 1;
ALTER SEQUENCE document_type_id_seq RESTART WITH 1;
ALTER SEQUENCE documents_submitted_id_seq RESTART WITH 1;
ALTER SEQUENCE documents_need_for_project_id_seq RESTART WITH 1;
ALTER SEQUENCE message_id_seq RESTART WITH 1;
ALTER SEQUENCE alert_id_seq RESTART WITH 1;

-- ==================== ENABLE FOREIGN KEY CHECKS ====================

-- Re-enable triggers
SET session_replication_role = DEFAULT;

-- ==================== VERIFY CLEANUP ====================

-- Check that all tables are empty
SELECT 'role' as table_name, COUNT(*) as row_count FROM role
UNION ALL
SELECT 'continent', COUNT(*) FROM continent
UNION ALL
SELECT 'country', COUNT(*) FROM country
UNION ALL
SELECT 'region', COUNT(*) FROM region
UNION ALL
SELECT 'department', COUNT(*) FROM department
UNION ALL
SELECT 'city', COUNT(*) FROM city
UNION ALL
SELECT 'person', COUNT(*) FROM person
UNION ALL
SELECT 'embassy', COUNT(*) FROM embassy
UNION ALL
SELECT 'institution', COUNT(*) FROM institution
UNION ALL
SELECT 'project', COUNT(*) FROM project
UNION ALL
SELECT 'application', COUNT(*) FROM application
UNION ALL
SELECT 'document_type', COUNT(*) FROM document_type
UNION ALL
SELECT 'documents_submitted', COUNT(*) FROM documents_submitted
UNION ALL
SELECT 'documents_need_for_project', COUNT(*) FROM documents_need_for_project
UNION ALL
SELECT 'message', COUNT(*) FROM message
UNION ALL
SELECT 'alert', COUNT(*) FROM alert
UNION ALL
SELECT 'users', COUNT(*) FROM users
UNION ALL
SELECT 'intervener', COUNT(*) FROM intervener;

-- All row counts should be 0

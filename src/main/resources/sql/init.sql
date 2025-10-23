-- FFA Platform Database Initialization Script
-- PostgreSQL Database Script

-- This script creates the database and initializes it with schema and data

-- ==================== CREATE DATABASE ====================

-- Create database (run as superuser)
-- CREATE DATABASE ffa_platform;
-- \c ffa_platform;

-- ==================== CREATE SCHEMA ====================

-- Run schema creation
\i schema.sql

-- ==================== INSERT INITIAL DATA ====================

-- Run data insertion
\i data.sql

-- ==================== VERIFY INSTALLATION ====================

-- Check database structure
SELECT 
    schemaname,
    tablename,
    tableowner
FROM pg_tables 
WHERE schemaname = 'public' 
ORDER BY tablename;

-- Check data counts
SELECT 
    'role' as table_name, 
    COUNT(*) as row_count 
FROM role
UNION ALL
SELECT 'continent', COUNT(*) FROM continent
UNION ALL
SELECT 'country', COUNT(*) FROM country
UNION ALL
SELECT 'person', COUNT(*) FROM person
UNION ALL
SELECT 'project', COUNT(*) FROM project
UNION ALL
SELECT 'application', COUNT(*) FROM application
ORDER BY table_name;

-- ==================== CREATE USERS AND PERMISSIONS ====================

-- Create application user
-- CREATE USER ffa_app WITH PASSWORD 'ffa_password_2024';
-- GRANT CONNECT ON DATABASE ffa_platform TO ffa_app;
-- GRANT USAGE ON SCHEMA public TO ffa_app;
-- GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA public TO ffa_app;
-- GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA public TO ffa_app;

-- Create read-only user for reporting
-- CREATE USER ffa_readonly WITH PASSWORD 'ffa_readonly_2024';
-- GRANT CONNECT ON DATABASE ffa_platform TO ffa_readonly;
-- GRANT USAGE ON SCHEMA public TO ffa_readonly;
-- GRANT SELECT ON ALL TABLES IN SCHEMA public TO ffa_readonly;

-- ==================== FINAL MESSAGE ====================

SELECT 'FFA Platform database initialization completed successfully!' as status;

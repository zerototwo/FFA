# FFA Platform Database Scripts

This directory contains all database-related scripts for the FFA Platform.

## 📁 File Structure

```
sql/
├── README.md                    # This file
├── schema.sql                   # Complete database schema
├── data.sql                     # Test data insertion
├── cleanup.sql                  # Database cleanup script
├── init.sql                     # Complete initialization script
└── migrations/
    └── V1__Initial_Schema.sql   # Flyway migration script
```

## 🚀 Quick Start

### 1. Create Database
```sql
-- Connect to PostgreSQL as superuser
CREATE DATABASE ffa_platform;
\c ffa_platform;
```

### 2. Initialize Database
```bash
# Run the complete initialization script
psql -d ffa_platform -f src/main/resources/sql/init.sql
```

### 3. Verify Installation
```sql
-- Check tables
\dt

-- Check data
SELECT COUNT(*) FROM person;
SELECT COUNT(*) FROM project;
SELECT COUNT(*) FROM application;
```

## 📋 Script Descriptions

### `schema.sql`
- **Purpose**: Creates the complete database schema
- **Contains**: Tables, indexes, constraints, comments
- **Usage**: Run once to create the database structure

### `data.sql`
- **Purpose**: Inserts test data for development
- **Contains**: Sample users, projects, applications, messages
- **Usage**: Run after schema creation to populate with test data

### `cleanup.sql`
- **Purpose**: Removes all data from the database
- **Contains**: DELETE statements and sequence resets
- **Usage**: Use with caution - deletes all data!

### `init.sql`
- **Purpose**: Complete database initialization
- **Contains**: Calls to schema.sql and data.sql
- **Usage**: One-stop script for database setup

### `migrations/V1__Initial_Schema.sql`
- **Purpose**: Flyway migration script
- **Contains**: Initial schema for version control
- **Usage**: Used by Flyway for database versioning

## 🔧 Database Configuration

### Connection Settings
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/ffa_platform
    username: ffa_app
    password: ffa_password_2024
    driver-class-name: org.postgresql.Driver
```

### User Permissions
- **ffa_app**: Full read/write access for application
- **ffa_readonly**: Read-only access for reporting

## 📊 Database Schema Overview

### Core Tables
- `role` - User roles (ADMIN, INTERVENER, USER)
- `person` - Base user information
- `country` - Country information
- `city` - City information
- `embassy` - Embassy information

### Business Tables
- `project` - Project information
- `application` - Project applications
- `document_type` - Document types
- `documents_submitted` - Submitted documents
- `documents_need_for_project` - Required documents

### Communication Tables
- `message` - User messages
- `alert` - System alerts

### User Role Tables
- `users` - Regular users
- `intervener` - Interveners (embassy staff)

## 🔍 Sample Queries

### Get All Users
```sql
SELECT p.first_name, p.last_name, p.email, r.name as role
FROM person p
JOIN role r ON p.role_id = r.id
WHERE p.is_deleted = FALSE;
```

### Get Projects with Applications
```sql
SELECT pr.name as project_name, COUNT(a.id) as application_count
FROM project pr
LEFT JOIN application a ON pr.id = a.project_id
WHERE pr.is_deleted = FALSE
GROUP BY pr.id, pr.name;
```

### Get User Messages
```sql
SELECT m.content, m.create_date, 
       sender.first_name || ' ' || sender.last_name as sender_name
FROM message m
JOIN person sender ON m.sender_id = sender.id
WHERE m.receiver_id = ? AND m.is_deleted = FALSE
ORDER BY m.create_date DESC;
```

## 🛠️ Maintenance

### Backup Database
```bash
pg_dump -h localhost -U ffa_app -d ffa_platform > backup.sql
```

### Restore Database
```bash
psql -h localhost -U ffa_app -d ffa_platform < backup.sql
```

### Reset Database
```bash
# Clean all data
psql -d ffa_platform -f cleanup.sql

# Reinitialize
psql -d ffa_platform -f init.sql
```

## 🔒 Security Notes

- Change default passwords in production
- Use environment variables for sensitive data
- Regularly backup the database
- Monitor database access logs
- Use SSL connections in production

## 📈 Performance Tips

- Indexes are created for frequently queried columns
- Use connection pooling in production
- Monitor query performance
- Consider partitioning for large tables
- Regular VACUUM and ANALYZE operations

## 🐛 Troubleshooting

### Common Issues

1. **Permission Denied**
   - Check user permissions
   - Verify database ownership

2. **Connection Refused**
   - Check PostgreSQL service status
   - Verify connection parameters

3. **Foreign Key Violations**
   - Check data insertion order
   - Verify referential integrity

4. **Sequence Issues**
   - Reset sequences after data import
   - Check sequence ownership

### Logs
- Check PostgreSQL logs for errors
- Monitor application logs for database issues
- Use EXPLAIN ANALYZE for slow queries

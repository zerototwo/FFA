# FFA Platform

A comprehensive platform for managing French international mobility programs, facilitating applications, embassy information, and project administration.

## Features

- **Country & Embassy Management**: Browse and search country information and embassy locations
- **Project Management**: View and apply for international mobility projects
- **Application System**: Submit and track project applications
- **User Management**: Role-based access control for different user types
- **Messaging & Alerts**: Internal communication and notification system
- **Document Management**: Upload and manage application documents

## Technology Stack

- **Backend Framework**: Spring Boot 3.2.0
- **Database**: PostgreSQL 15+
- **ORM**: MyBatis-Plus 3.5.14
- **Security**: Spring Security with JWT authentication
- **API Documentation**: SpringDoc OpenAPI 3.0 (Swagger)
- **Build Tool**: Maven
- **Java Version**: 17
- **Frontend**: Vue.js 3 + PrimeVue (Separate repository)

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.8+
- PostgreSQL 15+
- Docker (optional, for database setup)

### Installation

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd FFA
   ```

2. **Database Setup**
   
   Using Docker:
   ```bash
   docker run --name ffa-postgres \
     -e POSTGRES_DB=ffa_platform \
     -e POSTGRES_USER=admin \
     -e POSTGRES_PASSWORD=123456 \
     -p 5432:5432 \
     -d postgres:15
   ```

   Or use your existing PostgreSQL instance:
   ```sql
   CREATE DATABASE ffa_platform;
   CREATE USER admin WITH PASSWORD '123456';
   GRANT ALL PRIVILEGES ON DATABASE ffa_platform TO admin;
   ```

3. **Run Database Migrations**
   
   Connect to the database and execute the initialization scripts in `src/main/resources/db/migration/`

4. **Configuration**
   
   Update `src/main/resources/application.yaml` with your database credentials if needed.

5. **Build and Run**
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

   Or use your IDE to run `FfaApplication.java`

6. **Access the Application**
   - API Base URL: `http://localhost:8080/ffaAPI`
   - Swagger UI: `http://localhost:8080/ffaAPI/swagger-ui.html`
   - API Docs: `http://localhost:8080/ffaAPI/v3/api-docs`

## API Endpoints

### Public Endpoints (No Authentication Required)
- `GET /ffaAPI/public/countries` - Get all countries
- `GET /ffaAPI/public/countries/{id}` - Get country by ID
- `GET /ffaAPI/public/countries/search` - Search countries
- `GET /ffaAPI/public/embassies` - Get all embassies
- `GET /ffaAPI/public/projects` - Get available projects

### Authentication Endpoints
- `POST /ffaAPI/auth/login` - User login
- `POST /ffaAPI/auth/register` - User registration
- `POST /ffaAPI/auth/forgot-password` - Request password reset
- `POST /ffaAPI/auth/reset-password` - Reset password

### Protected Endpoints (Authentication Required)
- Application management endpoints
- User profile endpoints
- Admin endpoints

## Project Structure

```
FFA/
├── src/
│   ├── main/
│   │   ├── java/com/isep/ffa/
│   │   │   ├── config/          # Configuration classes
│   │   │   ├── controller/      # REST controllers
│   │   │   ├── dto/             # Data Transfer Objects
│   │   │   ├── entity/          # Entity classes
│   │   │   ├── mapper/          # MyBatis-Plus mappers
│   │   │   ├── repository/      # JPA repositories
│   │   │   ├── security/        # Security configuration
│   │   │   ├── service/         # Service layer
│   │   │   └── util/            # Utility classes
│   │   └── resources/
│   │       ├── application.yaml # Application configuration
│   │       └── db/migration/    # Database migration scripts
│   └── test/                    # Test classes
├── pom.xml                      # Maven configuration
└── README.md                    # This file
```

## Development

### Code Style
- Follow Java naming conventions
- Use English for all comments and documentation
- Write meaningful commit messages

### Database Changes
- Always create migration scripts for schema changes
- Test migrations on development database first
- Follow existing naming conventions

### Security
- Never commit credentials or sensitive information
- Use environment variables for configuration
- Keep dependencies updated for security patches

## Testing

Run all tests:
```bash
mvn test
```

Run with coverage:
```bash
mvn test jacoco:report
```

## Deployment

### Production Environment
1. Set up PostgreSQL database
2. Configure environment variables
3. Update `application.yaml` with production settings
4. Build production JAR:
   ```bash
   mvn clean package -Pprod
   ```
5. Run the application:
   ```bash
   java -jar target/ffa-0.0.1-SNAPSHOT.jar
   ```

## Contributing

1. Create a feature branch
2. Make your changes
3. Write tests for new features
4. Submit a pull request

## License

This project is licensed under the MIT License.

## Contact

For questions or support, please contact the development team.

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

## Render 部署

### 快速部署到 Render

项目已针对 Render 平台优化，支持一键部署：

1. **使用 Render Dashboard**（推荐）
   - 登录 https://dashboard.render.com
   - 点击 "New +" → "Web Service"
   - 连接 Git 仓库
   - Render 会自动检测 `Dockerfile` 并开始部署

2. **配置环境变量**（可选）
   - 在 Render Dashboard 的 Environment 标签页中配置
   - 如果使用 Render PostgreSQL，数据库连接会自动注入

### Render 优化特性

- ✅ 自动检测 Dockerfile
- ✅ 自动注入 PORT 环境变量
- ✅ 内存优化（适合免费版 512MB）
- ✅ AOT 优化启用（更快启动）
- ✅ 健康检查配置
- ✅ 自动数据库迁移（Flyway）

## Docker 构建

项目使用 GraalVM Native Image 构建，生成原生可执行文件：

```bash
# 构建 Native Image
docker build -t ffa-platform .
```

**特点**:
- 启动时间: < 100ms（毫秒级）
- 内存占用: ~50-100MB
- 构建时间: 10-15 分钟（首次构建较慢）
- 适用场景: 生产环境（追求极致性能）

**注意**: Native Image 构建需要更多时间和资源，但运行时性能极佳，非常适合 Render 等云平台。

### 运行容器

```bash
# 运行 Native Image 镜像
docker run -p 8080:8080 ffa-platform
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

### Docker Deployment

使用 Docker 部署（推荐）：

```bash
# 构建镜像
docker build -t ffa-platform:latest .

# 运行容器
docker run -d -p 8080:8080 --name ffa-platform ffa-platform:latest
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

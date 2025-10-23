# FFA Platform - Backend API

## 项目概述
FFA Platform是一个大使馆合作项目管理系统，提供项目发布、申请和管理功能。

## 技术栈
- **Java 17**
- **Spring Boot 3.5.6**
- **Spring Data JPA**
- **MyBatis-Plus 3.5.4.1**
- **Spring Security**
- **PostgreSQL**
- **Swagger/OpenAPI 3**
- **JWT Authentication**
- **MapStruct**
- **Lombok**

## 项目结构
```
src/main/java/com/isep/ffa/
├── config/                 # 配置类
│   ├── SwaggerConfig.java
│   └── DatabaseConfig.java
├── controller/             # REST控制器
├── service/               # 业务逻辑层
├── repository/            # 数据访问层
├── mapper/                # MyBatis-Plus Mapper接口
├── entity/                # JPA实体类
├── dto/                   # 数据传输对象
│   ├── request/           # 请求DTO
│   └── response/          # 响应DTO
├── mapper/                # MapStruct映射器
├── security/              # 安全配置
├── exception/             # 异常处理
└── util/                  # 工具类
```

## 数据库配置
- **URL**: jdbc:postgresql://localhost:5432/testdb
- **用户名**: admin
- **密码**: 123456

## API文档
启动应用后访问：
- **Swagger UI**: http://localhost:8080/ffaAPI/swagger-ui.html
- **API Docs**: http://localhost:8080/ffaAPI/api-docs

## 开发规范

### API路径规范
- 管理员API: `/ffaAPI/admin/`
- 干预者API: `/ffaAPI/intervener/`
- 用户API: `/ffaAPI/user/`
- 认证API: `/ffaAPI/auth/`

### 命名规范
- 使用camelCase命名
- API方法名与类图方法名一致
- 实体类使用PascalCase
- 字段使用camelCase

## 启动说明
1. 确保PostgreSQL数据库运行在localhost:5432
2. 创建数据库`testdb`
3. 运行应用：`mvn spring-boot:run`
4. 访问Swagger UI查看API文档

## MyBatis-Plus特性
- **代码生成器**: 自动生成Entity、Mapper、Service、Controller
- **分页插件**: 内置分页功能，支持多种数据库
- **乐观锁**: 内置乐观锁插件
- **逻辑删除**: 支持逻辑删除功能
- **自动填充**: 自动填充创建时间、修改时间等字段
- **条件构造器**: 强大的查询条件构造器
- **性能分析**: SQL性能分析插件

## 代码生成器使用
运行 `CodeGenerator.java` 可以自动生成代码：
```java
// 生成指定表的代码
CodeGenerator.main(new String[]{"person", "country", "embassy"});

// 生成所有表的代码
CodeGenerator.main(new String[]{"all"});
```

## 开发任务
- [x] 项目基础设置
- [x] Swagger集成
- [x] 数据库配置
- [x] MyBatis-Plus集成
- [ ] 实体类创建
- [ ] Mapper接口
- [ ] Service层
- [ ] Controller层
- [ ] 安全认证
- [ ] 测试

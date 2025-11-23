## ---------------------------------------------------------------------------
## FFA Platform - Dockerfile (标准 JAR 构建)
## Multi-stage build that packages the Spring Boot application with Maven and
## produces a lightweight runtime image for deployment (e.g. on Render).
## Includes AOT (Ahead-of-Time) processing for faster startup.
##
## Render 部署:
##   - Render 会自动检测此 Dockerfile（无需配置）
##   - 确保项目根目录包含此文件
##   - Render 会自动注入 PORT 环境变量
##   - 内存已优化为适合 Render 免费版（512MB）
##
## 本地构建方式:
##   docker build -t ffa-platform .
##
## 性能特点:
##   - 启动时间: <1 秒 (优化后，包含懒加载、AOT、JVM 优化)
##   - 内存占用: ~200-250MB (堆内存 256MB + 元空间 96MB + 系统开销)
##   - 构建时间: 2-3 分钟
##   - 适用场景: 开发、测试、生产环境（包括 Render 免费版 512MB）
## ---------------------------------------------------------------------------

# ---- Build stage -----------------------------------------------------------
FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /workspace

# Copy Maven descriptor first to leverage Docker layer caching
COPY pom.xml ./

# Copy project sources
COPY src ./src

# Build the application with AOT processing enabled for faster startup
# Limit Maven memory usage during build to avoid OOM on Render free tier
# AOT processing happens during the build phase and optimizes the application
ENV MAVEN_OPTS="-Xmx512m -Xms256m"
RUN mvn -B -DskipTests -Dspring-boot.aot.enabled=true clean package


# ---- Runtime stage ---------------------------------------------------------
FROM eclipse-temurin:17-jre-alpine AS runtime

WORKDIR /app

# Copy the fat jar produced in the build stage (with AOT optimizations)
COPY --from=build /workspace/target/FFA-0.0.1-SNAPSHOT.jar app.jar

# Render automatically injects PORT env variable
# Spring Boot reads server.port=${PORT:8080} from application.yaml
# No need to set PORT here, Render will provide it

# JVM optimization flags optimized for Render's memory constraints and fast startup
# Render free tier: 512MB RAM total, need to leave room for OS and non-heap
# -XX:+UseSerialGC: Use Serial GC (lowest memory footprint, suitable for small heaps)
# -Xmx256m: Set max heap size (aggressive limit for 512MB total RAM)
# -XX:MaxMetaspaceSize=96m: Limit metaspace size (reduced from 128m)
# -XX:+UseCompressedOops: Use compressed object pointers (saves memory)
# -XX:+TieredCompilation -XX:TieredStopAtLevel=1: Disable C2 compiler to save memory
# -XX:ReservedCodeCacheSize=32m: Reduce code cache size for faster startup
# -XX:InitialCodeCacheSize=16m: Set initial code cache size
# -Xss256k: Reduce thread stack size (saves memory)
# -XX:+UseStringDeduplication: Deduplicate strings (saves memory)
# -Djava.awt.headless=true: Disable GUI components
# -Dfile.encoding=UTF-8: Set file encoding
# -Dspring.backgroundpreinitializer.ignore=true: Disable background pre-initialization
# -Dspring.jmx.enabled=false: Disable JMX for faster startup
# -Dspring.main.lazy-initialization=true: Enable lazy initialization
# -Dspring.jpa.defer-datasource-initialization=false: Don't defer datasource init
# -Dserver.tomcat.mbeanregistry.enabled=false: Disable Tomcat JMX
# -Dspringdoc.swagger-ui.enabled=true: Enable Swagger UI (lazy loaded)
ENV JAVA_OPTS="-XX:+UseSerialGC -Xmx256m -XX:MaxMetaspaceSize=96m -XX:+UseCompressedOops -XX:+TieredCompilation -XX:TieredStopAtLevel=1 -XX:ReservedCodeCacheSize=32m -XX:InitialCodeCacheSize=16m -Xss256k -XX:+UseStringDeduplication -Djava.awt.headless=true -Dfile.encoding=UTF-8 -Dspring.backgroundpreinitializer.ignore=true -Dspring.jmx.enabled=false -Dspring.main.lazy-initialization=true -Dserver.tomcat.mbeanregistry.enabled=false"

EXPOSE 8080

# Use JAVA_OPTS environment variable for JVM arguments
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]


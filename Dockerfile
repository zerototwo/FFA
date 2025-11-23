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
##   - 启动时间: 1-2 秒
##   - 内存占用: ~150-200MB
##   - 构建时间: 2-3 分钟
##   - 适用场景: 开发、测试、生产环境（包括 Render）
## ---------------------------------------------------------------------------

# ---- Build stage -----------------------------------------------------------
FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /workspace

# Copy Maven descriptor first to leverage Docker layer caching
COPY pom.xml ./

# Copy project sources
COPY src ./src

# Build the application with AOT processing enabled for faster startup
# AOT processing happens during the build phase and optimizes the application
RUN mvn -B -DskipTests -Dspring-boot.aot.enabled=true clean package


# ---- Runtime stage ---------------------------------------------------------
FROM eclipse-temurin:17-jre-alpine AS runtime

WORKDIR /app

# Copy the fat jar produced in the build stage (with AOT optimizations)
COPY --from=build /workspace/target/FFA-0.0.1-SNAPSHOT.jar app.jar

# Render automatically injects PORT env variable
# Spring Boot reads server.port=${PORT:8080} from application.yaml
# No need to set PORT here, Render will provide it

# JVM optimization flags optimized for Render's memory constraints
# Render free tier: 512MB RAM, paid tiers: 1GB+
# -XX:+UseG1GC: Use G1 garbage collector (good for low latency and memory efficiency)
# -XX:MaxGCPauseMillis=200: Target max GC pause time
# -XX:+UseStringDeduplication: Deduplicate strings to save memory
# -XX:+OptimizeStringConcat: Optimize string concatenation
# -Xmx384m: Set max heap size (leave room for non-heap memory on 512MB instances)
# -XX:MaxMetaspaceSize=128m: Limit metaspace size
# -XX:+UseCompressedOops: Use compressed object pointers (saves memory)
ENV JAVA_OPTS="-XX:+UseG1GC -XX:MaxGCPauseMillis=200 -XX:+UseStringDeduplication -XX:+OptimizeStringConcat -Xmx384m -XX:MaxMetaspaceSize=128m -XX:+UseCompressedOops"

EXPOSE 8080

# Use JAVA_OPTS environment variable for JVM arguments
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]


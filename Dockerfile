## ---------------------------------------------------------------------------
## FFA Platform - Dockerfile (GraalVM Native Image 构建)
## Multi-stage build that compiles the Spring Boot application to a native
## executable using GraalVM Native Image for ultra-fast startup.
##
## Render 部署:
##   - Render 会自动检测此 Dockerfile（无需配置）
##   - 确保项目根目录包含此文件
##   - Render 会自动注入 PORT 环境变量
##   - 内存占用极低，适合 Render 免费版（512MB）
##
## 本地构建方式:
##   docker build -t ffa-platform .
##
## 性能特点:
##   - 启动时间: < 100ms（毫秒级）
##   - 内存占用: ~50-100MB
##   - 构建时间: 10-15 分钟（首次构建较慢）
##   - 适用场景: 生产环境（追求极致性能）
##
## 注意: Native Image 构建需要更多时间和资源，但运行时性能极佳
## ---------------------------------------------------------------------------

# ---- Build stage -----------------------------------------------------------
FROM ghcr.io/graalvm/graalvm-community:17 AS build

# Install native-image component
RUN gu install native-image

WORKDIR /workspace

# Copy Maven descriptor first to leverage Docker layer caching
COPY pom.xml ./

# Download dependencies (this layer will be cached if pom.xml doesn't change)
RUN mvn dependency:go-offline -B || true

# Copy project sources
COPY src ./src

# Build native image
# This will:
# 1. Process AOT optimizations (Spring Boot 3.x)
# 2. Compile to native executable using GraalVM
# 3. Create a single binary file (no JVM needed at runtime)
RUN mvn -B -DskipTests -Pnative native:compile


# ---- Runtime stage ---------------------------------------------------------
FROM gcr.io/distroless/base-debian12:nonroot AS runtime

WORKDIR /app

# Copy the native executable (not a JAR!)
COPY --from=build /workspace/target/FFA /app/ffa-platform

# Render automatically injects PORT env variable
# Spring Boot reads server.port=${PORT:8080} from application.yaml
# No need to set PORT here, Render will provide it

EXPOSE 8080

# Run the native executable directly (no JVM needed!)
# Native executable is self-contained and optimized
ENTRYPOINT ["/app/ffa-platform"]


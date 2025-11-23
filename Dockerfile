## ---------------------------------------------------------------------------
## FFA Platform - Dockerfile
## Multi-stage build that packages the Spring Boot application with Maven and
## produces a lightweight runtime image for deployment (e.g. on Render).
## Includes AOT (Ahead-of-Time) processing for faster startup.
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

# Render injects PORT env variable; Spring Boot reads server.port=${PORT:8080}
ENV PORT=8080

# JVM optimization flags for faster startup and better performance
# -XX:+UseG1GC: Use G1 garbage collector (good for low latency)
# -XX:MaxGCPauseMillis=200: Target max GC pause time
# -XX:+UseStringDeduplication: Deduplicate strings to save memory
# -XX:+OptimizeStringConcat: Optimize string concatenation
# -Xmx512m: Set max heap size (adjust based on your needs)
ENV JAVA_OPTS="-XX:+UseG1GC -XX:MaxGCPauseMillis=200 -XX:+UseStringDeduplication -XX:+OptimizeStringConcat -Xmx512m"

EXPOSE 8080

# Use JAVA_OPTS environment variable for JVM arguments
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]


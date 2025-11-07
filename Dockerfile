## ---------------------------------------------------------------------------
## FFA Platform - Dockerfile
## Multi-stage build that packages the Spring Boot application with Maven and
## produces a lightweight runtime image for deployment (e.g. on Render).
## ---------------------------------------------------------------------------

# ---- Build stage -----------------------------------------------------------
FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /workspace

# Copy Maven descriptor first to leverage Docker layer caching
COPY pom.xml ./

# Copy project sources
COPY src ./src

# Build the application (skip tests for faster CI/CD builds)
RUN mvn -B -DskipTests clean package


# ---- Runtime stage ---------------------------------------------------------
FROM eclipse-temurin:17-jre-alpine AS runtime

WORKDIR /app

# Copy the fat jar produced in the build stage
COPY --from=build /workspace/target/FFA-0.0.1-SNAPSHOT.jar app.jar

# Render injects PORT env variable; Spring Boot reads server.port=${PORT:8080}
ENV PORT=8080

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]


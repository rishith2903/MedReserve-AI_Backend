# ---------- Build Stage ----------
FROM maven:3.9.6-eclipse-temurin-17 AS build

# Set working directory
WORKDIR /app

# Copy only pom.xml first to cache dependencies
COPY pom.xml .

# Copy source code
COPY src ./src

# Build the Spring Boot application, skip tests
RUN mvn clean package -DskipTests


# ---------- Runtime Stage ----------
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Create non-root user for better security
RUN groupadd -r medreserve && useradd -r -g medreserve medreserve

# Install curl for health checks
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# Copy built JAR from the build stage
COPY --from=build /app/target/medreserve-backend-*.jar app.jar

# Create necessary folders and set permissions
RUN mkdir -p uploads logs && chown -R medreserve:medreserve /app

# Switch to non-root user
USER medreserve

# Expose the application port
EXPOSE 8080

# Health check to validate container is alive
HEALTHCHECK CMD curl -f http://localhost:8080/api/actuator/health || exit 1

# Run the Spring Boot app with support for Render's dynamic port
ENTRYPOINT ["sh", "-c", "java -jar app.jar --server.port=${PORT:-8080}"]

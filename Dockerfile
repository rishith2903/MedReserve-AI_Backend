# Multi-stage build for Spring Boot application
FROM maven:3.9.11-openjdk-17-slim AS build

# Set working directory
WORKDIR /app

# Copy pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code and build application
COPY src ./src
RUN mvn clean package -DskipTests

# Runtime stage
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Create non-root user for security
RUN groupadd -r medreserve && useradd -r -g medreserve medreserve

# Copy the built JAR from build stage
COPY --from=build /app/target/medreserve-backend-*.jar app.jar

# Create uploads directory
RUN mkdir -p uploads logs && chown -R medreserve:medreserve /app

# Switch to non-root user
USER medreserve

# Expose port
EXPOSE 8080

# Install curl for health checks
USER root
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*
USER medreserve

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD curl -f http://localhost:${PORT:-8080}/api/actuator/health || exit 1

# Run the application with dynamic port
ENTRYPOINT ["sh", "-c", "java -jar app.jar --server.port=${PORT:-8080}"]

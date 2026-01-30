# Multi-stage build for AcmePlex Movie Theater Application

# Stage 1: Build Frontend
FROM node:20-alpine AS frontend-builder

WORKDIR /app/frontend

# Copy frontend package files
COPY frontend/package*.json ./

# Install dependencies
RUN npm ci

# Copy frontend source
COPY frontend/ ./

# Build frontend (outputs to ../src/main/resources/static)
RUN npm run build

# Stage 2: Build Backend
FROM eclipse-temurin:17-jdk AS builder

WORKDIR /app

# Copy gradle wrapper and build files
COPY gradlew .
COPY gradle gradle
COPY build.gradle settings.gradle ./

# Make gradlew executable
RUN chmod +x gradlew

# Download dependencies (cached if build.gradle unchanged)
RUN ./gradlew dependencies --no-daemon || true

# Copy source code
COPY src ./src

# Copy config files (checkstyle, etc.)
COPY config ./config

# Copy built frontend assets from frontend-builder
COPY --from=frontend-builder /app/src/main/resources/static ./src/main/resources/static

# Build the application
RUN ./gradlew build -x test --no-daemon

# Stage 3: Run
FROM eclipse-temurin:17-jre

WORKDIR /app

# Create non-root user for security
RUN groupadd -r appgroup && useradd -r -g appgroup appuser

# Copy the built artifact from builder stage
COPY --from=builder /app/build/libs/*.jar app.jar

# Change ownership to non-root user
RUN chown appuser:appgroup app.jar

# Switch to non-root user
USER appuser

# Expose the application port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=30s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]

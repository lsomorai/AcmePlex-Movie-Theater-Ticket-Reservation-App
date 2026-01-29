# Multi-stage build for AcmePlex Movie Theater Application

# Stage 1: Build
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

# Build the application
RUN ./gradlew build -x test --no-daemon

# Stage 2: Run
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

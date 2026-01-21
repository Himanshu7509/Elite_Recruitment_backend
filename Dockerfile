# Multi-stage build for Spring Boot application

# Stage 1: Build the application
FROM eclipse-temurin:17-jdk-alpine AS builder

# Install Maven
RUN apk add --no-cache maven

# Set working directory
WORKDIR /app

# Copy Maven files
COPY pom.xml .

# Download dependencies
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Stage 2: Create the runtime image
FROM eclipse-temurin:17-jre-alpine

# Set working directory
WORKDIR /app

# Copy the jar file from the builder stage
COPY --from=builder /app/target/aptitude-0.0.1-SNAPSHOT.jar app.jar

# Expose port
EXPOSE 8080

# Environment variables (can be overridden at runtime)
ENV MONGODB_URI=""

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
# Use Java 17 (Spring Boot 3 requires Java 17+)
FROM eclipse-temurin:17-jdk-alpine

# Create working directory
WORKDIR /app

# Copy Maven wrapper + pom.xml first
COPY .mvn/ .mvn
COPY mvnw .
COPY pom.xml .

# Make Maven wrapper executable
RUN chmod +x mvnw

# Download dependencies (cached layer)
RUN ./mvnw dependency:go-offline

# Copy project source
COPY src ./src

# Build Spring Boot JAR (skip tests)
RUN ./mvnw package -DskipTests

# Expose port for Render
EXPOSE 8080

# Run the Spring Boot app
CMD ["java", "-jar", "target/smartspend-0.0.1-SNAPSHOT.jar"]

# Use Java 17 (Spring Boot 3 requires Java 17+)
FROM eclipse-temurin:17-jdk-alpine

# Create working directory
WORKDIR /app

# Copy Maven wrapper + pom.xml
COPY .mvn/ .mvn
COPY mvnw .
COPY pom.xml .

RUN chmod +x mvnw

# Pre-download dependencies
RUN ./mvnw dependency:go-offline

# Copy all source
COPY src ./src

# Build JAR
RUN ./mvnw -DskipTests package

# Expose port (Render will override)
EXPOSE 8080

# Run the application (correct JAR path)
CMD ["java", "-jar", "/app/target/smartspend-0.0.1-SNAPSHOT.jar"]

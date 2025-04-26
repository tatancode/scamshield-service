FROM eclipse-temurin:21-jdk-jammy as builder

# Set the working directory
WORKDIR /app

# Copy the Maven wrapper and pom.xml
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Download dependencies (this layer is cached unless pom.xml changes)
# Using go-offline to download all dependencies first
RUN ./mvnw dependency:go-offline -B

# Copy the source code
COPY src ./src

# Package the application using Maven wrapper, skipping tests for faster builds
RUN ./mvnw package -DskipTests -B

# --- Second Stage: Create the actual runtime image ---

# Use a smaller JRE image for the final container
FROM eclipse-temurin:21-jre-jammy

# Set the working directory
WORKDIR /app

# Copy the packaged JAR file from the builder stage
# Make sure the artifactId in pom.xml matches the jar name below
COPY --from=builder /app/target/scamshield-service-*.jar app.jar

# Expose the port the app runs on (Spring Boot default is 8080)
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]

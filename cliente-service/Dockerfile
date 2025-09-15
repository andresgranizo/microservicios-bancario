FROM openjdk:17-jdk-slim

WORKDIR /app

# Copy the pre-built JAR file
COPY target/cliente-service-1.0.0.jar app.jar

# Expose port
EXPOSE 8081

# Run application
CMD ["java", "-jar", "app.jar"]
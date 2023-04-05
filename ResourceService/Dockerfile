FROM openjdk:17-oracle

# Set the working directory to /app
WORKDIR /app

# Copy the application JAR file and Docker profile properties to the container
COPY target/resource-service.jar /app

# Expose port 8080
EXPOSE 8080

# Set the active profile to "docker"
ENV SPRING_PROFILES_ACTIVE docker

# Run the application when the container starts
CMD ["java", "-jar", "/app/resource-service.jar"]
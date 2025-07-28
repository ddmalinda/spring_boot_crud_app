# Use a specific Java Runtime Environment (JRE) as a base image.
# It's more secure and smaller than a full JDK.
FROM eclipse-temurin:21-jre-jammy

# Set an argument for the JAR file path
ARG JAR_FILE=target/*.jar

# Set the working directory inside the container
WORKDIR /opt/app

# Copy the JAR file from your build directory into the container's working directory
COPY ./target/backend-0.0.1-SNAPSHOT.jar app.jar

# Expose the port your Spring Boot app runs on (default is 8080)
EXPOSE 8080

# The command to run your application when the container starts
ENTRYPOINT ["java", "-jar", "app.jar"]
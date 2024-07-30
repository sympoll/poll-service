# Use the official Maven image to build the application
FROM maven:3.9.8-amazoncorretto-17 AS build

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the source code and build the application
COPY src src
RUN mvn package -DskipTests

# Use the OpenJDK image to run the application
FROM amazoncorretto:17

# Set the working directory
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/target/*.jar app.jar


# Expose the port the application will run on
EXPOSE 8081

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
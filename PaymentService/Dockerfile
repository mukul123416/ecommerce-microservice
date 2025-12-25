# Step 1: Use an official Java runtime as a parent image
FROM eclipse-temurin:17-jdk-jammy

# Step 2: Set the working directory inside the container
WORKDIR /app

# Step 3: Copy the compiled JAR file from the host machine to the container
# Note: Ensure you run 'mvn clean package' before building the image
COPY target/*.jar PaymentService-0.0.1-SNAPSHOT.jar

# Step 4: Command to execute the application
ENTRYPOINT ["java", "-jar", "PaymentService-0.0.1-SNAPSHOT.jar"]
# Setup maven environment
FROM maven:3.8.2-openjdk-17 as build
COPY . /app
WORKDIR /app
RUN mvn clean compile package -Dmaven.test.skip

# Build application
FROM eclipse-temurin:17-jdk-jammy as runtime
WORKDIR /app
COPY --from=build /app/target/*.jar /app/
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]

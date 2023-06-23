#Build maven environment

FROM maven:3.9.0-eclipse-temurin-17 as build
COPY . /app
WORKDIR /app
RUN mvn package -Dmaven.test.skip

#Build application
FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar /app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/ebanking-api.jar"]

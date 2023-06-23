FROM openjdk:8-alpine
EXPOSE 8080
ADD target/ebanking-api.jar ebanking-api.jar
ENTRYPOINT ["java", "-jar", "/ebanking-api.jar"]
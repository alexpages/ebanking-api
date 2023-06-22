FROM openjdk:8-alpine
ADD target/my-fat.jar /usr/share/app.jar
COPY ./target/ebanking-api-v1.jar app.jar
ENTRYPOINT ["/usr/bin/java", "-jar", "/usr/share/app.jar"]
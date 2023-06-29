# Setup maven environment
FROM maven:3.8.2-openjdk-17 as build
COPY . /app
WORKDIR /app
RUN mvn clean package -Dmaven.test.skip

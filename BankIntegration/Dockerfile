FROM maven:3.8.6-openjdk-17 AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

FROM openjdk:17-jre-slim

WORKDIR /app

COPY --from=build /app/target/Ramazanov-0.0.1-SNAPSHOT.jar app.jar

ENV DB_PASSWORD=""
ENV TWELVEDATA_API_KEY=""

EXPOSE 8080

ENTRYPOINT ["java", "-Dspring.config.additional-location=./secrets/", "-jar", "app.jar"]
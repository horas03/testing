
FROM openjdk:21-jdk-slim


WORKDIR /app


COPY target/crypto-recommendation-service-0.0.1-SNAPSHOT.jar app.jar

COPY src/main/resources /app/resources

EXPOSE 8081


ENV SPRING_PROFILES_ACTIVE=docker


ENTRYPOINT ["java", "-jar", "app.jar"]

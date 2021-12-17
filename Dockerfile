FROM openjdk:11-jdk-slim
LABEL version="1.0"

WORKDIR /app

COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline

COPY src ./src

EXPOSE 8080
CMD ["./mvnw", "spring-boot:run", "-Dspring-boot.run.profiles=docker"]
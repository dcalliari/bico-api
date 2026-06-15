FROM maven:3.9-eclipse-temurin-21-alpine AS build

WORKDIR /app

COPY pom.xml mvnw ./
COPY .mvn .mvn
RUN chmod +x mvnw && ./mvnw dependency:go-offline -B

COPY src ./src
RUN ./mvnw package -DskipTests -B

FROM eclipse-temurin:21-jre-alpine AS production

WORKDIR /app

RUN addgroup -S bicos && adduser -S bicos -G bicos

COPY --from=build /app/target/*.jar app.jar
RUN chown bicos:bicos app.jar

USER bicos

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]

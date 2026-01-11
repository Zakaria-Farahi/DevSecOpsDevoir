FROM maven:3.9.9-eclipse-temurin-21-alpine AS build
WORKDIR /workspace
COPY pom.xml .
COPY src ./src
RUN mvn -q -DskipTests package

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
RUN useradd -r -u 10001 appuser
COPY --from=build /workspace/target/*.jar /app/app.jar
USER appuser
EXPOSE 8082
ENTRYPOINT ["java","-jar","/app/app.jar"]

FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /workspace
COPY pom.xml mvnw* ./

COPY . .
RUN mvn -B clean package -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /workspace/target/*.jar /app/api-reservas-mesas.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/api-reservas-mesas.jar"]

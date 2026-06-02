FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app

COPY . .

RUN POM_PATH=$(find . -name pom.xml | head -n 1) && \
    echo "Found pom.xml at: $POM_PATH" && \
    mvn -f "$POM_PATH" clean package -DskipTests && \
    cp $(dirname "$POM_PATH")/target/*.jar /app/app.jar

FROM eclipse-temurin:17-jdk
WORKDIR /app

COPY --from=build /app/app.jar app.jar

EXPOSE 10000

ENTRYPOINT ["sh", "-c", "java -jar app.jar --server.port=${PORT:-10000}"]

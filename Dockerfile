# --- ステージ1: ビルド環境 ---
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app

# すべてのファイルをコピー
COPY . .

# pom.xmlを自動で探し、その場所でビルドを実行して、完成品(JAR)を /app/app.jar に集める
RUN POM_PATH=$(find . -name pom.xml | head -n 1) && \
    echo "Found pom.xml at: $POM_PATH" && \
    mvn -f "$POM_PATH" clean package -DskipTests && \
    cp $(dirname "$POM_PATH")/target/*.jar /app/app.jar

# --- ステージ2: 実行環境 ---
FROM eclipse-temurin:17-jdk
WORKDIR /app

# ステージ1で集めた app.jar をコピー
COPY --from=build /app/app.jar app.jar

EXPOSE 10000

# ポート10000で実行
ENTRYPOINT ["java", "-jar", "app.jar", "--server.port=10000"]

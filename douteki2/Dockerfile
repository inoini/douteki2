# --- ステージ1: ビルド環境 (Maven) ---
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
# ソースコードをコピー
COPY . .
# MavenでJARファイルをビルド (テストはスキップ)
RUN mvn clean package -DskipTests

# --- ステージ2: 実行環境 ---
FROM eclipse-temurin:17-jdk
WORKDIR /app
# ステージ1で作られたJARファイルだけをコピー
COPY --from=build /app/target/*.jar app.jar

# Renderのポートに合わせる（推奨10000）
EXPOSE 10000

# ポート番号をRenderから受け取れるようにしつつ実行
ENTRYPOINT ["java", "-jar", "app.jar", "--server.port=10000"]


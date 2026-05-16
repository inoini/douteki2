# --- ステージ1: ビルド環境 ---
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app

# すべてのファイルをコピー
COPY . .

# pom.xml がある場所（douteki2/douteki2）を指定してビルドを実行
RUN mvn -f douteki2/douteki2/pom.xml clean package -DskipTests

# --- ステージ2: 実行環境 ---
FROM eclipse-temurin:17-jdk
WORKDIR /app

# 作成されたJARファイルをコピー（ここも深い階層から取得）
COPY --from=build /app/douteki2/douteki2/target/*.jar app.jar

EXPOSE 10000

# ポート10000で実行
ENTRYPOINT ["java", "-jar", "app.jar", "--server.port=10000"]

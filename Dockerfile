FROM gradle:8.12.1-jdk17 AS build
WORKDIR /app

COPY . .

RUN gradle clean --no-daemon
ENV GRADLE_OPTS="--add-opens java.base/java.lang=ALL-UNNAMED"
RUN gradle build -x test --no-daemon -Dorg.gradle.jvmargs="$GRADLE_OPTS"

FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Копируем JAR-файл приложения
COPY --from=build /app/build/libs/*.jar app.jar

ENV JVM_ARGS="--add-opens java.base/java.lang=ALL-UNNAMED"
ENTRYPOINT java $JVM_ARGS -jar app.jar

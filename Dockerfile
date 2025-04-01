FROM gradle:8.12.1-jdk17 AS build
WORKDIR /app
COPY gradlew gradlew
COPY gradle gradle
RUN chmod +x gradlew

COPY . .

# Не провожу тесты, потому что посгрес не поднимается и тесты на БД падают - проект не собирается
RUN gradle build --no-daemon -x test

FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
# Для того чтоб запустить тесты градлом внутри.
COPY --from=build /app/gradlew gradlew
COPY --from=build /app/gradle gradle
COPY --from=build /app/gradle/wrapper gradle/wrapper

RUN chmod +x gradlew
CMD ["java", "-jar", "app.jar"]

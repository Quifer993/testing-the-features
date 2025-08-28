FROM ubuntu:latest
LABEL authors="quifer"

ENTRYPOINT ["top", "-b"]

# Используем образ BellSoft Liberica JDK 8 (slim для минимального размера)
FROM bellsoft/liberica-openjdk-debian:8

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем jar-файл
COPY target/quartz-test-1.0-SNAPSHOT.jar app.jar
COPY target/classes/application-docker.yaml /app/config/application-docker.yaml

CMD ["java", "-jar", "app.jar", "--spring.config.additional-location=config/application-docker.yaml"]
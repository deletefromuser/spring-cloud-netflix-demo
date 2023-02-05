# https://hub.docker.com/_/eclipse-temurin
FROM maven:3.8.7-eclipse-temurin-11-focal
COPY . .
# RUN mvn spring-boot:run
CMD ["mvn", "spring-boot:run", "-Dspring-boot.run.profiles=docker"]
# ENTRYPOINT ["mvn", "spring-boot:run", "-Dspring-boot.run.profiles=docker"]

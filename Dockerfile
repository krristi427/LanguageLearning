FROM openjdk:17-alpine

# the port on which spark sets up the server
EXPOSE 4567

RUN mkdir /app

COPY /build/libs/LanguageLearning-1.0-SNAPSHOT-all.jar /app/LanguageLearning-1.0-SNAPSHOT-all.jar

ENTRYPOINT ["java", "-jar", "/app/LanguageLearning-1.0-SNAPSHOT-all.jar"]
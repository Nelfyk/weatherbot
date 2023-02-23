FROM openjdk:17-jdk-alpine
MAINTAINER Burduzhan Ruslan
VOLUME /main-app
ADD target/weatherbot-0.0.1-SNAPSHOT.jar weatherbot.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","weatherbot.jar"]
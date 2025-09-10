FROM openjdk:21-jdk-slim
ARG JAR_FILE=classes/build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
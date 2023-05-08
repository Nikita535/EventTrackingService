FROM openjdk:17-alpine
VOLUME /tmp
ARG JAR_FILE
COPY ${JAR_FILE} lab-0.0.1-SNAPSHOT-plain.jar
ENTRYPOINT ["java","-jar","/app.jar"]
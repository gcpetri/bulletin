FROM adoptopenjdk:11-jre-hotspot
ARG JAR_FILE=target/*.jar
ARG CONFIG_FILE=config.properties
COPY ${JAR_FILE} app.jar
COPY ${CONFIG_FILE} application.properties
ENTRYPOINT [ "java", "-jar", "app.jar"]

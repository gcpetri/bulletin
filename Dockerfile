FROM adoptopenjdk:11-jre-hotspot

#RUN --mount=type=cache,target=/root/.m2 ./mvnw clean install -DskipTests

ARG JAR_FILE=target/*.jar
ARG CONFIG_FILE=config.properties
COPY ${JAR_FILE} app.jar
COPY ${CONFIG_FILE} application.properties
ENTRYPOINT [ "java", "-jar", "app.jar"]
#RUN java -Djarmode=layertools -jar app.jar extract

#COPY --from=bulid dependencies/ .
#COPY --from=bulid spring-boot-loader/ .
#COPY --from=bulid snapshot-dependencies/ .
#COPY --from=bulid application/ .
#ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]

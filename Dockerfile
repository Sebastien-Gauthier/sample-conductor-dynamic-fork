FROM eclipse-temurin:21-jre-ubi9-minimal

RUN mkdir /opt/app
COPY target/java-worker-1.0-SNAPSHOT.jar /opt/app

EXPOSE 8080

CMD ["java", "-jar", "/opt/app/sample-dynamic-fork-1.0-SNAPSHOT.jar"]

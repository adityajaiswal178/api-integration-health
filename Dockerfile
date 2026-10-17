FROM eclipse-temurin:17-jre
COPY target/api-integration-health-1.0.0.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]

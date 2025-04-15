FROM eclipse-temurin:17-jdk-alpine
COPY target/${artifactId}-${version}-${classifier}.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]

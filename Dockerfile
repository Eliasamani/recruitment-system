FROM eclipse-temurin:21-jdk-noble





ARG JAR_FILE=recruitment-backend/target/*.jar
COPY ${JAR_FILE} app.jar



ENTRYPOINT [ "java", "-jar", "/app.jar" ]

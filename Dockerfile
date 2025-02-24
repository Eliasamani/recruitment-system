FROM eclipse-temurin:21-jre-noble




ARG JAR_FILE=recruitment-backend/target/*.jar

COPY ${JAR_FILE} app.jar


CMD export SPRING_PORT=$PORT; java -jar /app.jar

FROM eclipse-temurin:21-jre-noble



ENV RECR_EMAIL_HOST = ${RECR_EMAIL_HOST}
ENV RECR_EMAIL_PORT = ${RECR_EMAIL_PORT}
ENV RECR_EMAIL = ${RECR_EMAIL}
ENV RECR_EMAIL_PWORD = ${RECR_EMAIL_PWORD}

ARG JAR_FILE=recruitment-backend/target/*.jar

COPY ${JAR_FILE} app.jar


CMD export SPRING_PORT=$PORT; java -jar /app.jar

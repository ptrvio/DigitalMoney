FROM eclipse-temurin:17
ARG JAR_FILE=target/cards-service.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","app.jar"]
EXPOSE 8085
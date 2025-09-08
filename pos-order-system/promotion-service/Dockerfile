FROM eclipse-temurin:17-jre
WORKDIR /app
ARG JAR_FILE=target/promotion-service-1.0.0-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8093
ENTRYPOINT ["java","-XX:+UseZGC","-Xms256m","-Xmx512m","-jar","/app/app.jar"]
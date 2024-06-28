FROM eclipse-temurin:21-alpine

RUN apk add --no-cache chromium
RUN apk add --no-cache chromium-chromedriver

RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

ARG JAR_FILE=build/libs/\*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
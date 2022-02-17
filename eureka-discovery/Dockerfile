FROM openjdk:17-alpine
WORKDIR app/
COPY target/eureka-discovery-1.0.jar app.jar
EXPOSE 8761
ENTRYPOINT ["java", "-jar", "app.jar"]

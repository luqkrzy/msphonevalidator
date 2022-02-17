FROM openjdk:17-alpine
WORKDIR app/
COPY target/balancer-1.0.jar app.jar
EXPOSE 9000
ENTRYPOINT ["java", "-jar", "app.jar"]

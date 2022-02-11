FROM openjdk:17-alpine
WORKDIR app/
RUN apk add tesseract-ocr
ENV TESSDATA_PREFIX=/app/tessdata
RUN mkdir -p "/app/tessdata"
COPY /tessdata ./tessdata
COPY target/ocr-parser-1.0-SNAPSHOT.jar app.jar
EXPOSE 8000
ENTRYPOINT ["java", "-jar", "app.jar"]

FROM openjdk:17-alpine
EXPOSE 9000
WORKDIR app/
RUN apk add tesseract-ocr
ENV TESSDATA_PREFIX=/app/tessdata
RUN mkdir -p "/app/tmp"
RUN mkdir -p "/app/tessdata"
COPY /tessdata ./tessdata
COPY ocr-parser/target/ocr-parser-1.0-SNAPSHOT.jar app.jar
RUN ls -la
RUN pwd
ENTRYPOINT ["java", "-jar", "app.jar"]

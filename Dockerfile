FROM alpine/git as clone (1)
WORKDIR /app
RUN git clone https://gitlab.com/ashutoshRai/imgur-image-uploader.git

FROM maven:3.6-jdk-8-alpine as build (2)
WORKDIR /app
COPY --from=clone /app/imgur-image-uploader /app (3)
RUN mvn install

FROM openjdk:8-jdk-alpine
WORKDIR /app
COPY --from=build /app/target/ImgurUploader-1.0-SNAPSHOT.jar /app
EXPOSE 8080
RUN sh -c 'touch ImgurUploader-1.0-SNAPSHOT.jar'
ENTRYPOINT ["java","-jar","ImgurUploader-1.0-SNAPSHOT.jar"]

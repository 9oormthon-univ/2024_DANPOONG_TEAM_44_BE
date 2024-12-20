FROM openjdk:21

WORKDIR /app

COPY ./build/libs/ZipCock_44-0.0.1-SNAPSHOT.jar /app

EXPOSE 8080

CMD ["java", "-jar", "/app/ZipCock_44-0.0.1-SNAPSHOT.jar"]

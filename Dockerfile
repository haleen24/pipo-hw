FROM eclipse-temurin:21-jdk-jammy

WORKDIR /app

COPY inventory-service/build/libs/inventory-service-0.1.jar app.jar

ENTRYPOINT ["java","-jar","/app/app.jar"]

EXPOSE 8080
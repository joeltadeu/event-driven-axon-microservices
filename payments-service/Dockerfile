FROM openjdk:16-alpine3.13
ADD target/payments-service.jar payments-service.jar
EXPOSE 7501
ENTRYPOINT ["java", "-jar", "/payments-service.jar"]

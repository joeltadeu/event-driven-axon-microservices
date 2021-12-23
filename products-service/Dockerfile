FROM openjdk:16-alpine3.13
ADD target/products-service.jar products-service.jar
EXPOSE 7502
ENTRYPOINT ["java", "-jar", "/products-service.jar"]

FROM openjdk:16-alpine3.13
ADD target/users-service.jar users-service.jar
EXPOSE 7503
ENTRYPOINT ["java", "-jar", "/users-service.jar"]

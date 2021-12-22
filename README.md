# Order a product using Spring CLoud Axon Framework, CQRS, Event Sourcing, SAGA, Transactions

Learn to build distributed Event-driven Microservices, CQRS, Event Sourcing, SAGA, Transactions

## There are four microservices:

- **Products** : This microservice is responsible for managing products. A user can create, update, get details, list and delete products.
- **Orders** : This microservice is responsible for managing orders. A user can order many products.
- **Payment** : This microservice is responsible for managing payments. Save details about the payment associated with the order.
- **Users** : This microservice is responsible for managing users. Save details about the user.

### Concepts used ###
- Event-Driven Microservices
- Basics of Spring Cloud
- Axon Framework
- Eureka Discovery Service
- CQRS Design Pattern
- Spring Cloud API Gateway
- SAGA Design Pattern
- Event Based Messages
- Transactions

### EndPoints ###

| Service  | EndPoint                        | Method | Description                                              |
|----------|---------------------------------|:------:|----------------------------------------------------------|
| Orders   | /orders                         |  POST  | Create an order                                          |
| Products | /products                       |  GET   | Return list of products                                  |
| Products | /products/{id}                  |  GET   | Return details from a specific product                   |
| Products | /products                       |  POST  | Insert a new product                                     |
| Products | /products/{id}                  |  PUT   | Update a specific product                                |
| Products | /products/{id}                  | DELETE | Delete a specific product                                |
| Users    | /users/{userId}/payment-details |  GET   | Return details from a userfor payment based on your ID   |

### Gateways ###

| Service  |               EndPoint                |
|----------|:-------------------------------------:|
| Products |          **/products**/{id}           | 
| Products |             **/products**             |
| Orders   |              **/orders**              |
| Users    |  **/users/**{userId}/payment-details  |

URI for gateway : *http://localhost:8082*

### Documentation and examples ###

###Swagger

- **Products** : http://localhost:8082/products/swagger-ui.html
- **Orders** : http://localhost:8082/orders/swagger-ui.html
- **Users** : http://localhost:8082/users/swagger-ui.html

###Postman collection

![Alt text](assets/postman-collection-folder.png?raw=true "Postman collection folder")
## Used Netflix OSS:

- **Netflix Eureka** is used for discovery service.
- **Netflix Zuul** is used for gateway.

## Distributed Tracing:

- **Sleuth** and **Zipkin**

You can open Zipkin : http://localhost:9411

## Used ELK STACK:

- **ElasticSearch** is on 6972 port
- **Logstash TCP** is on 5000 port
- **Kibana** is on 5601 port

Open kibana with http://localhost:5601/. You must define an index pattern (taner-*) on Management/Stack Management.

## Grafana:

You can open Grafana : http://localhost:3000/

username/password: admin

## Build & Run

- *>mvn clean package* : to build
- *>docker-compose up* --build : build docker images and containers and run containers
- *>docker-compose stop* : stop the dockerized services
- Each maven module has a Dockerfile.

In docker-compose.yml file:

- Books Service : **__7500__** port is mapped to **__7500__** port of host
- Orders Service : **__7501__** port is mapped to **__7501__** port of host
- Params Service : **__7502__** port is mapped to **__7502__** port of host
- Eureka Discovery Service : **__8761__** port is mapped to **__8761__** port of host
- Spring Boot (/ Zuul) Gateway Service : **__8762__** port is mapped to **__8762__** port of host 

## VERSIONS

### 1.0.0

- Sleuth and Zipkin Integration
- ElasticSearch, Kibana, Logstash integration
- Spring Boot Gateway
- Spring-Boot 2.5.7.RELEASE
- Java 16
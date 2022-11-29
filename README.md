# Transactions Microservice
Transactions Microservice using Event Sourcing & CQRS with Spring Boot & Axon Framework

## Download Axon Server
https://download.axoniq.io/quickstart/AxonQuickStart.zip

## Running Axon Server
Unzip AxonQuickStart.zip and execute the following command:
```
$ cd documents/tools/axonquickstart-4.5.11/AxonServer
$ java -jar axonserver-4.5.11.jar
```

## Axon Server URL
http://localhost:8024

## Environment Variables
### MYSQL_CUSTOMERS_AXON_JDBC_URL
```
jdbc:mysql://{YOUR-HOST}:3306/{YOUR_DATABASE}?verifyServerCertificate=false&useSSL=false&useTimezone=true&serverTimezone=UTC
```

## Swagger
http://localhost:8083/v3/api-docs
http://localhost:8083/api-docs/swagger-ui/index.html
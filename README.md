# Agoda Assignment - Gunaalan S - Spring Batch and Multi-Threading # 

The project would give a help in storing large scale of booking data and retrieve their summaries pretty quickly.

## Getting Started ##

Technology stack includes [Spring Boot](https://spring.io/projects/spring-boot) + Spring-Batch + Java + Maven

### Prerequisites ##
- [Java](https://www.oracle.com/technetworkk/java/javase/downloads/jdk8-downloads-2133151.html) >= 8
- [Mysql](https://dev.mysql.com/doc/mysql-installation-excerpt/8.0/en/) 8.0

## Load Data from CSV and Read Data using Multi-Threading ##
- `localhost:8081/bookingmanager/load` - Trigger point for Spring batch to initiate and start running.
  The spring batch fetches all the records from BookingData.csv and batch-wise inserts them in to booking mysql table.
  
```
Curl Command:

curl --location --request GET 'localhost:8081/bookingmanager/load' --data-raw ''
```

- `localhost:8081/bookingmanager/bookingsummary/customer` - Get endpoint to retrieve the booking summaries grouped by customer-id.
The endpoint uses multi-threading to retrieve large data parallely from sql tables and finally aggregates, computes the cost of booking in USD to display the response.

```
Curl Command:

curl --location --request GET 'localhost:8081/bookingmanager/bookingsummary/customer' \
--header 'Content-Type: application/json' \
--data-raw '{
    "customerIds" : ["f57e85f3-b496-49fb-9093-d54db5b9fc6e",
"f9908c8c-bc9f-444e-8c36-e746f9914f73",
"f5837db5-b938-41ef-9807-63e9da4b30e9",
"7b9bdc2b-0904-4e8b-ad42-1d2b4061dd87"]
}'```
  
```
- `localhost:8081/bookingmanager/bookingsummary/hotel/1000336` - Get endpoint to retrieve the booking summaries grouped by hotel-id.
  The endpoint uses multi-threading to retrieve large data parallely from sql tables and finally aggregates, computes the cost of booking in USD to display the response.
  This endpoint also has the provision to feed custom currency exchange rate for all currencies.
  
```
Curl Command:

curl --location --request GET 'localhost:8081/bookingmanager/bookingsummary/hotel/1000336' \
--header 'Content-Type: application/json' \
--data-raw '{
    "SGD" : 2.22,
    "THB" : 4.35
}'
```
## MySQL Config ##
```
spring.datasource.url=jdbc:mysql://localhost:3306/agoda?useSSL=false&createDatabaseIfNotExist=true
spring.datasource.username = root
spring.datasource.password= root
```

## Coding style ##
Follow default Java code style on IntelliJ IDE

## Available APIs
```
GET localhost:8081/bookingmanager/bookingsummary/customer (retrieves data using multi-threading)
GET  localhost:8081/bookingmanager/load (used spring-batch)
GET localhost:8081/bookingmanager/bookingsummary/hotel/1000336 (retrieves data using multi-threading)
```
## A deeper look

### Applied design pattern
- Service pattern
- Repository pattern
- SOLID principles
- DTO - Data transfer object
- MVC
- REST APIs
- Viewer Patter
- Multi-Threading
- Spring-Batch
- ORM

### Algorithm
- Spring Batch: Spring batch has a chain of steps to follow in performing batch operations.
  * File Reader 
  * Job Launcher
  * Job
  * Step
  * Item Reader
  * Item Processor
  * Item Writer
  * Task Queues  
  ![batch processs](https://i.imgur.com/WyxCQFF.png)
- Multi Threading for Reading data: Similar to batching principle, We have implemented our own multi threading concept, that reads from a connection pool to fetch reusable connections and makes separate individual connections and queries for results in separate batches parallely.
- Connection Pool: You can't use only one connection when doing multi threading. SQL doesn't allow that. Hence, we needed a pool, and a strategy to reuse the connections.
- Aggregating & Constructing Summaries: The Summary construction is being done using OOPs and can be reused to generate different kinds of summaries.

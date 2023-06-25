Synpulse8 HK - Backend challange - ebanking-api
-
***
### About
This project is intended to be a resolution proposal for Synpulse8 Junior Java Backend vacancy in Hong Kong. The project complies with the following aspects:
- [x] Spring Boot
- [x] Spring Security 
- [x] JWT authentication
- [x] PostgreSQL for Users and Accounts
- [x] Kafka Producer/consumer, topic management
- [x] REST Controller with open endpoints
- [ ] JUnit
- [x] Logging and monitoring support

To run a working copy of the code, go to [Running the project](#running-the-project)
***
### Pre-Requisites
To run this code it is necessary the following:
- Java v17
- Docker

***
### Architecture

As an overview of the architecutre, it can be seen in the picture below:

<img src="src/main/resources/Architecture.png"/>

_Note: Some configuration files and components have not been included to ensure readability of what is more relevant._
***
### Data related
- PostgreSQL: to store user, accounts and tokens (jwt)
- Kafka: Used Kafka Topics and partitions to retrieve
***
### Security
Security has been implemented with Spring Security and covers the following:
- Roles
- Encoded client password
- JWT for Auth within http request
- General http security configuration
- Other filters (filterchain)

***
### Relevant implementation decisions
- Avoid using Pageable - since Transactions are not saved into a Repository.
- Avoid using Kafka Streams - since it is commonly used for Real-Time Data Ingestion systems.
- N-Tier architecture/Folder architecture - by element function within the project.
- Kafka Topic/Partition distribution
  - It was considered grouping transactions by IBAN.
  - It was considered grouping transactions by KTable and an updatable value as the list of transactions. The intention behind was to perform interactive queries.
  - For optimal retrieval, more topics (year-user combination) and partitions (12 per topic, as in months) was decided.

***
### Running the project
Since the application is wrapped inside a docker container. Just performe docker-compose up operation on the shell [docker-compose.yml](docker-compose.yml). 
It will start a Zookeeper instance, the Kafka broker, PostgreSQL for client related and the Spring Boot app.
```shell
docker compose -f ./docker-compose.yml up
```
### Building and starting the application
```shell
tbd
```

***
### Future improvements
- [x] Implement Custom Serdes for Kafka Streams
- [ ] Kafka Streams and Topology for filtering transactions
- [ ] Kafka Tables for updating the total balance of each user (Key-Value Store) with interactive queries
- [ ] Microservice for JWT generation and renewal

****


Synpulse8 - Backend Engineer Challange - ebanking-api
-
***
### About
This project is a resolution proposal for Synpulse8 Junior Java Backend vacancy in Hong Kong. The project complies with the following aspects:
- [x] Spring Boot
- [x] Spring Security with JWT authentication
- [x] PostgreSQL for saving Users and Accounts
- [x] Kafka Producer/consume for Transactions and topic management
- [x] REST Controllers (Transaction/Authentication) with open endpoints
- [x] Logging and monitoring support
- [x] JUnit
- [x] API modeling (Swagger/OpenAPI) and its documentation
- [x] Completed CI/CD circle with Circle Ci and prepared K8s config with `kompose docker-compose.yml`

To run a working copy of the code, go to [Running the project](#running-the-project).

Additionally:
- CI/CD: [Circle-Ci-Pipeline](https://app.circleci.com/pipelines/github/alexpages?filter=all&status=none&status=success)
- K8s: [K8s Configuration](/Users/alexpages/IdeaProjects/ebanking-api/k8s)

***
### Architecture
This project follows the architecture shown below:

![img.png](src/main/resources/architecture.png)

The application is wrapped with Docker which enables different containers:

![img.png](src/main/resources/docker.png)

_Note: Some configuration files and components have not been included into the image to ensure readability of what is more relevant._


***
### Endpoints
Enabled endpoints for user interaction:

| Endpoint                          | Description                              | Auth required?       |
|-----------------------------------|------------------------------------------|----------------------|
| `POST /api/v1/auth/register`      | Register new user                        | Does not require jwt |
| `POST /api/v1/auth/authenticate`  | Authenticate user                        | Does not require jwt |
| `POST /api/v1/transaction/publish`| Publish transactions to the system       | Requires jwt         |
| `GET /api/v1/transaction/`        | Gets paginated list of user transactions | Requires jwt         |

Once the system is up, swagger documentation is available at: `http://localhost:8080/swagger-ui/index.html#/`.

***
### Data related
- PostgreSQL: To store user and their accounts.
- Kafka: Used Kafka Topics and partitions to retrieve

Transactions are stored in kafka topics (see [Relevant implementation decisions](#Relevant-implementation-decisions) for justification).

The proposed solution for data management involves the creation of Kafka topics based on the year and user, following a specific schema: transactions-`year`-`clientName`. This approach ensures efficient data retrieval by dividing each topic into 12 partitions, corresponding to the months of the year.
***
### Security related
Security has been implemented with Spring Security and covers the following:
- User roles (User, Admin).
- Encrypted client passwords.
- JWT for authentication within HTTP requests.
- Overall HTTP security configuration (endpoint whitelisting and authentication requirements for others).
- Implementation of additional filters (filter chain) for enhanced security.

***
### Pre-Requisites
To run this code it is necessary the following:
- Java v17
- Docker
***
### Running the project
1. Clone the repository:
```shell
gh repo clone alexpages/ebanking-api
```
2. Go to the root of the project and initialize containers with the code below:
```shell
docker-compose up
```
3. After previous step, 4 containers initialize  (Kafka, Zookeper, Postgresql and ebanking-api) and the application is ready for interaction.

The docker-compose.yml file follows a structured approach, defining a total of 4 container.
The Spring Boot application is encapsulated within a Docker image, which is generated during the second step of the process through the execution of a Dockerfile, being this the only image that does not get pulled from Docker Hub.

***
### Examples of requests

`POST /api/v1/auth/register`
```
{
  "clientName": "synpulse8",
  "password": "password",
  "accounts": [
    {
      "iban": "CH93-0000-0000-0000-0000-0",
      "currency": "EUR"
    },
    {
      "iban": "EH93-0000-0000-0000-0000-0",
      "currency": "EUR"
    }
  ]
}
```
`POST /api/v1/auth/authenticate`
```
{
"clientName": "synpulse8",
"password": "password",
}
```
`POST /api/v1/transaction/publish`
```
{
  "id" : "synpulse8",
  "currency": "EUR",
  "amount": "350",
  "iban" : "CH93-0000-0000-0000-0000-0",
  "date" : "03-01-2019",
  "description" : "Online payment EUR"
}
```
```
"amount": "-350",
```

`GET /api/v1/transaction/`
```
{
  "clientName" : "synpulse8",
  "year" : "2019",
  "month" : "01",
  "pageSize" : "4"
}
```

***
### Key Implementation Decisions
- `N-Tier architecture/Folder architecture`: Organizing elements based on their functions within the project.
- `Kafka Topic/Partition` distribution:
  - Consideration 1: grouping transactions by IBAN.
  - Consideration 2: grouping transactions by KTable for interactive queries, especially for overall money balance.
  - Final decision: Using more topics (year-user combination) and partitions (12 per topic, representing months).
- `Dockerfile` for creating the Spring Boot application.
- Making the `REST API` reusable by utilizing environmental variables in docker-compose and application.yml files, with default values.
- K8s configuration performed using `kompose convert docker-compose.yml` to convert to Kubernetes format. Note that restart policies 'unless-stopped' in service kafka is not supported. To deploy it should be changed to 'always'.
- Avoiding the use of Pageable as transactions are not saved in a Repository.
- Determining that Kafka Streams is not the appropriate approach as it is designed for real-time data ingestion.

***
### Considerations for future improvements
- [x] Implement Custom SERDES for Kafka Streams.
- [ ] KTables and Kafka Topology for updating the total balance of each user (Key-Value Store) with interactive queries.
- [ ] Enhance JwtService for token renewal.
****


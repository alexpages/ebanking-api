banking-api
-
This project is intended to be a resolution proposal for Synpulse8 Junior Java Backend position open in Hong Kong.
***
### About
The system work as follows:

- User will have a token (jwt) and insert it in the request.
- Api Controller will validate jwt and if correct proceed to fetch data from messaging queue.
- Transactions are stored in topics within Kafka, it simulates a Real-in-Time data ingestion.

***
### Architecture

The suggested architecture is the following:

![](src/main/resources/architecture_overview.png)

***
### Database
- PostgreSQL: to store user, accounts and tokens (jwt)
- Kafka: To retrieve data from messaging queues and in-memory storage.

***
### Security
Security has been implemented with the following concepts:

- Roles
- Encoded client password
- JWT for Auth within http request
- General http security configuration (some are deprecated //TODO)
- Other filters
- 
***
### JUnit

****


[![Build Status](https://travis-ci.org/hemantpande/money-transfer-api.svg?branch=master)](https://travis-ci.org/hemantpande/money-transfer-api)

# Money Transfer Service

### Description
SimpleMoney Inc. wants to develop a API to transfer money between different accounts.

The money can be transferred between different currencies.

This repo holds the code for this API.

The use cases are specified in a separate file in the parent directory.

### Design
The design is service-oriented. The application is layered into below components:   
1. Rest API
2. Model based service (AccountService, for instance)
3. Repository

The REST API is capable of executing requests in parallel.   
All the business logic is handled by the Service, guarded by tests.   
The core area where the deadlock can happen (in money class), uses read-write re-entrant locks to prevent deadlocks.   

### Libraries used
1. JUnit - For testing
2. Spark - For hosting the API. (Our service runs in parallel mode by default. We are not using Spark's inbuilt parallel mechanism)   
3. Lombok - For keeping code concise and clean.   
4. Jackson - For json serialization and de-serialization

### Key decision points
1. For the requirements of an in-memory data store, there were 2 options. H2 and an in-memory collection.   
Our aim is to focus on testability, parallel executions and simplicity.   
Hence, a concurrent HashMap, since it suffices our need.   
2. We have followed [Subcutaneous testing](https://martinfowler.com/bliki/SubcutaneousTest.html)

### Steps to build and run 
**_Note_** - The application uses JAVA 11 to run. Please change the module settings if the code does not compile.   
1. Build the service - `mvn clean package`

2. Run locally from the project folder   
a. Copy dependencies to the directory `mvn install dependency:copy-dependencies`   
b. Run - `java -cp target/money-transfer-api-1.0-SNAPSHOT.jar:target/dependency/* simpleMoney.Application`   
c. The service by default starts at 8080, Hit - http://localhost:8080/status . The service should return `API is up and running`

3. Run tests - `mvn test`   
_TEST O/P_
```
------- T E S T S -------   
Running simpleMoney.concurrencyTests.AccountServiceShould
Processed 10000 transactions sequentially in PT-3.774388S
Processed 10000 transactions in parallel in PT-2.578507S
Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 6.414 sec - in simpleMoney.concurrencyTests.AccountServiceShould
Running simpleMoney.integrationTests.AccountShould
Tests run: 3, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0 sec - in simpleMoney.integrationTests.AccountShould
Running simpleMoney.unitTests.AccountShould
Tests run: 5, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0 sec - in simpleMoney.unitTests.AccountShould

Results :

Tests run: 10, Failures: 0, Errors: 0, Skipped: 0
```  


### REST API
The Account rest service exposes 4 APIs. They are described below.   
1. `POST /account` - Creates new account   
*Example*-   
URL - `POST http://localhost:8080/account`   
BODY - 
```
{
    "accountNumber": 1,
    "holderName": "Jack",
    "balance": {
        "amount": 100,
        "baseCurrency": "USD"
    }
}
``` 
STANDARD RESPONSE  
```
{
    "message":"Successfully created",
    "responseCode":"SUCCESS"
}
```
VALIDATIONS - Duplicate account check
```
{
    "message": "Account with same id already exists",
    "responseCode": "DUPLICATE_ACCOUNT"
}
```

2. `GET /account/1` - Gets account with ID 1   
URL - `GET http://localhost:8080/account/1`     
*Example*   
STANDARD RESPONSE
```
{
    "data": {
        "accountNumber": 1,
        "holderName": "Hemant",
        "balance": 100,
        "baseCurrency": "USD"
    }
}
```
VALIDATIONS - Account that is being retrived should exists.   
RESPONSE -
```
{
    "message": "Account does not exist",
    "responseCode": "NOT_FOUND"
}
```

3. `DELETE /account/1` - Deletes account with ID 1 
URL - `DELETE http://localhost:8080/account/1`
STANDARD RESPONSE
```
{
    "message": "Successfully deleted",
    "responseCode": "SUCCESS"
}
```
VALIDATIONS - Account that is being deleted should exists.   
RESPONSE -
```
{
    "message": "Account does not exist",
    "responseCode": "NOT_FOUND"
}
```

4. `POST /account-transfer` - Transfers money from one account to another. Currency conversion is done to deposit money into destination account.   
URL - `POST http://localhost:8080/account-transfer`   
BODY -
```
{
    "fromId":1,
    "toId":2,
    "amount":50
}
```
STANDARD RESPONSE -   
```
{
    "message": "Request executed",
    "responseCode": "SUCCESS"
}
```
VALIDATIONS -    
a. Insufficient balance at source   
```
{
    "message": "Insufficient balance at source",
    "responseCode": "INSUFFICIENT_BALANCE"
}
```
b. Either of the accounts does not exists   
```
{
    "message": "Account does not exist",
    "responseCode": "NOT_FOUND"
}
```

5. `/status` - Check the health of API
STANDARD RESPONSE    
URL - http://localhost:8080/status   
```
API is up and running
```

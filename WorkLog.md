### Session 1
Ok, so we are here, starting afresh and our problem we're trying to solve is as below:
 
Our mission (if we choose to accept) is to create an API, that will _"transfer"_ **money** between different bank **accounts**.

Ok, so it seems we already have a domain emerging out from our problem statement.

Nouns (Models) - _Money, Account_

Verbs (Behavior) - _transfer_

Let's see, if we can come up with some use cases for our problem. We'll maintain it in separate file called "UseCases.md".

### Session 2

We do TDD. Let's start with tests first. Before that, we need to give some thought to our project structure.

Let's start with what we know right now. We have a domain and on scaffolding side, we'll need some repositories for data interactions. 

We'll have a CORE package. Let's keep a clear separation between Models, services and repositories. For now they are in one module, but different packages.  
.  
.  
.  
ok, so we have our basic project structure in place. Let's think a bit from SRP perspective, we need a class which will be responsible for managing the transactions. Need to keep a check in amount of code going into this class, down the line, just a thought to keep in back of the mind. For now, it looks a decent start. Let's write our first test for UseCase 1.

Test results  

Sequential:
Processed 1000000 transactions completed in PT-5.722342S

Parallel
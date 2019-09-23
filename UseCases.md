We'll be using 3 personas, John, Steve and Hemant.

### Use cases

##### 1. New account should get created

##### 2. New account with an existing account ID should not get created

##### 3. Transfer money between accounts with same currency
a. John has an account with base currency as USD  
b. Steve has an account with base currency as USD  
c. John wants to transfer 100 USD in Steve's account  
d. System debits 100 USD from John's account and credits 100 USD into Steve's account.  

##### 4. Transfer money between accounts with different currencies
a. John has an account with base currency as USD  
b. Hemant lives in India and has an account with base currency as INR and  
c. John wants to transfer 100 USD in Hemant's account  
d. System debits 100 USD from John's account and credits corresponding amount in INR into Hemant's account.  

##### 5. John's account has an insufficient balance
a. John wants to transfer 100 pounds to Steve.  
b. John's current account balance is 50 pounds.  
c. System sends back a response to John, indicating "Insufficient balance".  

##### 6. All transactions should get recorded as a part of compliance
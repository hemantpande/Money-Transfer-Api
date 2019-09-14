We'll be using 3 personas, John, Steve and Hemant.

### Use cases
##### 1. Transfer money between accounts
a. John has an account with base currency as USD  
b. Steve has an account with base currency as USD  
c. John wants to transfer 100 USD in Steve's account  
d. System debits 100 USD from John's account and credits 100 USD into Steve's account.  

##### 2. Transfer money between accounts
a. John has an account with base currency as USD  
b. Hemant lives in India and has an account with base currency as INR and  
c. John wants to transfer 100 USD in Hemant's account  
d. System debits 100 USD from John's account and credits corresponding amount in INR into Hemant's account.  

##### 3. John's account has an insufficient balance
a. John wants to transfer 100 pounds to Steve.  
b. John's current account balance is 50 pounds.  
c. System sends back a response to John, indicating "Insufficient balance".  

##### 4. Steve's account is inactive
a. John wants to send 100 pounds to Steve.  
b. Unfortunately, Steve's account is inactive.  
c. System sends a response back to John, indicating "Inactive destination account".  


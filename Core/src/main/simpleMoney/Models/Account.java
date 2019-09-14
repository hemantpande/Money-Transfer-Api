package simpleMoney.Models;

public class Account {
    
    private int accountNumber;
    private String holderName;
    private double initialBalance;
    private Currencies baseCurrency;

    public void setAccountNumber(int accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void setHolderName(String holderName) {
        this.holderName = holderName;
    }

    public void setInitialBalance(double initialBalance) {
        this.initialBalance = initialBalance;
    }

    public void setBaseCurrency(Currencies baseCurrency) {
        this.baseCurrency = baseCurrency;
    }
}

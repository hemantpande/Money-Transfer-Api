package simpleMoney.builders;

import simpleMoney.models.Account;
import simpleMoney.models.Currencies;
import simpleMoney.models.Money;

public class AccountBuilder {

    private Account account;
    private Money initialBalance;

    public AccountBuilder newAccountWithId(Long accountNumber) {
        this.initialBalance = new Money();
        this.account = new Account(accountNumber);
        return this;
    }

    public AccountBuilder withName(String holderName) {
        account.setHolderName(holderName);
        return this;
    }

    public AccountBuilder andInitialBalance(double balance) {
        initialBalance.setBalance(balance);
        return this;
    }

    public AccountBuilder withBaseCurrency(Currencies baseCurrency) {
        initialBalance.setBaseCurrency(baseCurrency);
        return this;
    }

    public Account build() {
        account.setBalance(initialBalance);
        return account;
    }
}

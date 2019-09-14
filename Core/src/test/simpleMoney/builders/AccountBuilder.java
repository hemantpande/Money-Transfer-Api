package simpleMoney.builders;

import simpleMoney.models.Account;
import simpleMoney.models.Currencies;

public class AccountBuilder {

    private Account account;

    public AccountBuilder newAccountWithId(Long accountNumber) {
        this.account = new Account(accountNumber);
        return this;
    }

    public AccountBuilder withName(String holderName) {
        account.setHolderName(holderName);
        return this;
    }

    public AccountBuilder andInitialBalance(double initialBalance) {
        account.setInitialBalance(initialBalance);
        return this;
    }

    public AccountBuilder withBaseCurrency(Currencies baseCurrency) {
        account.setBaseCurrency(baseCurrency);
        return this;
    }

    public Account build() {
        return account;
    }
}

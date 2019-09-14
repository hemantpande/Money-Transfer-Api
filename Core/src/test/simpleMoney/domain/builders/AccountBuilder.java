package simpleMoney.domain.builders;

import simpleMoney.Models.Account;
import simpleMoney.Models.Currencies;

public class AccountBuilder {

    private Account account;

    public AccountBuilder getNewAccount() {
        this.account = new Account();
        return this;
    }

    public AccountBuilder withId(int accountNumber) {
        account.setAccountNumber(accountNumber);
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

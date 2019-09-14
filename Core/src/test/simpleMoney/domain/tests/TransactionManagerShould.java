package simpleMoney.domain.tests;


import org.junit.Test;
import simpleMoney.domain.TransactionManager;
import simpleMoney.domain.builders.AccountBuilder;

import simpleMoney.Models.Currencies;

public class TransactionManagerShould {

    @Test
    public void transferMoneyBetweenTwoAccounts(){

        var jackAccount = new AccountBuilder().getNewAccount()
                .withId(1)
                .withName("Jack")
                .withBaseCurrency(Currencies.USD)
                .andInitialBalance(500)
                .build();

        var willAccount = new AccountBuilder().getNewAccount()
                .withId(2)
                .withName("Will")
                .withBaseCurrency(Currencies.USD)
                .andInitialBalance(0)
                .build();

        var transactionManager = new TransactionManager();
    }

}

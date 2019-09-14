package simpleMoney.services.tests;


import org.junit.Test;
import simpleMoney.services.AccountService;
import simpleMoney.services.TransactionService;
import simpleMoney.services.builders.AccountBuilder;

import simpleMoney.models.Currencies;

public class TransactionServiceShould {

    @Test
    public void transferMoneyBetweenTwoAccounts(){

        var jackAccount = new AccountBuilder()
                .newAccountWithId(1L)
                .withName("Jack")
                .withBaseCurrency(Currencies.USD)
                .andInitialBalance(500)
                .build();
        var accountService = new AccountService();
        accountService.create(jackAccount);

        var steveAccount = new AccountBuilder()
                .newAccountWithId(2L)
                .withName("Steve")
                .withBaseCurrency(Currencies.USD)
                .andInitialBalance(0)
                .build();
        accountService.create(steveAccount);

        var transactionService = new TransactionService();
    }

}

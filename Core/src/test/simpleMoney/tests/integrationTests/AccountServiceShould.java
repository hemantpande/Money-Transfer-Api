package simpleMoney.tests.integrationTests;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import simpleMoney.builders.TransferRequestBuilder;
import simpleMoney.models.Account;
import simpleMoney.models.TransferRequest;
import simpleMoney.services.AccountService;
import simpleMoney.builders.AccountBuilder;
import simpleMoney.models.Currencies;

public class AccountServiceShould {

    private final AccountService accountService = new AccountService();

    private Account jackAccount;
    private final Long jackAccountId = 1L;

    private Account steveAccount;
    private final Long steveAccountId = 2L;

    private Account hemantAccount;
    private final Long hemantAccountId = 3L;

    @Before
    public void setup(){

        jackAccount = new AccountBuilder()
                .newAccountWithId(jackAccountId)
                .withName("Jack")
                .withBaseCurrency(Currencies.USD)
                .andInitialBalance(500)
                .build();
        accountService.create(jackAccount);
    }

    @Test
    public void transferMoneyBetweenTwoAccountsWithSameCurrency(){

        steveAccount = new AccountBuilder()
                .newAccountWithId(steveAccountId)
                .withName("Steve")
                .withBaseCurrency(Currencies.USD)
                .andInitialBalance(0)
                .build();
        accountService.create(steveAccount);

        TransferRequest request = new TransferRequestBuilder()
                .transfer(100D)
                .from(jackAccountId)
                .to(steveAccountId)
                .build();

        accountService.transfer(request);

        double jackCurrentBalance = accountService.getById(jackAccountId).getBalance();
        double steveCurrentBalance = accountService.getById(steveAccountId).getBalance();

        Assert.assertEquals(400D, jackCurrentBalance,0);
        Assert.assertEquals(100L, steveCurrentBalance,0);
    }

    @Test
    public void transferMoneyBetweenTwoAccountsWithDifferentCurrency(){

        hemantAccount = new AccountBuilder()
                .newAccountWithId(hemantAccountId)
                .withName("Steve")
                .withBaseCurrency(Currencies.INR)
                .andInitialBalance(0)
                .build();
        accountService.create(hemantAccount);

        TransferRequest request = new TransferRequestBuilder()
                .transfer(100D)
                .from(jackAccountId)
                .to(hemantAccountId)
                .build();

        accountService.transfer(request);

        double jackCurrentBalance = accountService.getById(jackAccountId).getBalance();
        double hemantCurrentBalance = accountService.getById(hemantAccountId).getBalance();

        Assert.assertEquals(400D, jackCurrentBalance,0);
        Assert.assertEquals(7103L, hemantCurrentBalance,0);
    }

    @After
    public void teardown(){

    }
}

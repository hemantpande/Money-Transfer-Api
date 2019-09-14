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

    private AccountService accountService;
    private Account jackAccount;
    private Account steveAccount;
    private final Long jackAccountId = 1L;
    private final Long steveAccountId = 2L;

    @Before
    public void setup(){
        accountService = new AccountService();

        jackAccount = new AccountBuilder()
                .newAccountWithId(jackAccountId)
                .withName("Jack")
                .withBaseCurrency(Currencies.USD)
                .andInitialBalance(500)
                .build();
        accountService.create(jackAccount);

        steveAccount = new AccountBuilder()
                .newAccountWithId(steveAccountId)
                .withName("Steve")
                .withBaseCurrency(Currencies.USD)
                .andInitialBalance(0)
                .build();
        accountService.create(steveAccount);
    }

    @Test
    public void transferMoneyBetweenTwoAccounts(){
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

    @After
    public void teardown(){

    }
}

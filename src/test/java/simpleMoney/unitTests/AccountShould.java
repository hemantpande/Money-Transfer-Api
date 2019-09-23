package simpleMoney.unitTests;

import org.junit.Assert;
import org.junit.Test;
import simpleMoney.library.exceptions.*;
import simpleMoney.models.Account;
import simpleMoney.services.AccountService;

public class AccountShould {

    @Test
    public void CreateNewAccount(){
        AccountService accountService = new AccountService();
        Account excepted = new Account(1L);

        accountService.create(excepted);
        Account actual = accountService.getById(1L);

        Assert.assertEquals(actual, excepted);
    }

    @Test(expected = AlreadyExistsException.class)
    public void NotGetCreatedWhenAccountWithSameIdExists(){
        AccountService accountService = new AccountService();
        accountService.create(new Account(1L));
        accountService.create(new Account(1L));
    }

    @Test(expected = NotFoundException.class)
    public void NotBeFetchedWhenItDoesNotExists(){
        AccountService accountService = new AccountService();
        accountService.getById(1L);
    }

}

package simpleMoney.unitTests;

import org.junit.Assert;
import org.junit.Test;
import simpleMoney.library.exceptions.*;
import simpleMoney.models.Account;
import simpleMoney.services.AccountService;

public class AccountShould {

    @Test
    public void createNewAccount(){
        AccountService accountService = new AccountService();
        Account excepted = new Account(1L);

        accountService.create(excepted);
        Account actual = accountService.getById(1L);

        Assert.assertEquals(actual, excepted);
    }

    @Test(expected = AlreadyExistsException.class)
    public void notGetCreatedWhenAccountWithSameIdExists(){
        AccountService accountService = new AccountService();
        accountService.create(new Account(1L));
        accountService.create(new Account(1L));
    }

    @Test(expected = NotFoundException.class)
    public void notBeFetchedWhenItDoesNotExists(){
        AccountService accountService = new AccountService();
        accountService.getById(1L);
    }

    @Test
    public void getDeletedWhenItExists(){
        AccountService accountService = new AccountService();
        Account excepted = new Account(1L);

        accountService.create(excepted);
        accountService.delete(1L); // Assertion is that no exception is thrown
    }

    @Test(expected = NotFoundException.class)
    public void notGetDeletedWhenItDoesNotExists(){
        AccountService accountService = new AccountService();
        accountService.delete((1L));
    }
}

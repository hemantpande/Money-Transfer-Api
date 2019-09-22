package simpleMoney.unitTests;

import org.junit.Before;
import org.junit.Test;
import simpleMoney.library.exceptions.AccountException;
import simpleMoney.models.Account;
import simpleMoney.services.AccountService;

public class AccountShould {

    @Test(expected = AccountException.class)
    public void NotGetCreatedWhenAccountWithSameIdExists(){
        AccountService accountService = new AccountService();
        accountService.create(new Account(1L));
        accountService.create(new Account(1L));
    }

    @Test
    public void NotBeFetchedWhenItDoesNotExists(){

    }

}

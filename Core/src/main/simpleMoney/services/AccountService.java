package simpleMoney.services;

import simpleMoney.library.Repository;
import simpleMoney.models.Account;
import simpleMoney.repositories.InMemoryRepository;

public class AccountService {

    private final Repository<Account> _accountRepository;

    public AccountService() {
        _accountRepository = new InMemoryRepository<>();
    }

    public AccountService(Repository<Account> _accountRepository) {
        this._accountRepository = _accountRepository;
    }

    public void create(Account account) {
        //_accountRepository.create(account);

    }
}

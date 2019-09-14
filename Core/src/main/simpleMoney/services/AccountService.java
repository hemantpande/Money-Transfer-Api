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
        _accountRepository.create(account.getAccountNumber(), account);
    }

    public Account getById(Long id){
        return _accountRepository.getById(id);
    }

    // TODO : we need to return a proper transfer status
    public void transfer(Long fromId, Long toId, Double amount){

    }
}

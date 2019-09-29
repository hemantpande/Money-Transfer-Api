package simpleMoney.services;

import simpleMoney.library.Repository;
import simpleMoney.library.ResponseCode;
import simpleMoney.library.exceptions.AlreadyExistsException;
import simpleMoney.library.exceptions.InsufficientBalanceException;
import simpleMoney.library.exceptions.NotFoundException;
import simpleMoney.library.exceptions.SameAccountException;
import simpleMoney.models.Account;
import simpleMoney.models.Currency;
import simpleMoney.models.TransferRequest;
import simpleMoney.repositories.InMemoryRepository;

public class AccountService{

    private Object lock;

    private final Repository<Account> _accountRepository;

    public AccountService() {
        this(new InMemoryRepository<>());
        lock = new Object();
    }

    // Note: For mocking repository
    public AccountService(Repository<Account> _accountRepository) {
        this._accountRepository = _accountRepository;
    }

    public void create(Account account) throws AlreadyExistsException {
        _accountRepository.create(account.getAccountNumber(), account);
    }

    public void delete(Long accountId) throws NotFoundException {
        _accountRepository.delete(accountId);
    }

    public Account getById(Long id) throws NotFoundException{
        return _accountRepository.getById(id);
    }

    public ResponseCode transfer(TransferRequest request)
            throws NotFoundException, InsufficientBalanceException, SameAccountException {

        if(request.getFromId().equals(request.getToId())){
            throw new SameAccountException(ResponseCode.SAME_ACCOUNT, "Money cannot be transferred within same account");
        }

        final Account fromAccount = _accountRepository.getById(request.getFromId());
        final Account toAccount = _accountRepository.getById(request.getToId());

        if(fromAccount == null || toAccount == null){
            throw new NotFoundException(ResponseCode.NOT_FOUND,
                    "Account involved in the transaction do not exists");
        }

        final Currency sourceCurrencyForConversion = fromAccount.getBaseCurrency();

        synchronized(lock){
            fromAccount.debit(request.getAmount());
            toAccount.credit(request.getAmount(), sourceCurrencyForConversion);
        }

        _accountRepository.update(request.getFromId(), fromAccount);
        _accountRepository.update(request.getToId(), toAccount);

        return ResponseCode.SUCCESS;
    }
}

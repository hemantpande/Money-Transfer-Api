package simpleMoney.services;

import simpleMoney.library.Repository;
import simpleMoney.library.ResponseCode;
import simpleMoney.library.exceptions.*;
import simpleMoney.models.Account;
import simpleMoney.models.Currency;
import simpleMoney.models.TransferRequest;
import simpleMoney.repositories.InMemoryRepository;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

public class AccountService{

    private final int TIMEOUT_PERIOD = 5;
    private final Repository<Account> _accountRepository;

    public AccountService() {
        this(new InMemoryRepository<>());
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

        final Account fromAccount = _accountRepository.getById(request.getFromId());
        final Account toAccount = _accountRepository.getById(request.getToId());

        validateRequest(request, fromAccount, toAccount);

        final Currency sourceCurrencyForConversion = fromAccount.getBaseCurrency();

        tryDebiting(request, fromAccount);

        tryCrediting(request, toAccount, sourceCurrencyForConversion);

        return ResponseCode.SUCCESS;
    }

    private void tryCrediting(TransferRequest request, Account toAccount, Currency sourceCurrencyForConversion) {
        final Lock lock = toAccount.getLock();
        try {
            if (lock.tryLock(TIMEOUT_PERIOD, TimeUnit.SECONDS)) {
                try{
                    toAccount.credit(request.getAmount(), sourceCurrencyForConversion);
                    _accountRepository.update(request.getToId(), toAccount);
                }catch (Exception exception){
                    throw exception;
                }finally {
                    lock.unlock();
                    toAccount.setLock(lock);
                }
            }else{
                throw new FailedTransactionException(ResponseCode.TRANSACTION_FAILURE,
                        "Transaction Failed");
            }
        } catch (InterruptedException e) {
            throw new FailedTransactionException(ResponseCode.TRANSACTION_FAILURE,
                    "Transaction Failed");
        }
    }

    private void tryDebiting(TransferRequest request, Account fromAccount) {
        final Lock lock = fromAccount.getLock();
        try {
            if (lock.tryLock(TIMEOUT_PERIOD, TimeUnit.SECONDS)) {
                try{
                    fromAccount.debit(request.getAmount());
                    _accountRepository.update(request.getFromId(), fromAccount);
                }catch (Exception exception){
                    throw exception;
                }finally {
                    lock.unlock();
                    fromAccount.setLock(lock);
                }
            }
        } catch(InsufficientBalanceException exception){
            throw exception;
        } catch (InterruptedException exception) {
            throw new FailedTransactionException(ResponseCode.TRANSACTION_FAILURE,
                    "Transaction Failed");
        }
    }

    private void validateRequest(TransferRequest request, Account fromAccount, Account toAccount) {
        if(fromAccount == null || toAccount == null){
            throw new NotFoundException(ResponseCode.NOT_FOUND,
                    "Account involved in the transaction do not exists");
        }

        if(request.getFromId().equals(request.getToId())){
            throw new SameAccountException(ResponseCode.SAME_ACCOUNT, "Money cannot be transferred within same account");
        }
    }
}

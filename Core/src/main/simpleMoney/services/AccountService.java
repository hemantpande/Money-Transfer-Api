package simpleMoney.services;

import simpleMoney.library.Repository;
import simpleMoney.library.ResponseCode;
import simpleMoney.models.Account;
import simpleMoney.models.Currencies;
import simpleMoney.models.TransferRequest;
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
    public ResponseCode transfer(TransferRequest request) {

        try{
            final Account fromAccount = _accountRepository.getById(request.getFromId());
            final Account toAccount = _accountRepository.getById(request.getToId());
            final Currencies sourceCurrencyForConversion = fromAccount.getBaseCurrency();

            fromAccount.debit(request.getAmount());
            toAccount.credit(request.getAmount(), sourceCurrencyForConversion);

            _accountRepository.update(request.getFromId(), fromAccount);
            _accountRepository.update(request.getToId(), toAccount);

            return ResponseCode.SUCCESS;
        }catch(Exception exception){
            return ResponseCode.FAILURE;
        }
    }
}

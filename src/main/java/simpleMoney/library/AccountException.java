package simpleMoney.library;

public class AccountException extends BaseException {

    public AccountException(ResponseCode responseCode, String message, String... args) {
        super(responseCode, message, args);
    }
}
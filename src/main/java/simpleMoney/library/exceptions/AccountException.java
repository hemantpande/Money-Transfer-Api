package simpleMoney.library.exceptions;

import simpleMoney.library.ResponseCode;

public class AccountException extends BaseException {

    public AccountException(ResponseCode responseCode, String message, String... args) {
        super(responseCode, message, args);
    }
}
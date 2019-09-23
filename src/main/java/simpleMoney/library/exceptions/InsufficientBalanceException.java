package simpleMoney.library.exceptions;

import simpleMoney.library.ResponseCode;

public class InsufficientBalanceException extends BaseException {

    public InsufficientBalanceException(ResponseCode responseCode, String message, String... args) {
        super(responseCode, message, args);
    }
}
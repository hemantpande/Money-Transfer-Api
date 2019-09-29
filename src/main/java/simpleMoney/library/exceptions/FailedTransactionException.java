package simpleMoney.library.exceptions;

import simpleMoney.library.ResponseCode;

public class FailedTransactionException extends BaseException {
    public FailedTransactionException(ResponseCode responseCode, String message, String... args) {
        super(responseCode, message, args);
    }
}

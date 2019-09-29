package simpleMoney.library.exceptions;

import simpleMoney.library.ResponseCode;

public class SameAccountException extends BaseException {
    public SameAccountException(ResponseCode responseCode, String message, String... args) {
        super(responseCode, message, args);
    }
}

package simpleMoney.library.exceptions;

import simpleMoney.library.ResponseCode;

public class AlreadyExistsException extends BaseException {

    public AlreadyExistsException(ResponseCode responseCode, String message, String... args) {
        super(responseCode, message, args);
    }
}
package simpleMoney.library.exceptions;

import simpleMoney.library.ResponseCode;

public class BaseException extends RuntimeException {

    private final ResponseCode responseCode;

    public BaseException(ResponseCode responseCode, String message, String... args) {
        super(String.format(message, args));
        this.responseCode = responseCode;
    }

    public BaseException(ResponseCode responseCode, String message, Throwable cause) {
        super(message, cause);
        this.responseCode = responseCode;
    }

    public ResponseCode getResponseCode() {
        return responseCode;
    }
}
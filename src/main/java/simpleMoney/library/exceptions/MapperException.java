package simpleMoney.library.exceptions;


import simpleMoney.library.ResponseCode;

public class MapperException extends BaseException {

    public MapperException(ResponseCode responseCode, String message, Throwable cause) {
        super(responseCode, message, cause);
    }
}

package simpleMoney.library;


public class MapperException extends BaseException {

    public MapperException(ResponseCode responseCode, String message, Throwable cause) {
        super(responseCode, message, cause);
    }
}

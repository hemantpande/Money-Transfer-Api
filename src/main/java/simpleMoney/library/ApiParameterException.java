package simpleMoney.library;

public class ApiParameterException extends BaseException {

  public ApiParameterException(ResponseCode responseCode, String message, String... args) {
    super(responseCode, message, args);
  }
}

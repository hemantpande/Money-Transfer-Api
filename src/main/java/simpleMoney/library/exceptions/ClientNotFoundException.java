package simpleMoney.library.exceptions;

import simpleMoney.library.ResponseCode;

public class ClientNotFoundException extends BaseException {

  public ClientNotFoundException(ResponseCode responseCode, String message, String... args) {
    super(responseCode, message, args);
  }
}

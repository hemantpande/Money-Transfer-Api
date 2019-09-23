package simpleMoney.library.exceptions;

import simpleMoney.library.ResponseCode;

public class NotFoundException extends BaseException {

  public NotFoundException(ResponseCode responseCode, String message, String... args) {
    super(responseCode, message, args);
  }
}

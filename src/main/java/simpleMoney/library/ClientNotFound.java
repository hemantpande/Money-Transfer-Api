package simpleMoney.library;

public class ClientNotFound extends BaseException {

  public ClientNotFound(ResponseCode responseCode, String message, String... args) {
    super(responseCode, message, args);
  }
}

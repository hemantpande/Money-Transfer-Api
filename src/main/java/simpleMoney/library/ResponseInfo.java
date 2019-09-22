package simpleMoney.library;

import lombok.Data;

@Data
public class ResponseInfo<T> {

  private String message;

  private T data;

  private String responseCode;

  public void setData(T data){
    if(data == null){
      this.setResponseCode(ResponseCode.NOT_FOUND.toString());
    }
    this.data = data;
  }

  public static ResponseInfo create(String msg, ResponseCode responseCode){
    ResponseInfo info = new ResponseInfo();
    info.setMessage(msg);
    info.setResponseCode(responseCode.toString());
    return info;
  }
}

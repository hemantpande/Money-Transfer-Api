package simpleMoney.builders;

import simpleMoney.models.TransferRequest;

public class TransferRequestBuilder {

    private TransferRequest request;

    public TransferRequestBuilder() {
        this.request = new TransferRequest();
    }

    public TransferRequestBuilder from(Long id){
        request.setFromId(id);
        return this;
    }

    public TransferRequestBuilder to(Long id){
        request.setToId(id);
        return this;
    }

    public TransferRequestBuilder transfer(double amount){
        request.setAmount(amount);
        return this;
    }

    public TransferRequest build(){
        return request;
    }
}

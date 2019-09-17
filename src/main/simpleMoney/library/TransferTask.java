package simpleMoney.library;

import simpleMoney.models.TransferRequest;

import java.util.concurrent.Callable;
import java.util.function.Function;

public class TransferTask implements Callable<ResponseCode> {

    private final TransferRequest request;

    private final Function<TransferRequest, ResponseCode> callBack;

    public TransferTask(TransferRequest request, Function<TransferRequest, ResponseCode> callBack) {
        this.request = request;
        this.callBack = callBack;
    }

    @Override
    public ResponseCode call() throws Exception {
        return callBack.apply(request);
    }
}

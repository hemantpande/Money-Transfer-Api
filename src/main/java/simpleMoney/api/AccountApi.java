package simpleMoney.api;

import lombok.extern.slf4j.Slf4j;
import simpleMoney.library.*;
import simpleMoney.models.Account;
import simpleMoney.models.TransferRequest;
import simpleMoney.services.AccountService;
import spark.Request;
import spark.Response;

import static org.eclipse.jetty.http.HttpStatus.*;
import static simpleMoney.library.ResponseCode.INVALID_ID;
import static simpleMoney.library.ResponseCode.SUCCESS;
import static spark.Spark.*;
import static spark.Spark.exception;

@Slf4j
public class AccountApi {

    private static final String PARAM_ACCOUNT_ID = ":accountId";

    private final AccountService service;

    public AccountApi() {
        service = new AccountService();
        initApi();
        initExceptionMapper();
    }

    private void initApi() {
        port(8080);
        get("/status", (req, res) -> "API is up and running");

        post("/account", this::createClient);
        get("/account/:accountId", this::getClient);
        delete("/account/:accountId", this::deleteClient);
        post("/account/:accountId/transfer", this::transferMoney);
    }

    private void initExceptionMapper() {
        exception(ApiParameterException.class, (exception, req, res) -> handleException(exception, res, BAD_REQUEST_400));
        exception(ClientNotFound.class, (exception, req, res) -> handleException(exception, res, NOT_FOUND_404));
        exception(MapperException.class, (exception, req, res) -> handleException(exception, res, BAD_REQUEST_400));
        exception(AccountException.class, (exception, req, res) -> handleException(exception, res, BAD_REQUEST_400));
    }

    private String createClient(Request request, Response response) {
        Account account = Mapper.fromJson(request.body(), Account.class);
        service.create(account);
        response.status(CREATED_201);
        return Mapper.toJson(ResponseInfo.create("Successfully created", SUCCESS));
    }

    private String getClient(Request request, Response response) {
        long id = parseAccountId(request.params(PARAM_ACCOUNT_ID));
        Account client = service.getById(id);
        ResponseInfo<Account> responseInfo = new ResponseInfo<>();
        responseInfo.setData(client);
        return Mapper.toJson(responseInfo);
    }

    private String deleteClient(Request req, Response res) {
        long id = parseAccountId(req.params(PARAM_ACCOUNT_ID));
        service.delete(id);
        return Mapper.toJson(ResponseInfo.create("Successfully deleted", SUCCESS));
    }

    private String transferMoney(Request request, Response response) {

        TransferRequest transferRequest = Mapper.fromJson(request.body(), TransferRequest.class);
        final ResponseCode responseCode = service.transfer(transferRequest);
        return Mapper.toJson(ResponseInfo.create("Response code", responseCode));
    }

    private <T extends BaseException> void handleException(T exception, Response res, int statusCode) {
        res.status(statusCode);
        res.body(Mapper.toJson(ResponseInfo.create(exception.getMessage(), exception.getResponseCode())));
    }

    private long parseAccountId(String accountId) {
        try {
            return Long.valueOf(accountId);
        } catch (Exception e) {
            throw new ApiParameterException(INVALID_ID, "AccountId should be a number, but -> %s", accountId);
        }
    }
}

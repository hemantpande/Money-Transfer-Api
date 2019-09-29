package simpleMoney.api;

import lombok.extern.slf4j.Slf4j;
import simpleMoney.library.*;
import simpleMoney.library.exceptions.*;
import simpleMoney.models.Account;
import simpleMoney.models.TransferRequest;
import simpleMoney.services.AccountService;
import spark.Request;
import spark.Response;
import spark.Spark;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static org.eclipse.jetty.http.HttpStatus.*;
import static simpleMoney.library.ResponseCode.*;

@Slf4j
public class AccountRestService extends RestServiceBase {

    private static final String PARAM_ACCOUNT_ID = ":accountId";
    private final AccountService service;
    public AccountRestService() {
        service = new AccountService();
        Service_root = "account";
        Service_root_id = "accountId";
        initApi();
        initExceptionMapper();
        Spark.post("/account-transfer", this::transfer);
    }

    @Override
    public String create(Request request, Response response) {
        return super.executeRequest(() -> {
            try{
                Account account = Mapper.fromJson(request.body(), Account.class);
                service.create(account);
                response.status(CREATED_201);
                return Mapper.toJson(ResponseInfo.create("Successfully created", SUCCESS));
            }catch (AlreadyExistsException exception){
                return Mapper.toJson(ResponseInfo.create("Account with same id already exists", DUPLICATE_ACCOUNT));
            }catch (MapperException exception){
                return Mapper.toJson(ResponseInfo.create("Bad request", BAD_REQUEST));
            }catch(Exception exception){
                return Mapper.toJson(ResponseInfo.create("Bad request", BAD_REQUEST));
            }
        });
    }

    @Override
    public String get(Request request, Response response) {
        return super.executeRequest(() -> {
            try{
                long id = parseAccountId(request.params(PARAM_ACCOUNT_ID));
                Account account = service.getById(id);
                ResponseInfo<Account> responseInfo = new ResponseInfo<>();
                responseInfo.setData(account);
                return Mapper.toJson(responseInfo);
            }catch(NotFoundException exception){
                return Mapper.toJson(ResponseInfo.create("Account does not exist", NOT_FOUND));
            }catch(ApiParameterException exception){
                return Mapper.toJson(ResponseInfo.create("AccountId should be a number", FAILURE));
            }catch(Exception exception){
                return Mapper.toJson(ResponseInfo.create("Bad request", BAD_REQUEST));
            }
        });
    }

    @Override
    public String delete(Request req, Response res) {
        return super.executeRequest(() -> {
            try{
                long id = parseAccountId(req.params(PARAM_ACCOUNT_ID));
                service.delete(id);
                return Mapper.toJson(ResponseInfo.create("Successfully deleted", SUCCESS));
            }catch(NotFoundException exception){
                return Mapper.toJson(ResponseInfo.create("Account does not exist", NOT_FOUND));
            }catch(ApiParameterException exception){
                return Mapper.toJson(ResponseInfo.create("AccountId should be a number", FAILURE));
            }catch(Exception exception){
                return Mapper.toJson(ResponseInfo.create("Bad request", BAD_REQUEST));
            }
        });
    }

    public String transfer(Request request, Response response) {
        return super.executeRequest(() -> {
            try{
                TransferRequest transferRequest = Mapper.fromJson(request.body(), TransferRequest.class);
                final ResponseCode responseCode = service.transfer(transferRequest);
                return Mapper.toJson(ResponseInfo.create("Request executed", responseCode));
            }catch(NotFoundException exception){
                return Mapper.toJson(ResponseInfo.create("Account does not exist", NOT_FOUND));
            }catch (InsufficientBalanceException exception){
                return Mapper.toJson(ResponseInfo.create("Insufficient balance at source", INSUFFICIENT_BALANCE));
            }catch(Exception exception){
                return Mapper.toJson(ResponseInfo.create("Bad request", BAD_REQUEST));
            }
        });
    }

    private long parseAccountId(String accountId) {
        try {
            return Long.valueOf(accountId);
        } catch (Exception e) {
            throw new ApiParameterException(INVALID_ID, "AccountId should be a number, value provided -> %s", accountId);
        }
    }
}

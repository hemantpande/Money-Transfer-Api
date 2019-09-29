package simpleMoney.apiTests;

import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.*;
import simpleMoney.Application;
import simpleMoney.builders.AccountBuilder;
import simpleMoney.builders.TransferRequestBuilder;
import simpleMoney.library.Mapper;
import simpleMoney.library.ResponseInfo;
import simpleMoney.models.Account;
import simpleMoney.models.Currency;
import simpleMoney.models.TransferRequest;
import spark.Spark;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;
import static simpleMoney.library.ResponseCode.*;

public class AccountApiShould {

    private Client restEndpoint;
    private String baseUrl = "http://localhost:8080/account";
    private final URI endPoint = URI.create(baseUrl);

    @BeforeClass
    public static void startApi(){
        Application.start();
    }

    @AfterClass
    public static void cleanUp(){
        Spark.stop();
    }

    @Before
    public void setup() {
        restEndpoint = ClientBuilder.newBuilder().build();
    }

    @After
    public void tearDown() {
        restEndpoint.close();
    }

    @Test
    public void createAccountWhenItIsValid(){
        final Account account = getTestAccount(1L, Currency.USD, 100D);
        final Response response = create(account);
        final ResponseInfo responseInfo = parseResponse(response);
        assertThat(responseInfo).isEqualTo(ResponseInfo.create("Successfully created", SUCCESS));
        delete(account);
    }

    @Test
    public void notCreateAccountWhenRequestIsNotAsExpected(){
        final Response response = restEndpoint.target(endPoint)
                .request()
                .post(Entity.json("{\"a\":1,\"b\":[{\"c\":100,\"d\":\"USD\"}]}"));
        final ResponseInfo responseInfo = parseResponse(response);
        assertThat(responseInfo).isEqualTo(ResponseInfo.create("Cannot parse request to JSON", PARSER_ERROR));
    }

    @Test
    public void notCreateAccountWhenItAlreadyExists(){
        Account account = getTestAccount(1L, Currency.USD, 100D);
        create(account);
        Response response = create(account);
        ResponseInfo responseInfo = parseResponse(response);
        assertThat(responseInfo).isEqualTo(ResponseInfo.create("Account with same id already exists", DUPLICATE_ACCOUNT));
        delete(account);
    }

    @Test
    public void getAccountWhenItIsSuccessfullyCreated(){
        final Account account = getTestAccount(1L, Currency.USD, 100D);
        create(account);
        final Response response = get(account);
        final ResponseInfo<Account> accountResponseInfo = parseResponseData(response);
        assertThat(accountResponseInfo.getData()).isEqualTo(account);
        delete(account);
    }

    @Test
    public void notGetAccountWithInvalidId(){
        final Account account = getTestAccount(-1L, Currency.USD, 100D);
        final Response response = get(account);
        final ResponseInfo responseInfo = parseResponse(response);
        assertThat(responseInfo).isEqualTo(ResponseInfo.create("Account does not exist", NOT_FOUND));
    }

    @Test
    public void deleteAnExistingAccount(){
        Account account = getTestAccount(1L, Currency.USD, 100D);
        create(account);
        Response response = delete(account);;
        ResponseInfo responseInfo = parseResponse(response);
        assertThat(responseInfo).isEqualTo(ResponseInfo.create("Successfully deleted", SUCCESS));
    }

    @Test
    public void successfullyTransferMoneyInAccountsWithSameCurrency(){
        final Account first = getTestAccount(1L, Currency.USD, 100D);
        final Account second = getTestAccount(2L, Currency.USD, 100D);

        create(first);
        create(second);
        final Response response = transfer(first, second);
        ResponseInfo responseInfo = parseResponse(response);
        assertThat(responseInfo).isEqualTo(ResponseInfo.create("Request executed", SUCCESS));

        final Response first_actual = get(first);
        final ResponseInfo<Account> firstAccountResponse = parseResponseData(first_actual);
        final Response second_actual = get(second);
        final ResponseInfo<Account> secondAccountResponse = parseResponseData(second_actual);

        Assert.assertEquals(firstAccountResponse.getData().getAmountBalance(), 50D, 0);
        Assert.assertEquals(secondAccountResponse.getData().getAmountBalance(), 150D, 0);
        delete(first);
        delete(second);
    }

    @Test
    public void successfullyTransferMoneyInAccountsWithDifferentCurrency(){
        final Account first = getTestAccount(1L, Currency.USD, 100D);
        final Account second = getTestAccount(2L, Currency.INR, 100D);

        create(first);
        create(second);
        final Response response = transfer(first, second);
        ResponseInfo responseInfo = parseResponse(response);
        assertThat(responseInfo).isEqualTo(ResponseInfo.create("Request executed", SUCCESS));

        final Response first_actual = get(first);
        final ResponseInfo<Account> firstAccountResponse = parseResponseData(first_actual);
        final Response second_actual = get(second);
        final ResponseInfo<Account> secondAccountResponse = parseResponseData(second_actual);

        Assert.assertEquals(firstAccountResponse.getData().getAmountBalance(), 50D, 0);
        Assert.assertEquals(secondAccountResponse.getData().getAmountBalance(), 3651.5D, 0);

        delete(first);
        delete(second);
    }

    @Test
    public void notTransferMoneyInAccountsWithInsufficientBalance(){
        final Account first = getTestAccount(1L, Currency.USD, 0D);
        final Account second = getTestAccount(2L, Currency.USD, 100D);

        create(first);
        create(second);
        final Response response = transfer(first, second);
        ResponseInfo responseInfo = parseResponse(response);
        assertThat(responseInfo).isEqualTo(ResponseInfo.create("Insufficient balance at source", INSUFFICIENT_BALANCE));

        delete(first);
        delete(second);
    }

    @Test
    public void notTransferMoneyInSameAccount(){
        final Account first = getTestAccount(1L, Currency.USD, 0D);

        create(first);
        final Response response = transfer(first, first);
        ResponseInfo responseInfo = parseResponse(response);
        assertThat(responseInfo).isEqualTo(ResponseInfo.create("Money cannot be transferred within same account",
                SAME_ACCOUNT));

        delete(first);
    }

    private Response transfer(Account first, Account second) {

        final TransferRequest transferRequest = new TransferRequestBuilder().from(first.getAccountNumber())
                .to(second.getAccountNumber())
                .transfer(50D)
                .build();

        return restEndpoint.target(URI.create(baseUrl + "-transfer"))
                .request()
                .post(Entity.json(Mapper.toJson(transferRequest)));
    }

    private Account getTestAccount(long id,
                                   Currency currency,
                                   double initialBalance) {
        return new AccountBuilder()
                .newAccountWithId(id)
                .withName("account" + id)
                .withBaseCurrency(currency)
                .andInitialBalance(initialBalance)
                .build();
    }

    private Response create(Account account) {
        return restEndpoint.target(endPoint)
                .request()
                .post(Entity.json(Mapper.toJson(account)));
    }

    private Response get(Account account) {
        return restEndpoint.target(URI.create(baseUrl + "/" + account.getAccountNumber()))
                .request()
                .get();
    }

    private Response delete(Account account) {
        return restEndpoint.target(URI.create(baseUrl + "/" + account.getAccountNumber()))
                .request()
                .delete();
    }

    private  ResponseInfo parseResponse(Response response){
        String json = response.readEntity(String.class);
        return Mapper.fromJson(json, new TypeReference<ResponseInfo>() {});
    }

    private ResponseInfo<Account> parseResponseData(Response response){
        String json = response.readEntity(String.class);
        return Mapper.fromJson(json, new TypeReference<ResponseInfo<Account>>() {});
    }
}

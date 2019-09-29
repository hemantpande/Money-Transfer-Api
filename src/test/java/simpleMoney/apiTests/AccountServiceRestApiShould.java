package simpleMoney.apiTests;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.AllArgsConstructor;
import org.junit.*;
import simpleMoney.Application;
import simpleMoney.builders.AccountBuilder;
import simpleMoney.library.Mapper;
import simpleMoney.library.ResponseInfo;
import simpleMoney.models.Account;
import simpleMoney.models.Currencies;
import spark.Spark;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;
import static simpleMoney.library.ResponseCode.PARSER_ERROR;
import static simpleMoney.library.ResponseCode.SUCCESS;

public class AccountServiceRestApiShould {

    private Client restEndpoint;
    private final URI endPoint = URI.create("http://localhost:8080/account");

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
        Account account = getTestAccount();
        Response response = create(account);
        ResponseInfo responseInfo = parseResponse(response);
        assertThat(responseInfo).isEqualTo(ResponseInfo.create("Successfully created", SUCCESS));
    }

    @Test
    public void notCreateAccountWhenRequestIsNotAsExpected(){
        Response response = restEndpoint.target(endPoint)
                .request()
                .post(Entity.json("{\"id\":1,\"account\":[{\"amount\":100,\"currency\":\"RUB\"}]}"));
        ResponseInfo responseInfo = parseResponse(response);
        assertThat(responseInfo).isEqualTo(ResponseInfo.create("Can't parse from JSON", PARSER_ERROR));
    }

    private Account getTestAccount() {
        return new AccountBuilder()
                .newAccountWithId(1L)
                .withName("account1")
                .withBaseCurrency(Currencies.USD)
                .andInitialBalance(100)
                .build();
    }

    private Response create(Account account) {
        return restEndpoint.target(endPoint)
                .request()
                .post(Entity.json(Mapper.toJson(account)));
    }

    private  ResponseInfo parseResponse(Response response){
        String json = response.readEntity(String.class);
        return Mapper.fromJson(json, new TypeReference<ResponseInfo>() {});
    }
}

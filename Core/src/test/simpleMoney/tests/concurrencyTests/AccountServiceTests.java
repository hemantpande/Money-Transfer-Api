package simpleMoney.tests.concurrencyTests;

import org.junit.Test;
import simpleMoney.builders.AccountBuilder;
import simpleMoney.builders.TransferRequestBuilder;
import simpleMoney.library.ResponseCode;
import simpleMoney.models.Account;
import simpleMoney.models.Currencies;
import simpleMoney.models.TransferRequest;
import simpleMoney.services.AccountService;

import java.time.Duration;
import java.time.LocalDateTime;

public class AccountServiceTests {

    private final int maxNumberOfTransactions = 1000000;
    private final AccountService accountService = new AccountService();

    @Test
    public void MeasureTimeTakenWhenTransactionsAreExecutedSequentially() {

        final LocalDateTime startTime = LocalDateTime.now();
        for (int current = 0; current < maxNumberOfTransactions; current++) {

            final int firstAccountId = current;
            createAccountWith(firstAccountId);

            final int secondAccountId = maxNumberOfTransactions + current;
            createAccountWith(secondAccountId);

            TransferRequest request = getTransferRequestFor(firstAccountId, secondAccountId);
            System.out.println("Attempting to transfer $50 dollars from account " + firstAccountId
                    + " to " + secondAccountId);

            ResponseCode response = accountService.transfer(request);
            System.out.println("Response-" + response);
        }

        System.out.println("Processed " + maxNumberOfTransactions + " transactions completed in "
                + Duration.between(LocalDateTime.now(), startTime));
    }

    @Test
    public void MeasureTimeTakenWhenTransactionsAreExecutedParallelly() {

    }

    private TransferRequest getTransferRequestFor(int firstAccountId, int secondAccountId) {
        return new TransferRequestBuilder()
                        .transfer(100D)
                        .from(Long.valueOf(firstAccountId))
                        .to(Long.valueOf(secondAccountId))
                        .build();
    }

    private void createAccountWith(int id) {
        Account secondAccount = new AccountBuilder()
                .newAccountWithId(Long.valueOf(id))
                .withName("account-" + id)
                .withBaseCurrency(Currencies.USD)
                .andInitialBalance(100D)
                .build();
        accountService.create(secondAccount);
    }
}

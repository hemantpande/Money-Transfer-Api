package simpleMoney.concurrencyTests;

import org.junit.Assert;
import org.junit.Test;
import simpleMoney.builders.*;
import simpleMoney.library.ResponseCode;
import simpleMoney.library.TransferTask;
import simpleMoney.models.Account;
import simpleMoney.models.Currencies;
import simpleMoney.models.TransferRequest;
import simpleMoney.services.AccountService;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class AccountServiceTests {

    private final int maxNumberOfTransactions = 10000;
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

            ResponseCode response = accountService.transfer(request);
            Assert.assertEquals(ResponseCode.SUCCESS, response);
        }

        System.out.println("Processed " + maxNumberOfTransactions + " transactions sequentially in "
                + Duration.between(LocalDateTime.now(), startTime));
    }

    @Test
    public void MeasureTimeTakenWhenTransactionsAreExecutedParallelly() {
        final ExecutorService executor = Executors.newFixedThreadPool(10);
        final LocalDateTime startTime = LocalDateTime.now();
        try{
            for (int current = 0; current < maxNumberOfTransactions; current++) {

                final int firstAccountId = current;
                createAccountWith(firstAccountId);

                final int secondAccountId = maxNumberOfTransactions + current;
                createAccountWith(secondAccountId);

                TransferRequest request = getTransferRequestFor(firstAccountId, secondAccountId);
                final TransferTask transferTask = new TransferTask(request, accountService::transfer);
                Future<ResponseCode> response = executor.submit(transferTask);
                Assert.assertEquals(ResponseCode.SUCCESS, response.get());
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }

        System.out.println("Processed " + maxNumberOfTransactions + " transactions in parallel in "
                + Duration.between(LocalDateTime.now(), startTime));
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

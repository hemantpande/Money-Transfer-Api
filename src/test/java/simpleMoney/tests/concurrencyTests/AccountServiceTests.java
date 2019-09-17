package java.simpleMoney.tests.concurrencyTests;

import org.junit.Test;
import java.simpleMoney.builders.AccountBuilder;
import java.simpleMoney.builders.TransferRequestBuilder;
import java.simpleMoney.library.ResponseCode;
import java.simpleMoney.library.TransferTask;
import java.simpleMoney.models.Account;
import java.simpleMoney.models.Currencies;
import java.simpleMoney.models.TransferRequest;
import java.simpleMoney.services.AccountService;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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
                System.out.println("Attempting to transfer $50 dollars from account " + firstAccountId
                        + " to " + secondAccountId);

                System.out.println("Response-" + response.get());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }finally {
            executor.shutdown();
        }

        System.out.println("Processed " + maxNumberOfTransactions + " transactions completed in "
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

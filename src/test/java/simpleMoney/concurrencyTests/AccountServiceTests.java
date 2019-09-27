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
import java.util.concurrent.*;

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
        try {
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

    @Test
    public void DeadlockTest() {
        System.out.println("**This test will check if the application is Thread-safe and deadlock free**");

        final long firstAccountId = 1L;
        final long secondAccountId = 2L;
        final long thirdAccountId = 3L;

        Thread firstTransaction = getFirstTransactionThread(firstAccountId, secondAccountId);
        Thread secondTransaction = getSecondTransactionThread(firstAccountId, thirdAccountId);

        firstTransaction.start();
        secondTransaction.start();

        sleepForSomeTimeToLetTheThreadsComplete();

        System.out.println("After execution - " + firstTransaction.getName() + " is "
                + firstTransaction.getState());
        System.out.println("After execution - " + secondTransaction.getName() + " is "
                + secondTransaction.getState());

        assertAccountBalanceFor(firstAccountId, secondAccountId, thirdAccountId);
    }

    private void assertAccountBalanceFor(long firstAccountId, long secondAccountId, long thirdAccountId) {
        final Account firstAccount = accountService.getById(firstAccountId);
        Assert.assertEquals(firstAccount.getBalance(), 800L, 0);

        final Account secondAccount = accountService.getById(secondAccountId);
        Assert.assertEquals(secondAccount.getBalance(), 1100L, 0);

        final Account thirdAccount = accountService.getById(thirdAccountId);
        Assert.assertEquals(thirdAccount.getBalance(), 1100L, 0);
    }

    private Thread getSecondTransactionThread(long firstAccountId, long thirdAccountId) {
        return new Thread(() -> {
                final long begin = System.nanoTime();
                createAccountWith(thirdAccountId);

                final TransferRequest request = getTransferRequestFor(firstAccountId, thirdAccountId);
                System.out.println("Thread name with " + Thread.currentThread().getName() + " is in "
                        + Thread.currentThread().getState() + " state.");
                System.out.println(String.format("Transfer request received from account %s to %s",
                        request.getFromId(), request.getToId()));

                accountService.transfer(request);

                System.out.println("Exiting from 2st transaction in "
                        + TimeUnit.MILLISECONDS.convert(System.nanoTime() - begin, TimeUnit.NANOSECONDS)
                        + " milliseconds");
            });
    }

    private Thread getFirstTransactionThread(long firstAccountId, long secondAccountId) {
        return new Thread(() -> {
                final long begin = System.nanoTime();
                createAccountWith(firstAccountId);
                createAccountWith(secondAccountId);

                final TransferRequest request = getTransferRequestFor(firstAccountId, secondAccountId);
                System.out.println("Thread name with " + Thread.currentThread().getName() + " is in "
                        + Thread.currentThread().getState() + " state.");

                System.out.println(String.format("Transfer request received from account %s to %s",
                        request.getFromId(), request.getToId()));
                accountService.transfer(request);

                System.out.println("Exiting from 1st transaction in "
                        + TimeUnit.MILLISECONDS.convert(System.nanoTime() - begin, TimeUnit.NANOSECONDS)
                        + " milliseconds");
            });
    }

    private void sleepForSomeTimeToLetTheThreadsComplete() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private TransferRequest getTransferRequestFor(long firstAccountId, long secondAccountId) {
        return new TransferRequestBuilder()
                .transfer(100D)
                .from(firstAccountId)
                .to(secondAccountId)
                .build();
    }

    private void createAccountWith(long id) {
        Account secondAccount = new AccountBuilder()
                .newAccountWithId(id)
                .withName("account-" + id)
                .withBaseCurrency(Currencies.USD)
                .andInitialBalance(1000D)
                .build();
        accountService.create(secondAccount);
    }
}

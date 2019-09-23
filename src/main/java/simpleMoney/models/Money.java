package simpleMoney.models;

import lombok.*;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Money {

    private double amount;

    @Getter @Setter
    private Currencies baseCurrency;

    private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private Lock readLock = readWriteLock.readLock();
    private Lock writeLock = readWriteLock.writeLock();

    public double getExchangeRate(Currencies sourceCurrencyForConversion) {

        /*this can be retrieved through an ExchangeRateAPI, having it hard-coded for simplicity sake, right now*/
        String exchangeRateMapping = sourceCurrencyForConversion.toString() + "_to_" + baseCurrency.toString();
        ExchangeRates exchangeRate = ExchangeRates.valueOf(exchangeRateMapping);

        return exchangeRate.getExchangeRate();
    }

    public double getAmount() {
        try{ // this block can never throw an exception
            readLock.lock();
            return amount;
        }finally {
            readLock.unlock();
        }
    }

    public void update(double updatedBalance) {
        try{ // this block can never throw an exception
            writeLock.lock();
            this.amount = updatedBalance;
        }finally {
            writeLock.unlock();
        }
    }
}

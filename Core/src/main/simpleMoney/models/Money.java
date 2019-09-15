package simpleMoney.models;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Data
public class Money {

    @Getter @Setter
    private double balance;

    @Getter @Setter
    private Currencies baseCurrency;

    public double getExchangeRate(Currencies sourceCurrencyForConversion) {

        /*this can be retrived through an ExchangeRateAPI, having it hard-coded for simplicity sake, right now*/
        String exchangeRateMapping = sourceCurrencyForConversion.toString() + "_to_" + baseCurrency.toString();
        ExchangeRates exchangeRate = ExchangeRates.valueOf(exchangeRateMapping);

        return exchangeRate.getExchangeRate();
    }

    public void update(double updatedBalance) {
        this.balance = updatedBalance;
    }
}

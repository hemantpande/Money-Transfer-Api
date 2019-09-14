package simpleMoney.models;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class Money {

    @Getter @Setter
    private double balance;

    @Getter @Setter
    private Currencies baseCurrency;

    public void debit(double amount) {
        balance -= amount;
    }

    public void credit(double amount, Currencies sourceCurrencyForConversion) {
        double rate = getExchangeRate(sourceCurrencyForConversion);
        balance += rate * amount;
    }

    private double getExchangeRate(Currencies sourceCurrencyForConversion) {

        /*this can be retrived through an ExchangeRateAPI, having it hard-coded for simplicity sake, right now*/
        String exchangeRateMapping = sourceCurrencyForConversion.toString() + "_to_" + baseCurrency.toString();
        ExchangeRates exchangeRate = ExchangeRates.valueOf(exchangeRateMapping);

        return exchangeRate.getExchangeRate();
    }
}

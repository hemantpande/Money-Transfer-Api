package simpleMoney.models;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Money {

    @Getter @Setter
    private double amount;

    @Getter @Setter
    private Currencies baseCurrency;

    public double getExchangeRate(Currencies sourceCurrencyForConversion) {

        /*this can be retrieved through an ExchangeRateAPI, having it hard-coded for simplicity sake, right now*/
        String exchangeRateMapping = sourceCurrencyForConversion.toString() + "_to_" + baseCurrency.toString();
        ExchangeRates exchangeRate = ExchangeRates.valueOf(exchangeRateMapping);

        return exchangeRate.getExchangeRate();
    }

    public void update(double updatedBalance) {
        this.amount = updatedBalance;
    }
}

package simpleMoney.models;

public enum ExchangeRates {

    /* exchange rates as on 15th Sep 2019*/

    USD_to_USD(Currencies.USD, Currencies.USD, 1),
    USD_to_INR(Currencies.USD, Currencies.INR, 71.03),
    INR_to_USD(Currencies.INR, Currencies.USD, 0.014);

    private final Currencies sourceCurrency;
    private final Currencies destinationCurrency;
    private final double exchangeRate;

    ExchangeRates(Currencies sourceCurrency, Currencies destinationCurrency, double exchangeRate) {
        this.sourceCurrency = sourceCurrency;
        this.destinationCurrency = destinationCurrency;
        this.exchangeRate = exchangeRate;
    }

    public double getExchangeRate(){
        return exchangeRate;
    }

}

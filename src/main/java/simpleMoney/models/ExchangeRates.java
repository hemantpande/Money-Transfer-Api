package simpleMoney.models;

public enum ExchangeRates {

    /* exchange rates as on 15th Sep 2019*/

    USD_to_USD(Currency.USD, Currency.USD, 1),
    USD_to_INR(Currency.USD, Currency.INR, 71.03),
    INR_to_USD(Currency.INR, Currency.USD, 0.014);

    private final Currency sourceCurrency;
    private final Currency destinationCurrency;
    private final double exchangeRate;

    ExchangeRates(Currency sourceCurrency, Currency destinationCurrency, double exchangeRate) {
        this.sourceCurrency = sourceCurrency;
        this.destinationCurrency = destinationCurrency;
        this.exchangeRate = exchangeRate;
    }

    public double getExchangeRate(){
        return exchangeRate;
    }

}

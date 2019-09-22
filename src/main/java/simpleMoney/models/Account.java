package simpleMoney.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@AllArgsConstructor
public class Account {

    @Getter
    private final Long accountNumber;

    @Getter @Setter
    private String holderName;

    @Setter
    private Money balance;

    public Currencies getBaseCurrency(){
        return balance.getBaseCurrency();
    }

    public double getBalance(){
        return balance.getAmount();
    }

    @JsonCreator
    public Account(Long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void debit(double amount) {
        final double currentBalance = getBalance();
        final double debitedBalance = currentBalance - amount;
        balance.update(debitedBalance);
    }

    public void credit(double amount, Currencies sourceCurrencyForConversion) {

        final double currentBalance = getBalance();
        final double rate = balance.getExchangeRate(sourceCurrencyForConversion);
        final double creditedBalance = currentBalance + rate * amount;

        balance.update(creditedBalance);
    }
}

package simpleMoney.models;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
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
        return balance.getBalance();
    }

    public Account(Long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void debit(double amount) {
        balance.debit(amount);
    }

    public void credit(double amount, Currencies sourceCurrencyForConversion) {
        balance.credit(amount, sourceCurrencyForConversion);
    }
}

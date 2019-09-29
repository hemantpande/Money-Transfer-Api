package simpleMoney.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import simpleMoney.library.ResponseCode;
import simpleMoney.library.exceptions.InsufficientBalanceException;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Data
@AllArgsConstructor
public class Account {

    @Getter
    private final Long accountNumber;

    @Getter @Setter
    private String holderName;

    @Setter @Getter
    private Money balance;

    @JsonIgnore
    public Currency getBaseCurrency(){
        return balance.getBaseCurrency();
    }

    @JsonIgnore
    public double getAmountBalance(){
        return balance.getAmount();
    }

    @JsonCreator
    public Account(Long accountNumber) {
        this.accountNumber = accountNumber;
    }

    @JsonIgnore @Setter
    private Lock lock;

    public Lock getLock(){
        if(lock == null){
            return new ReentrantLock(true);
        }
        return lock;
    }

    public void debit(double amount) {
        final double currentBalance = getAmountBalance();
        final double debitedBalance = currentBalance - amount;

        if(debitedBalance < 0){
            throw new InsufficientBalanceException(ResponseCode.INSUFFICIENT_BALANCE,
                    "Source account does not have enough balance");
        }

        balance.update(debitedBalance);
    }

    public void credit(double amount, Currency sourceCurrencyForConversion) {

        final double currentBalance = getAmountBalance();
        final double rate = balance.getExchangeRate(sourceCurrencyForConversion);
        final double creditedBalance = currentBalance + rate * amount;

        balance.update(creditedBalance);
    }

    @Override
    public boolean equals(Object value) {
        if (value == this) {
            return true;
        }

        if (!(value instanceof Account)) {
            return false;
        }

        Account valueToCompare = (Account) value;
        return accountNumber.equals(valueToCompare.accountNumber);
    }
}

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

    @Getter @Setter
    private double initialBalance;

    @Getter @Setter
    private Currencies baseCurrency;

    public Account(Long accountNumber) {
        this.accountNumber = accountNumber;
    }
}

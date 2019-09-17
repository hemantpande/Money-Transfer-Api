package java.simpleMoney.models;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class TransferRequest {
    private Long fromId;
    private Long toId;
    private double amount;
}

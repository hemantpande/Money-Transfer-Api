package simpleMoney.models;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransferRequest {
    private Long fromId;
    private Long toId;
    private double amount;
}

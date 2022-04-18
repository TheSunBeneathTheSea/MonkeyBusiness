package monkey.domain.trading;

import lombok.Data;

@Data
public class TradeOrderRequestDto {
    private Long competitionId;

    private boolean buying;

    private String ticker;

    private int amount;
}

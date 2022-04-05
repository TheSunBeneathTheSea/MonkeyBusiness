package monkey.domain.trading;

import lombok.Data;

@Data
public class TradeRequestVO {
    private String userId;

    private boolean buying;

    private String ticker;

    private int amount;
}

package monkey.domain.trading;

import lombok.Builder;
import lombok.Data;

@Data
public class TradingStrategy {
    private short takeProfitPoint;

    private short stopLossPoint;

    @Builder
    public TradingStrategy(short takeProfitPoint, short stopLossPoint) {
        this.takeProfitPoint = takeProfitPoint;
        this.stopLossPoint = stopLossPoint;
    }
}

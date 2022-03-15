package monkey.domain.trading;

import lombok.Data;

@Data
public class TradingDataSaveRequestDto {
    private Long userId;
    private short takeProfitPoint;
    private short stopLossPoint;
}

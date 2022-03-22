package monkey.domain.trading;

import lombok.Data;

@Data
public class AccountSaveRequestDto {
    private String user_id;
    private short takeProfitPoint;
    private short stopLossPoint;
}

package monkey.domain.trading;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class StockUpdateDto {
    private String ticker;

    private String companyName;

    private int currentPrice;
}

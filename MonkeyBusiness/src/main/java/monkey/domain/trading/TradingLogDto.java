package monkey.domain.trading;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TradingLogDto {
    private boolean isBuying;

    private String ticker;

    private int buyingPrice;

    private int sellingPrice;

    private int amount;

    @Builder
    public TradingLogDto(int buyingPrice, int sellingPrice, boolean isBuying, String ticker, int amount) {
        this.isBuying = isBuying;
        this.ticker = ticker;
        this.buyingPrice = buyingPrice;
        this.sellingPrice = sellingPrice;
        this.amount = amount;
    }
}

package monkey.domain.trading;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class StockInfo {
    @Id
    private String ticker;

    private String companyName;

    private int openPrice;

    private int currentPrice;

    @OneToMany(mappedBy = "stockInfo", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<TradingData> tradingDataList;

    @Builder
    public StockInfo(String ticker, String companyName, int currentPrice){
        this.ticker = ticker;
        this.companyName = companyName;
        this.currentPrice = currentPrice;
    }

    public StockInfo(StockUpdateDto updateDto) {
        this.ticker = updateDto.getTicker();
        this.companyName = updateDto.getCompanyName();
        this.currentPrice = updateDto.getCurrentPrice();
    }

    public void updateOpenPrice(int price) {
        this.openPrice = price;
        this.currentPrice = price;
    }

    public void updateCurrentPrice(StockUpdateDto updateDto) {
        this.currentPrice = updateDto.getCurrentPrice();
    }
}

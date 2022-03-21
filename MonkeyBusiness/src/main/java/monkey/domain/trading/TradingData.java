package monkey.domain.trading;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class TradingData {
    @Id
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_info_ticker")
    private StockInfo stockInfo;

    private int buyingPrice;

    private short takeProfitPoint;

    private short stopLossPoint;

    private int holdingAmount;

    private Long cash;

    @Builder
    public TradingData(Long id, StockInfo stockInfo, TradingStrategy strategy) {
        this.id = id;
        this.stockInfo = stockInfo;
        this.buyingPrice = 0;
        this.takeProfitPoint = strategy.getTakeProfitPoint();
        this.stopLossPoint = strategy.getStopLossPoint();
        this.holdingAmount = 0;
        this.cash = 1000000L;
    }

    public void updateStrategy(TradingStrategy strategy) {
        this.takeProfitPoint = strategy.getTakeProfitPoint();
        this.stopLossPoint = strategy.getStopLossPoint();
    }

    public boolean makeDecision() {
        if (holdingAmount <= 0) {
            return false;
        }
        double movement = (((double)stockInfo.getCurrentPrice() / this.buyingPrice) * 100) - 100;

        return movement <= (-1 * this.getStopLossPoint()) || movement >= this.getTakeProfitPoint();
    }

    public TradingLogDto sellingStocks() {
        TradingLogDto newLogDto = TradingLogDto.builder()
                .amount(this.holdingAmount)
                .isBuying(false)
                .ticker(this.stockInfo.getTicker())
                .buyingPrice(getBuyingPrice())
                .sellingPrice(this.stockInfo.getCurrentPrice())
                .build();

        this.cash += (long) holdingAmount * this.stockInfo.getCurrentPrice();
        this.holdingAmount = 0;

        return newLogDto;
    }

    public TradingLogDto buyingStocks(StockInfo stockInfo) {
        this.stockInfo = stockInfo;
        this.buyingPrice = stockInfo.getCurrentPrice();
        this.holdingAmount = (int)(this.cash / this.buyingPrice);
        this.cash -= (long) holdingAmount * this.buyingPrice;

        TradingLogDto newLogDto = TradingLogDto.builder()
                .amount(this.holdingAmount)
                .isBuying(true)
                .ticker(stockInfo.getTicker())
                .buyingPrice(this.buyingPrice)
                .sellingPrice(0)
                .build();

        return newLogDto;
    }
}

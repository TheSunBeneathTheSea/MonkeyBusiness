package monkey.domain.trading;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Optional;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "`Account`")
public class Account {
    @Column(columnDefinition = "char(36)")
    @Id
    private String user_id;

    @Column(unique = true, columnDefinition = "not null auto_increment")
    private Long id;

    @Column(columnDefinition = "default 10000")
    private Long points;

    private int buyingPrice;

    private int takeProfitPoint;

    private int stopLossPoint;

    private int holdingAmount;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_info_ticker")
    private StockInfo stockInfo;

    @Builder
    public Account(String user_id, TradingStrategy strategy) {
        this.user_id = user_id;
        this.buyingPrice = 0;
        this.takeProfitPoint = strategy.getTakeProfitPoint();
        this.stopLossPoint = strategy.getStopLossPoint();
        this.holdingAmount = 0;
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

        this.points += (long) holdingAmount * this.stockInfo.getCurrentPrice();
        this.holdingAmount = 0;

        return newLogDto;
    }

    public boolean canBuy(StockInfo stockInfo){
        return this.points >= stockInfo.getCurrentPrice();
    }

    public Optional<TradingLogDto> buyingStocks(StockInfo stockInfo) throws Exception {
        if(!canBuy(stockInfo)){
            return null;
        }
        this.stockInfo = stockInfo;
        this.buyingPrice = stockInfo.getCurrentPrice();
        this.holdingAmount = (int)(this.points / this.buyingPrice);
        this.points -= (long) holdingAmount * this.buyingPrice;

        TradingLogDto newLogDto = TradingLogDto.builder()
                .amount(this.holdingAmount)
                .isBuying(true)
                .ticker(stockInfo.getTicker())
                .buyingPrice(this.buyingPrice)
                .sellingPrice(0)
                .build();

        return Optional.of(newLogDto);
    }
}

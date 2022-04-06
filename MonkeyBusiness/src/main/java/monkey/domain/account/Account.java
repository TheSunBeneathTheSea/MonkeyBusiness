package monkey.domain.account;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import monkey.domain.trading.StockInfo;
import monkey.domain.trading.TradeRequestDto;
import monkey.domain.trading.TradingLogDto;

import javax.persistence.*;
import java.util.Set;

@Getter
@NoArgsConstructor
@Entity
public class Account {
    @Id
    private String user_id;

    private String nickname;

    private Long points;

    @OneToMany(mappedBy = "owner")
    private Set<Portfolio> holdingStocks;

    @Builder
    public Account(String user_id, String nickname) {
        this.user_id = user_id;
        this.nickname = nickname;
        this.points = 1000000L;
    }

    public TradingLogDto sellingStocks(TradeRequestDto requestDto) throws NullPointerException {
        Portfolio portfolio = requestDto.getPortfolio();
        StockInfo stock = portfolio.getStockInfo();
        int sellingAmount = Math.min(requestDto.getAmount(), portfolio.getAmount());

        TradingLogDto newLogDto = TradingLogDto.builder()
                .amount(sellingAmount)
                .isBuying(requestDto.isBuying())
                .ticker(stock.getTicker())
                .companyName(stock.getCompanyName())
                .buyingPrice(portfolio.getBuyingPrice())
                .sellingPrice(stock.getCurrentPrice())
                .build();

        Long tradePoints = (long) sellingAmount * stock.getCurrentPrice();
        this.points += tradePoints;

        portfolio.trade(false, sellingAmount);

        return newLogDto;
    }

    public boolean canBuy(StockInfo stockInfo){
        return this.points >= stockInfo.getCurrentPrice();
    }

    public TradingLogDto buyingStocks(TradeRequestDto requestDto) {
        Portfolio portfolio = requestDto.getPortfolio();
        StockInfo stockInfo = portfolio.getStockInfo();
        int buyingAmount = Math.min(requestDto.getAmount(), (int) (this.points / stockInfo.getCurrentPrice()));

        TradingLogDto newLogDto = TradingLogDto.builder()
                .amount(buyingAmount)
                .isBuying(true)
                .ticker(stockInfo.getTicker())
                .companyName(stockInfo.getCompanyName())
                .buyingPrice(stockInfo.getCurrentPrice())
                .sellingPrice(0)
                .build();

        portfolio.setBuyingPrice(stockInfo.getCurrentPrice(), buyingAmount);
        portfolio.trade(true, buyingAmount);
        Long tradePoints = (long) buyingAmount * portfolio.getStockInfo().getCurrentPrice();
        this.points -= tradePoints;

        return newLogDto;
    }
}

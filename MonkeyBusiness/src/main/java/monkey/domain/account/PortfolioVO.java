package monkey.domain.account;

import lombok.Data;
import monkey.domain.trading.StockInfoVO;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class PortfolioVO {
    private Long id;

    private StockInfoVO stockInfo;

    private AccountVO owner;

    private int amount;

    private Long value;

    private int buyingPrice;

    private Long profit;

    public PortfolioVO(Portfolio portfolio) {
        this.id = portfolio.getId();
        this.stockInfo = new StockInfoVO(portfolio.getStockInfo());
        this.owner = new AccountVO(portfolio.getAccount());
        this.amount = portfolio.getAmount();
        this.value = portfolio.calculateValue();
        this.buyingPrice = portfolio.getBuyingPrice();
        this.profit = portfolio.calculateProfit();
    }

    public static List<PortfolioVO> transformList(List<Portfolio> Portfolios) {
        return Portfolios.stream().map(p -> new PortfolioVO(p)).collect(Collectors.toList());
    }
}

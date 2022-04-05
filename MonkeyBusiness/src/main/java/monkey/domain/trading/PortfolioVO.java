package monkey.domain.trading;

import lombok.Data;

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
        this.owner = new AccountVO(portfolio.getOwner());
        this.amount = portfolio.getAmount();
        this.value = portfolio.calculateValue();
        this.buyingPrice = portfolio.getBuyingPrice();
        this.profit = portfolio.calculateProfit();
    }
}

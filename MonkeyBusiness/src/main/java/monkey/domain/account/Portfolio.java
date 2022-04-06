package monkey.domain.account;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import monkey.domain.trading.StockInfo;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Portfolio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_info_ticker")
    private StockInfo stockInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_user_id")
    private Account owner;

    private int amount;

    private int buyingPrice;

    @Builder
    public Portfolio(StockInfo stockInfo, Account account, int amount, int buyingPrice) {
        this.stockInfo = stockInfo;
        this.owner = account;
        this.amount = amount;
        this.buyingPrice = buyingPrice;
    }

    public void setBuyingPrice(int newPrice, int newAmount) {
        if (this.buyingPrice == 0) {
            this.buyingPrice = newPrice;
        } else {
            this.buyingPrice = (this.buyingPrice * amount + newPrice * newAmount) / (amount + newAmount);
        }
    }

    public void trade(boolean isBuying, int amount) {
        if (isBuying) {
            this.amount += amount;
        } else {
            this.amount -= amount;
        }
    }

    public Long calculateValue() {
        return ((long)this.getAmount() * this.getStockInfo().getCurrentPrice());
    }

    public Long calculateProfit() {
        return ((long)this.getStockInfo().getCurrentPrice() - this.getBuyingPrice()) * this.getAmount();
    }
}

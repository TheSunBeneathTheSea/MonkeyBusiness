package monkey.domain.trading;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import monkey.domain.account.Portfolio;

import javax.persistence.*;
import java.util.Set;

@Getter
@NoArgsConstructor
@Entity
public class StockInfo {
    @Id
    private String ticker;

    private String companyName;

    private int openPrice;

    private int currentPrice;

    @OneToMany(mappedBy = "stockInfo", fetch = FetchType.LAZY)
    private Set<Portfolio> accountList;

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

    public void updateOpenPrice() {
        this.openPrice = this.currentPrice;
    }

    public void updateCurrentPrice(int price) {
        this.currentPrice = price;
    }
}

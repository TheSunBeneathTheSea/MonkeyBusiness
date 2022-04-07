package monkey.domain.trading;

import lombok.Getter;
import lombok.NoArgsConstructor;
import monkey.domain.account.Account;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
public class TradingLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "account_user_id", referencedColumnName = "userId"),
            @JoinColumn(name = "account_competition_id", referencedColumnName = "competitionId")
    })
    private Account account;

    @Column(nullable = false)
    private boolean isBuying;

    private String ticker;

    private String companyName;

    private int buyingPrice;

    private int sellingPrice;

    private int amount;

    private Long profit;

    private LocalDateTime createdTime;

    public TradingLog(Account account, TradingLogDto logDto) {
        this.account = account;
        this.isBuying = logDto.isBuying();
        this.ticker = logDto.getTicker();
        this.companyName = logDto.getCompanyName();
        this.buyingPrice = logDto.getBuyingPrice();
        this.sellingPrice = logDto.getSellingPrice();
        this.amount = logDto.getAmount();
        this.profit = logDto.isBuying() ? 0 : ((long)logDto.getSellingPrice() - logDto.getBuyingPrice()) * logDto.getAmount();
        this.createdTime = LocalDateTime.now();
    }
}

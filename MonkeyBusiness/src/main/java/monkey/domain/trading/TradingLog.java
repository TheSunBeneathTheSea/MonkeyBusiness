package monkey.domain.trading;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import monkey.domain.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
public class TradingLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @Column(nullable = false)
    private boolean isBuying;

    private String ticker;

    private int buyingPrice;

    private int sellingPrice;

    private int amount;

    private Long profit;

    private LocalDateTime createdTime;

    public TradingLog(Long userId, TradingLogDto logDto) {
        this.userId = userId;
        this.isBuying = logDto.isBuying();
        this.ticker = logDto.getTicker();
        this.buyingPrice = logDto.getBuyingPrice();
        this.sellingPrice = logDto.getSellingPrice();
        this.amount = logDto.getAmount();
        this.profit = logDto.isBuying() ? 0 : ((long)logDto.getSellingPrice() - logDto.getBuyingPrice()) * logDto.getAmount();
        this.createdTime = LocalDateTime.now();
    }
}

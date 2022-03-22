package monkey.domain.trading;

import lombok.Data;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class AccountVO {
    private String user_id;
    private String companyName;
    private String ticker;
    private Long id;
    private Long points;
    private Long capital;
    private int currentPrice;
    private int buyingPrice;
    private int takeProfitPoint;
    private int stopLossPoint;
    private int holdingAmount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public AccountVO(Account account) {
        this.user_id = account.getUser_id();
        this.companyName = ObjectUtils.isEmpty(account.getStockInfo()) ? "" : account.getStockInfo().getCompanyName();
        this.ticker = ObjectUtils.isEmpty(account.getStockInfo()) ? "" : account.getStockInfo().getTicker();
        this.id = account.getId();
        this.points = account.getPoints();
        this.currentPrice = ObjectUtils.isEmpty(account.getStockInfo()) ? -1 : account.getStockInfo().getCurrentPrice();
        this.capital = ObjectUtils.isEmpty(account.getStockInfo()) ? 0 : points + account.getHoldingAmount() * account.getStockInfo().getCurrentPrice();
        this.buyingPrice = account.getBuyingPrice();
        this.takeProfitPoint = account.getTakeProfitPoint();
        this.stopLossPoint = account.getStopLossPoint();
        this.holdingAmount = account.getHoldingAmount();
        this.createdAt = account.getCreatedAt();
        this.updatedAt = account.getUpdatedAt();
    }

    public static List<AccountVO> transformList(List<Account> accountList) {
        return accountList.stream().map(account -> new AccountVO(account)).collect(Collectors.toList());
    }
}

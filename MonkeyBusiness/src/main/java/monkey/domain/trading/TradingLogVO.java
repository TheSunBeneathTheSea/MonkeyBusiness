package monkey.domain.trading;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class TradingLogVO {
    private Long id;

    private String userId;

    private Long competitionId;

    private boolean isBuying;

    private String ticker;

    private String companyName;

    private int buyingPrice;

    private int sellingPrice;

    private int amount;

    private Long profit;

    private LocalDateTime createdTime;

    public TradingLogVO (TradingLog tradingLog) {
        this.id = tradingLog.getId();
        this.userId = tradingLog.getAccount().getId().getUserId();
        this.competitionId = tradingLog.getAccount().getId().getCompetitionId();
        this.isBuying = tradingLog.isBuying();
        this.ticker = tradingLog.getTicker();
        this.companyName = tradingLog.getCompanyName();
        this.buyingPrice = tradingLog.getBuyingPrice();
        this.sellingPrice = tradingLog.getSellingPrice();
        this.amount = tradingLog.getAmount();
        this.profit = tradingLog.getProfit();
        this.createdTime = tradingLog.getCreatedTime();
    }
    public static List<TradingLogVO> transformList(List<TradingLog> LogList) {
        return LogList.stream().map(tradingLog -> new TradingLogVO(tradingLog)).collect(Collectors.toList());
    }
}

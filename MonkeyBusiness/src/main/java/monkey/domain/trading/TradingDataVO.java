package monkey.domain.trading;

import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class TradingDataVO {
    private Long id;

    private String ticker;
    private String companyName;
    private int currentPrice;

    private int buyingPrice;

    private short takeProfitPoint;

    private short stopLossPoint;

    private int holdingAmount;

    private Long capital;

    public TradingDataVO(TradingData data) {
        this.id = data.getId();
        this.ticker = data.getStockInfo().getTicker();
        this.companyName = data.getStockInfo().getCompanyName();
        this.currentPrice = data.getStockInfo().getCurrentPrice();
        this.buyingPrice = data.getBuyingPrice();
        this.takeProfitPoint = data.getTakeProfitPoint();
        this.stopLossPoint = data.getStopLossPoint();
        this.holdingAmount = data.getHoldingAmount();
        this.capital = data.getCapital();
    }

    public static List<TradingDataVO> transformList(List<TradingData> dataList) {
        return dataList.stream().map(tradingData -> new TradingDataVO(tradingData)).collect(Collectors.toList());
    }
}

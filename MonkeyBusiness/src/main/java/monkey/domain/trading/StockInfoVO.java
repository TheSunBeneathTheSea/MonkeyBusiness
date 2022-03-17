package monkey.domain.trading;

import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class StockInfoVO {
    private String ticker;
    private String companyName;
    private int openPrice;
    private int currentPrice;

    public StockInfoVO(StockInfo info) {
        this.ticker = info.getTicker();
        this.companyName = info.getCompanyName();
        this.openPrice = info.getOpenPrice();
        this.currentPrice = info.getCurrentPrice();
    }

    public static List<StockInfoVO> transformList(List<StockInfo> dataList) {
        return dataList.stream().map(stockInfo -> new StockInfoVO(stockInfo)).collect(Collectors.toList());
    }
}

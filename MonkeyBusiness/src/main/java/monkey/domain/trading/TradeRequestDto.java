package monkey.domain.trading;

import lombok.Data;
import monkey.domain.account.Portfolio;

@Data
public class TradeRequestDto {
    private Long competitionId;

    private boolean buying;

    private Portfolio portfolio;

    private int amount;

    public TradeRequestDto(TradeOrderRequestDto vo, Portfolio portfolio) {
        this.competitionId = vo.getCompetitionId();
        this.buying = vo.isBuying();
        this.portfolio = portfolio;
        this.amount = vo.getAmount();
    }
}

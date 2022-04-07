package monkey.domain.trading;

import lombok.Data;
import monkey.domain.account.Portfolio;

@Data
public class TradeRequestDto {
    private String userId;

    private Long competitionId;

    private boolean buying;

    private Portfolio portfolio;

    private int amount;

    public TradeRequestDto(TradeRequestVO vo, Portfolio portfolio) {
        this.userId = vo.getUserId();
        this.competitionId = vo.getCompetitionId();
        this.buying = vo.isBuying();
        this.portfolio = portfolio;
        this.amount = vo.getAmount();
    }
}

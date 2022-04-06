package monkey.domain.competition;

import lombok.Data;

@Data
public class ParticipantVO {
    private String accountId;
    private String nickname;
    private String competitionName;
    private Long totalProfit;

    public ParticipantVO(Participant participant) {
        this.accountId = participant.getAccountId();
        this.nickname = participant.getNickname();
        this.competitionName = participant.getCompetition().getName();
        this.totalProfit = participant.getTotalProfit();
    }
}

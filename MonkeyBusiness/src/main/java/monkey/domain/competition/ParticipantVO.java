package monkey.domain.competition;

import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class ParticipantVO {
    private String nickname;
    private Long totalProfit;

    public ParticipantVO(Participant participant) {
        this.nickname = participant.getAccount().getNickname();
        this.totalProfit = participant.getTotalProfit();
    }

    public ParticipantVO(RankingData data) {
        this.nickname = data.getNickname();
        this.totalProfit = data.getTotalProfit();
    }

    public static List<ParticipantVO> transformList(List<Participant> participantList) {
        return participantList.stream().map(participant -> new ParticipantVO(participant)).collect(Collectors.toList());
    }

    public static List<ParticipantVO> transformRankDataList(List<RankingData> rankingDataList) {
        return rankingDataList.stream().map(data -> new ParticipantVO(data)).collect(Collectors.toList());
    }
}

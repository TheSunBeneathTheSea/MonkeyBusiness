package monkey.domain.competition;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class RankingData implements Serializable {
    private String nickname;
    private int rank;
    private Long totalCapital;

    @Builder
    public RankingData(String nickname, int rank, Long totalCapital) {
        this.nickname = nickname;
        this.rank = rank;
        this.totalCapital = totalCapital;
    }

    @Override
    public String toString() {
        return String.format("{\"nickname\":\"%s\",\"rank\":\"%d\",\"totalProfit\":\"%d\"}", nickname, rank, totalCapital);
    }
}

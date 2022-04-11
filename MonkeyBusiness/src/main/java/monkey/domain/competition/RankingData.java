package monkey.domain.competition;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
public class RankingData implements Serializable {
    private String nickname;
    private int rank;
    private Long totalProfit;

    @Builder
    public RankingData(String nickname, int rank, Long totalProfit) {
        this.nickname = nickname;
        this.rank = rank;
        this.totalProfit = totalProfit;
    }

    @Override
    public String toString() {
        return String.format("{\"nickname\":\"%s\",\"rank\":\"%d\",\"totalProfit\":\"%d\"}", nickname, rank, totalProfit);
    }
}

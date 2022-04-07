package monkey.domain.competition;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
public class RankingData implements Serializable {
    private String nickname;
    private Long totalProfit;

    @Builder
    public RankingData(String nickname, int rank, Long totalProfit) {
        this.nickname = nickname;
        this.totalProfit = totalProfit;
    }

    @Override
    public String toString() {
        return String.format("{\"nickname\":\"%s\",\"totalProfit\":\"%d\"}", nickname, totalProfit);
    }
}

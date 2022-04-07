package monkey.domain.competition;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class Ranking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long competitionId;

    @Convert(converter = RankDataConverter.class)
    @Column(columnDefinition = "json")
    private List<RankingData> data;

    @Builder
    public Ranking(Long competitionId, List<RankingData> data) {
        this.competitionId = competitionId;
        this.data = data;
    }
}

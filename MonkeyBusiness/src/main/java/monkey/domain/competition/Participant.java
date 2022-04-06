package monkey.domain.competition;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Participant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String accountId;

    private String nickname;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "competition_id")
    private Competition competition;

    private Long totalProfit;

    @Builder
    public Participant(String accountId, String nickname, Competition competition) {
        this.accountId = accountId;
        this.nickname = nickname;
        this.competition = competition;
        this.totalProfit = 0L;
    }
}

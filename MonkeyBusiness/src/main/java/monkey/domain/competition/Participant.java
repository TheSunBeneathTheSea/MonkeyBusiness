package monkey.domain.competition;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import monkey.domain.account.Account;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Participant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JoinColumns({
            @JoinColumn(name = "account_user_id"),
            @JoinColumn(name = "account_competition_id")
    })
    private Account account;

    private Long totalProfit;

    @Builder
    public Participant(Account account) {
        this.account = account;
        this.totalProfit = 0L;
    }
}

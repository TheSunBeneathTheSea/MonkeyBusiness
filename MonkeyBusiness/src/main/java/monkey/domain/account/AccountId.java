package monkey.domain.account;

import lombok.*;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@EqualsAndHashCode
@Embeddable
@NoArgsConstructor
public class AccountId implements Serializable {
    private String userId;
    private Long competitionId;

    public AccountId(String userId, Long competitionId) {
        this.userId = userId;
        this.competitionId = competitionId;
    }
}

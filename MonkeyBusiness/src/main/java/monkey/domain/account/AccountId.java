package monkey.domain.account;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@EqualsAndHashCode
@Embeddable
public class AccountId implements Serializable {
    private String userId;
    private Long competitionId;

    @Builder
    public AccountId(String userId, Long competitionId) {
        this.userId = userId;
        this.competitionId = competitionId;
    }
}

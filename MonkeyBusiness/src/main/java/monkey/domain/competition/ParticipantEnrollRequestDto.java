package monkey.domain.competition;

import lombok.Data;

@Data
public class ParticipantEnrollRequestDto {
    private Long competitionId;
    private String accountId;
}

package monkey.domain.competition;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    Participant findByAccountId(String accountId);

    boolean existsByAccountIdAndCompetitionId(String accountId, Long competitionId);

}

package monkey.domain.competition;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    @Query("SELECT p FROM Participant p WHERE account_user_id = ?1")
    Participant findByUserId(String accountUserId);

    @Query("SELECT p FROM Participant p WHERE account_user_id = ?1 AND account_competition_id = ?2")
    Participant findByAccountUserIdAndAccountCompetitionId(String accountId, Long competitionId);

    @Query("SELECT p FROM Participant p WHERE account_competition_id = ?1 ORDER BY p.totalProfit")
    List<Participant> findAllByCompetitionIdOrderByTotalProfitDesc(Long competitionId);
}

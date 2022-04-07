package monkey.domain.trading;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TradingLogRepository extends JpaRepository<TradingLog, Long> {
    @Query("SELECT l FROM TradingLog l WHERE account_user_id = ?1 AND account_competition_id = ?2 ORDER BY l.id DESC")
    List<TradingLog> findAllByUserIdAndCompetitionIdDesc(String userId, Long competitionId);
}

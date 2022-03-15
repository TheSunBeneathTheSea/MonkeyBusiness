package monkey.domain.trading;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TradingLogRepository extends JpaRepository<TradingLog, Long> {
    @Query("SELECT l FROM TradingLog l ORDER BY l.id DESC")
    List<TradingLog> findAllDesc();

    @Query("SELECT l FROM TradingLog l WHERE user_id = ?1 ORDER BY l.id DESC")
    List<TradingLog> findAllByUserIdDesc(Long userId);
}

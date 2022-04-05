package monkey.domain.trading;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {
    @Query(value = "SELECT * FROM portfolio WHERE account_user_id = ?1 AND stock_info_ticker = ?2", nativeQuery = true)
    Optional<Portfolio> getPortfolioByAccountIdAndTicker(String accountId, String ticker);

    @Query("SELECT p FROM Portfolio p WHERE account_user_id = ?1")
    List<Portfolio> findAllByAccountId(String accountId);
}

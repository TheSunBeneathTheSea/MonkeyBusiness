package monkey.domain.trading;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StockInfoRepository extends JpaRepository<StockInfo, String> {

    @Query("SELECT s FROM StockInfo s ORDER BY s.ticker ASC")
    List<StockInfo> findAllStockInfoAscByTicker();
}

package monkey.domain.account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, AccountId> {
    @Query("SELECT a FROM Account a WHERE userId = ?1")
    List<Account> findAllByUserId(String userId);

    @Query("SELECT a FROM Account a WHERE competitionId = ?1")
    List<Account> findAllByCompetitionId(Long competitionId);

    @Query("SELECT a FROM Account a WHERE userId = ?1 AND competitionId = 0")
    Account findBaseAccount(String userId);

    @Query("SELECT a FROM Account a WHERE userId = ?1 AND competitionId = ?2")
    Account findAccountById(String userId, Long competitionId);

    @Query("DELETE FROM Account a WHERE competitionId = ?1")
    void deleteAllAccountInCompetition(Long competitionId);
}

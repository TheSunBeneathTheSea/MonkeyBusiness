package monkey.domain.competition;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RankingRepository extends JpaRepository<Ranking, Long> {
    Ranking findByCompetitionId(Long competitionId);
}

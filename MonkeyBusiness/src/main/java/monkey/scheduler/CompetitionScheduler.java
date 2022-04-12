package monkey.scheduler;

import lombok.RequiredArgsConstructor;
import monkey.domain.competition.Competition;
import monkey.service.AccountService;
import monkey.service.CompetitionService;
import monkey.service.RankingService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class CompetitionScheduler {
    private final AccountService accountService;
    private final CompetitionService competitionService;
    private final RankingService rankingService;

    @Scheduled(cron = "0 0 9 ? * 1-5")
    public void startCompetition() {
        List<Long> today = competitionService.getCompetitionStartsToday();

        for (Long id : today) {
            competitionService.startCompetition(id);
        }
    }
    @Scheduled(cron = "0 30 15 ? * 1-5")
    public void endCompetition() {
        List<Long> today = competitionService.getCompetitionEndsToday();

        for (Long id : today) {
            competitionService.endCompetition(id);
            rankingService.storeRankData(id);
            accountService.prepareDelete(id);
            accountService.deleteAccountInCompetition(id);
        }
    }
}

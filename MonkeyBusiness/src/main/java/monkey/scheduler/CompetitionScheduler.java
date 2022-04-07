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
//    @Scheduled(cron = "0 0/1 * ? * 1-5")
    public void startCompetition() {
        List<Long> today = competitionService.getCompetitionStartsToday();

        for (Long id : today) {
            competitionService.startCompetition(id);
        }
    }
//    @Scheduled(cron = "0 30 15 ? * 1-5")
    public void endCompetition() {
        /*
        매일 폐장 시간마다 오늘 끝나는 대회가 있는지 체크
        있다면 해당 대회의 active를 false로 토글하고
        해당 대회의 ranking을 정산하고
        대회에 참여한 participant를 찾아서 account를 삭제
        */
        List<Long> today = competitionService.getCompetitionEndsToday();

        for (Long id : today) {
            competitionService.endCompetition(id);
            rankingService.storeRankData(id);
            accountService.deleteAccountInCompetition(id);
        }
    }
}

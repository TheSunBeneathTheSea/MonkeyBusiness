package monkey.service;

import lombok.RequiredArgsConstructor;
import monkey.domain.account.Account;
import monkey.domain.account.AccountRepository;
import monkey.domain.competition.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class RankingService {
    private final RankingRepository rankingRepository;
    private final AccountRepository accountRepository;
    private final CompetitionRepository competitionRepository;

    @Transactional
    public String storeRankData(Long competitionId) {
        List<RankingData> rankingDataList = getRankingData(competitionId);

        Ranking ranking = Ranking.builder()
                .competitionId(competitionId)
                .data(rankingDataList)
                .build();

        rankingRepository.save(ranking);

        return "ranking information of competition: " + competitionId + " has stored";
    }

    @Transactional
    public List<RankingData> getRankingOfCompetition(Long competitionId) {
        Competition competition = competitionRepository
                .findById(competitionId).orElseThrow(() -> new NoSuchElementException("no such competition"));

        if(!competition.isActive()){
            return rankingRepository.findByCompetitionId(competitionId).getData();
        }


        return getRankingData(competitionId);
    }

    public List<RankingData> getRankingData(Long competitionId) {
        List<Account> accountList = accountRepository.findAllByCompetitionId(competitionId);
        List<RankingData> rankingDataList = new ArrayList<>();

        accountList.sort(new Comparator<Account>() {
            @Override
            public int compare(Account o1, Account o2) {
                return (int)(o2.getTotalCapital() - o1.getTotalCapital());
            }
        });

        Long formerTotalProfit = -1L;
        int formerRank = 0;
        for (int i = 0; i < accountList.size(); i++) {
            Account a = accountList.get(i);
            RankingData rank = RankingData.builder()
                    .nickname(a.getNickname())
                    .rank(formerTotalProfit.equals(a.getTotalCapital()) ? formerRank : i + 1)
                    .totalCapital(a.getTotalCapital())
                    .build();

            rankingDataList.add(rank);
            formerTotalProfit = rank.getTotalCapital();
            formerRank = rank.getRank();
        }

        return rankingDataList;
    }
}

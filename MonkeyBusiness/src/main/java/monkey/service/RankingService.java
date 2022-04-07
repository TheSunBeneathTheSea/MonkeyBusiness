package monkey.service;

import lombok.RequiredArgsConstructor;
import monkey.domain.competition.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class RankingService {
    private final RankingRepository rankingRepository;
    private final ParticipantRepository participantRepository;
    private final CompetitionRepository competitionRepository;

    @Transactional
    public String storeRankData(Long competitionId) {
        List<Participant> participantList = participantRepository.findAllByCompetitionIdOrderByTotalProfitDesc(competitionId);

        List<RankingData> rankingDataList = new ArrayList<>();
        for (int i = 0; i < participantList.size(); i++) {
            Participant p = participantList.get(i);
            RankingData r = RankingData.builder()
                    .nickname(p.getAccount().getNickname())
                    .rank(i + 1)
                    .totalProfit(p.getTotalProfit())
                    .build();

            rankingDataList.add(r);
        }
        Ranking ranking = Ranking.builder()
                .competitionId(competitionId)
                .data(rankingDataList)
                .build();

        rankingRepository.save(ranking);

        return "ranking information of competition: " + competitionId + " has stored";
    }

    @Transactional
    public List<RankingData> getRankingOfCompetition(Long competitionId) {
        Competition competition = competitionRepository.findById(competitionId).orElseThrow(() -> new NoSuchElementException("no such competition"));

        if(!competition.isActive()){
            return rankingRepository.findByCompetitionId(competitionId).getData();
        }

        List<Participant> participantList = participantRepository.findAllByCompetitionIdOrderByTotalProfitDesc(competitionId);

        List<RankingData> rankingDataList = new ArrayList<>();
        for (int i = 0; i < participantList.size(); i++) {
            Participant p = participantList.get(i);
            RankingData r = RankingData.builder()
                    .nickname(p.getAccount().getNickname())
                    .rank(i + 1)
                    .totalProfit(p.getTotalProfit())
                    .build();

            rankingDataList.add(r);
        }

        return rankingDataList;
    }
}

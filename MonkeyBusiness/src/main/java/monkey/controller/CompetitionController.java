package monkey.controller;

import lombok.RequiredArgsConstructor;
import monkey.config.auth.LoginUser;
import monkey.config.auth.dto.User;
import monkey.domain.account.AccountId;
import monkey.domain.competition.*;
import monkey.service.CompetitionService;
import monkey.service.RankingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class CompetitionController {
    private final CompetitionService competitionService;
    private final RankingService rankingService;

    @PostMapping("/competition")
    public ResponseEntity<Long> createCompetition(@RequestBody CompetitionCreateRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(competitionService.createCompetition(requestDto));
    }

    @PostMapping("/participant")
    public ResponseEntity<AccountId> enrollParticipant(@LoginUser User user, @RequestBody Long competitionId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(competitionService.enrollParticipant(competitionId, user.getId()));
    }

    @GetMapping("/competition")
    public ResponseEntity<List<CompetitionVO>> getCompetitions() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(CompetitionVO.transformList(competitionService.getCompetitions()));
    }

    @GetMapping("/competition/{id}")
    public ResponseEntity<CompetitionVO> getCompetitionById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(new CompetitionVO(competitionService.getCompetitionById(id)));
    }

    @DeleteMapping("/competition/{id}")
    public void deleteCompetition(@PathVariable Long id) {
        competitionService.deleteCompetition(id);
    }

    @GetMapping("/ranking/{competitionId}")
    public ResponseEntity<List<RankingData>> getRankingData(@PathVariable Long competitionId) {
        Competition competition = competitionService.getCompetitionById(competitionId);

        if (ObjectUtils.isEmpty(competition)) {
            throw new NullPointerException("no such competition: " + competitionId);
        }

        return ResponseEntity.status(HttpStatus.OK).body(rankingService.getRankingOfCompetition(competitionId));
    }
}

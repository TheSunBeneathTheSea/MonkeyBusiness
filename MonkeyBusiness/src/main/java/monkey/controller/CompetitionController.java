package monkey.controller;

import lombok.RequiredArgsConstructor;
import monkey.domain.account.AccountId;
import monkey.domain.competition.*;
import monkey.service.CompetitionService;
import monkey.service.RankingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
@RestController
public class CompetitionController {
    private final CompetitionService competitionService;
    private final RankingService rankingService;

    @PostMapping("/api/v1/competition")
    public ResponseEntity<String> createCompetition(@RequestBody CompetitionCreateRequestDto requestDto) {
        String msg = competitionService.createCompetition(requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(msg);
    }

    @PostMapping("/api/v1/participant")
    public ResponseEntity<String> enrollParticipant(@RequestBody AccountId requestDto) {
        String msg = competitionService.enrollParticipant(requestDto.getCompetitionId(), requestDto.getUserId());

        return ResponseEntity.status(HttpStatus.CREATED).body(msg);
    }

    @GetMapping("/api/v1/competition")
    public ResponseEntity<List<CompetitionVO>> getCompetitions() {
        return ResponseEntity.status(HttpStatus.OK).body(CompetitionVO.transformList(competitionService.getCompetitions()));
    }

    @GetMapping("/api/v1/competition/{id}")
    public ResponseEntity<CompetitionVO> getCompetitionById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(new CompetitionVO(competitionService.getCompetitionById(id)));
    }

    @DeleteMapping("/api/v1/competition/{id}")
    public void deleteCompetition(@PathVariable Long id) {
        competitionService.deleteCompetition(id);
    }

    @GetMapping("/api/v1/ranking/{competitionId}")
    public ResponseEntity<List<RankingData>> getRankingData(@PathVariable Long competitionId) {
        Competition competition = competitionService.getCompetitionById(competitionId);

        if (ObjectUtils.isEmpty(competition)) {
            throw new NullPointerException("no such competition: " + competitionId);
        }

        return ResponseEntity.status(HttpStatus.OK).body(rankingService.getRankingOfCompetition(competitionId));
    }
}

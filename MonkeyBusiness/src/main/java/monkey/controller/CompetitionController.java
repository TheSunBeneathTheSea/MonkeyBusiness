package monkey.controller;

import lombok.RequiredArgsConstructor;
import monkey.domain.competition.CompetitionCreateRequestDto;
import monkey.domain.competition.CompetitionVO;
import monkey.domain.competition.ParticipantEnrollRequestDto;
import monkey.service.CompetitionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
@RestController
public class CompetitionController {
    private final CompetitionService competitionService;

    @PostMapping("/api/v1/competition")
    public ResponseEntity<String> createCompetition(@RequestBody CompetitionCreateRequestDto requestDto) {
        String msg = competitionService.createCompetition(requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(msg);
    }

    @PostMapping("/api/v1/participant")
    public ResponseEntity<String> enrollParticipant(@RequestBody ParticipantEnrollRequestDto requestDto) {
        String msg = competitionService.enrollParticipant(requestDto.getCompetitionId(), requestDto.getAccountId());

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
}

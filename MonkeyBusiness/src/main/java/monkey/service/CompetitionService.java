package monkey.service;

import lombok.RequiredArgsConstructor;
import monkey.domain.competition.*;
import monkey.domain.account.Account;
import monkey.domain.account.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class CompetitionService {
    private final CompetitionRepository competitionRepository;
    private final ParticipantRepository participantRepository;
    private final AccountRepository accountRepository;

    @Transactional
    public String createCompetition(CompetitionCreateRequestDto requestDto) {
        Competition competition = Competition.builder()
                .name(requestDto.getName())
                .start(requestDto.getStart())
                .end(requestDto.getEnd())
                .build();

        competitionRepository.save(competition);

        return "competition: " + requestDto.getName() + " from: " + requestDto.getStart() + " to: " + requestDto.getEnd() + " created";
    }

    @Transactional
    public String startCompetition(Long competitionId) {
        Competition competition = competitionRepository.getById(competitionId);
        competition.startCompetition();

        return "competition: " + competitionId + " start";
    }

    @Transactional
    public String endCompetition(Long competitionId) {
        Competition competition = competitionRepository.getById(competitionId);
        competition.endCompetition();

        return "competition: " + competitionId + " end";
    }

    @Transactional
    public List<Competition> getCompetitions() {
        return competitionRepository.findAll();
    }

    @Transactional
    public Competition getCompetitionById(Long competitionId) {
        return competitionRepository.findById(competitionId).orElseThrow(() -> new NoSuchElementException("no such competition"));
    }

    @Transactional
    public String enrollParticipant(Long competitionId, String accountId) throws IllegalArgumentException {
        if (participantRepository.existsByAccountIdAndCompetitionId(accountId, competitionId)) {
            throw new IllegalArgumentException("already exists");
        }
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new NoSuchElementException("no such account"));
        Competition competition = competitionRepository.findById(competitionId).orElseThrow(() -> new NoSuchElementException("no such competition"));
        Participant participant = Participant.builder()
                .accountId(accountId)
                .nickname(account.getNickname())
                .competition(competition)
                .build();

        participantRepository.save(participant);

        return "ID: " + participant.getAccountId() + " nickname: " + participant.getNickname() + " enrolled in competition: " + competition.getName();
    }

    @Transactional
    public void deleteCompetition(Long competitionId) {
        Competition competition = competitionRepository.findById(competitionId).orElseThrow(() -> new NoSuchElementException("no such competition"));
        competitionRepository.delete(competition);
    }
}

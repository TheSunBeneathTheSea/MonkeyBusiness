package monkey.service;

import lombok.RequiredArgsConstructor;
import monkey.domain.account.AccountId;
import monkey.domain.competition.*;
import monkey.domain.account.Account;
import monkey.domain.account.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class CompetitionService {
    private final CompetitionRepository competitionRepository;
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
    public List<Long> getCompetitionStartsToday() {
        return competitionRepository.findCompetitionStartsToday();
    }

    @Transactional
    public List<Long> getCompetitionEndsToday() {
        return competitionRepository.findCompetitionEndsToday();
    }

    @Transactional
    public void deleteCompetition(Long competitionId) {
        Competition competition = competitionRepository.findById(competitionId).orElseThrow(() -> new NoSuchElementException("no such competition"));
        competitionRepository.delete(competition);
    }

    @Transactional
    public String enrollParticipant(Long competitionId, String userId) throws IllegalArgumentException {
        Account check = accountRepository.findAccountById(userId, competitionId);
        if (!ObjectUtils.isEmpty(check)) {
            throw new IllegalArgumentException("already exists");
        }
        Account baseAccount = accountRepository.findBaseAccount(userId);
        if (ObjectUtils.isEmpty(baseAccount)) {
            throw new NullPointerException("user: " + userId + "does not exist");
        }
        Competition competition = competitionRepository.findById(competitionId).orElseThrow(() -> new NoSuchElementException("no such competition"));

        AccountId accountId = AccountId.builder()
                .userId(userId)
                .competitionId(competitionId)
                .build();
        Account account = Account.builder()
                .id(accountId)
                .nickname(baseAccount.getNickname())
                .build();

        accountRepository.save(account);

        return "ID: " + account.getId().getUserId() + " nickname: " + account.getNickname() + " enrolled in competition: " + competition.getName();
    }

    @Transactional
    public List<Account> getParticipantsByCompetitionId(Long competitionId) {
        return accountRepository.findAllByCompetitionId(competitionId);
    }
}

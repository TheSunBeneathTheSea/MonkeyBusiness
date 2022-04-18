package monkey.service;

import lombok.RequiredArgsConstructor;
import monkey.domain.account.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    private final PortfolioRepository portfolioRepository;

    @Transactional
    public Account createAccount(AccountSaveRequestDto requestDto) {
        AccountId id = new AccountId(requestDto.getUserId(), 0L);

        if (accountRepository.existsById(id)) {
            throw new IllegalArgumentException("user: " + requestDto.getUserId() + " already have base account");
        }
        return accountRepository.save(Account.builder()
                .id(id)
                .nickname(requestDto.getNickname())
                .build());
    }

    @Transactional
    public void deleteAccount(String userId) {
        List<Account> account = accountRepository.findAllByUserId(userId);
        accountRepository.deleteAll(account);
    }

    @Transactional
    public void prepareDelete(String userId) {
        List<Account> accountList = accountRepository.findAllByUserId(userId);
        for (Account a : accountList) {
            a.prepareDelete();
        }
        accountRepository.saveAll(accountList);
    }

    @Transactional
    public void prepareDelete(Long competitionId) {
        List<Account> accountList = accountRepository.findAllByCompetitionId(competitionId);
        for (Account a : accountList) {
            a.prepareDelete();
        }
        accountRepository.saveAll(accountList);
    }

    @Transactional
    public void deleteAccountInCompetition(Long competitionId) {
        accountRepository.deleteAllAccountInCompetition(competitionId);
    }

    @Transactional
    public List<Account> showAccounts(String userId) {
        return accountRepository.findAllByUserId(userId);
    }

    @Transactional
    public Account showAccountById(AccountId id) {
        return accountRepository.findAccountById(id.getUserId(), id.getCompetitionId());
    }

    @Transactional
    public List<Portfolio> showPortfolios(String userId, Long competitionId) {
        return portfolioRepository.findAllByAccountIdAndCompetitionId(userId, competitionId);
    }
}

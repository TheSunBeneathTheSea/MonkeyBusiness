package monkey.service;

import lombok.RequiredArgsConstructor;
import monkey.domain.account.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final PortfolioRepository portfolioRepository;

    @Transactional
    public String createAccount(AccountSaveRequestDto requestDto) {
        Account account = Account.builder()
                .user_id(requestDto.getUserId())
                .nickname(requestDto.getNickname())
                .build();

        accountRepository.save(account);

        return "create account for nickname: " + account.getNickname();
    }

    @Transactional
    public void deleteAccount(String accountId) {
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new NullPointerException("no such account"));
        accountRepository.delete(account);
    }

    @Transactional
    public List<Account> showTradingData() {
        return accountRepository.findAll();
    }

    @Transactional
    public Account showTradingDataOfId(String user_id) {
        return accountRepository.getById(user_id);
    }

    @Transactional
    public List<Portfolio> showPortfolios(String user_id) {
        return portfolioRepository.findAllByAccountId(user_id);
    }
}

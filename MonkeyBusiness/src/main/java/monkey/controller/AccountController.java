package monkey.controller;

import lombok.RequiredArgsConstructor;
import monkey.domain.account.AccountSaveRequestDto;
import monkey.domain.account.AccountVO;
import monkey.domain.account.PortfolioVO;
import monkey.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
@RestController
public class AccountController {
    private final AccountService accountService;

    @PostMapping("/api/v1/account")
    public ResponseEntity<String> createAccount(@RequestBody AccountSaveRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(accountService.createAccount(requestDto));
    }

    @GetMapping("/api/v1/account")
    public ResponseEntity<List<AccountVO>> showAccounts() {
        return ResponseEntity.status(HttpStatus.OK).body(AccountVO.transformList(accountService.showTradingData()));
    }

    @DeleteMapping("/api/v1/account")
    public void deleteAccount(@RequestBody String accountId) {
        accountService.deleteAccount(accountId);
    }

    @GetMapping("/api/v1/account/{user_id}")
    public ResponseEntity<AccountVO> showAccountById(@PathVariable String user_id) {
        return ResponseEntity.status(HttpStatus.OK).body(new AccountVO(accountService.showTradingDataOfId(user_id)));
    }

    @GetMapping("/api/v1/portfolio/{user_id}")
    public ResponseEntity<List<PortfolioVO>> showPortfolios(@PathVariable String user_id) {
        List<PortfolioVO> portfolioVOList = accountService.showPortfolios(user_id).stream().map(portfolio -> new PortfolioVO(portfolio)).collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(portfolioVOList);
    }
}

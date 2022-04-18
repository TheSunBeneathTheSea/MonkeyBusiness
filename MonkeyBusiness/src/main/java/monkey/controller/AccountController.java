package monkey.controller;

import lombok.RequiredArgsConstructor;
import monkey.domain.account.*;
import monkey.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/account")
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<AccountVO> createAccount(@RequestBody AccountSaveRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(new AccountVO(accountService.createAccount(requestDto)));
    }

    @GetMapping
    public ResponseEntity<List<AccountVO>> showAccounts(@RequestParam String userId) {
        return ResponseEntity.status(HttpStatus.OK).body(AccountVO.transformList(accountService.showAccounts(userId)));
    }

    @DeleteMapping
    public void deleteAccount(@RequestBody HashMap<String, String> map) {
        if (!map.containsKey("userId")) {
            throw new IllegalArgumentException("need userId");
        }
        accountService.prepareDelete(map.get("userId"));
        accountService.deleteAccount(map.get("userId"));
    }

    @GetMapping("/{competitionId}")
    public ResponseEntity<AccountVO> showAccountById(@RequestParam String userId, @PathVariable Long competitionId) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new AccountVO(accountService.showAccountById(
                        new AccountId(userId, competitionId)
                ))
        );
    }

    @GetMapping("/portfolio")
    public ResponseEntity<List<PortfolioVO>> showPortfolios(@RequestParam String userId, @RequestParam Long competitionId) {
        AccountId id = new AccountId(userId, competitionId);
        List<PortfolioVO> portfolioVOList = PortfolioVO.transformList(accountService.showPortfolios(id.getUserId(), id.getCompetitionId()));
        return ResponseEntity.status(HttpStatus.OK).body(portfolioVOList);
    }
}

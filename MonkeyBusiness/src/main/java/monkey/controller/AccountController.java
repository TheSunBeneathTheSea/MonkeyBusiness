package monkey.controller;

import lombok.RequiredArgsConstructor;
import monkey.domain.account.*;
import monkey.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
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
    public ResponseEntity<List<AccountVO>> showAccounts(@RequestBody HashMap<String, String> map) {
        if(!map.containsKey("userId")){
            throw new IllegalArgumentException("need userId");
        }
        return ResponseEntity.status(HttpStatus.OK).body(AccountVO.transformList(accountService.showAccounts(map.get("userId"))));
    }

    @DeleteMapping("/api/v1/account")
    public void deleteAccount(@RequestBody HashMap<String, String> map) {
        if(!map.containsKey("userId")){
            throw new IllegalArgumentException("need userId");
        }
        accountService.deleteAccount(map.get("userId"));
    }

    @GetMapping("/api/v1/account/{competitionId}")
    public ResponseEntity<AccountVO> showAccountById(@RequestBody HashMap<String, String> map, @PathVariable Long competitionId) {
        if(!map.containsKey("userId")){
            throw new IllegalArgumentException("need userId");
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new AccountVO(accountService.showAccountById(
                        new AccountId(map.get("userId"), competitionId)
                ))
        );
    }

    @GetMapping("/api/v1/portfolio")
    public ResponseEntity<List<PortfolioVO>> showPortfolios(@RequestBody AccountId id) {
        List<PortfolioVO> portfolioVOList = PortfolioVO.transformList(accountService.showPortfolios(id.getUserId(), id.getCompetitionId()));
        return ResponseEntity.status(HttpStatus.OK).body(portfolioVOList);
    }
}

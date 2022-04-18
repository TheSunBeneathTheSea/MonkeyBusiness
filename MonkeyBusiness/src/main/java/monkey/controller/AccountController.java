package monkey.controller;

import lombok.RequiredArgsConstructor;
import monkey.config.auth.LoginUser;
import monkey.config.auth.dto.User;
import monkey.domain.account.*;
import monkey.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/account")
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<AccountVO> createAccount(@LoginUser User user) {
        AccountSaveRequestDto requestDto = AccountSaveRequestDto.of(user.getId(), user.getNickname());

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new AccountVO(accountService.createAccount(requestDto)));
    }

    @GetMapping
    public ResponseEntity<List<AccountVO>> showAccounts(@LoginUser User user) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(AccountVO.transformList(accountService.showAccounts(user.getId())));
    }

    @DeleteMapping
    public void deleteAccount(@LoginUser User user) {
        accountService.prepareDelete(user.getId());
        accountService.deleteAccount(user.getId());
    }

    @GetMapping("/{competitionId}")
    public ResponseEntity<AccountVO> showAccountById(@LoginUser User user, @PathVariable Long competitionId) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new AccountVO(accountService.showAccountById(
                        new AccountId(user.getId(), competitionId)
                ))
        );
    }

    @GetMapping("/portfolio")
    public ResponseEntity<List<PortfolioVO>> showPortfolios(@LoginUser User user, @RequestParam Long competitionId) {
        AccountId id = new AccountId(user.getId(), competitionId);
        List<PortfolioVO> portfolioVOList =
                PortfolioVO.transformList(accountService.showPortfolios(id.getUserId(), id.getCompetitionId()));
        return ResponseEntity.status(HttpStatus.OK).body(portfolioVOList);
    }
}

package monkey.controller;

import lombok.RequiredArgsConstructor;
import monkey.config.auth.LoginUser;
import monkey.config.auth.dto.User;
import monkey.domain.account.AccountId;
import monkey.domain.trading.*;
import monkey.service.TradingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class TradingController {
    private final TradingService tradingService;

    @PostMapping("/trade")
    public ResponseEntity<Long> placeOrder(@LoginUser User user, @RequestBody TradeOrderRequestDto tradeOrderRequestDto)
            throws NoSuchElementException, IllegalArgumentException {

        if (tradeOrderRequestDto.isBuying()) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(tradingService.buyingStocks(user.getId(), tradeOrderRequestDto));
        } else {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(tradingService.sellingStocks(user.getId(), tradeOrderRequestDto));
        }
    }

    @GetMapping("/logs")
    public ResponseEntity<List<TradingLogVO>> showAccountLogs(
            @LoginUser User user, @RequestParam Long competitionId
    ) {
        AccountId id = new AccountId(user.getId(), competitionId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(TradingLogVO.transformList(tradingService.showLogsOfUserByUserId(id)));
    }

    @GetMapping("/stock")
    public ResponseEntity<List<StockInfoVO>> showStockInfo() {
        return ResponseEntity.status(HttpStatus.OK).body(StockInfoVO.transformList(tradingService.showStockInfo()));
    }
}

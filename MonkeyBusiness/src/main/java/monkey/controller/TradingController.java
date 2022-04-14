package monkey.controller;

import lombok.RequiredArgsConstructor;
import monkey.domain.account.AccountId;
import monkey.domain.trading.*;
import monkey.service.TradingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
@RestController
public class TradingController {
    private final TradingService tradingService;

    @PostMapping("/api/v1/trade")
    public ResponseEntity<String> placeOrder(@RequestBody TradeOrderRequestDto tradeOrderRequestDto) throws NoSuchElementException, IllegalArgumentException {
        if (tradeOrderRequestDto.isBuying()) {
            return ResponseEntity.status(HttpStatus.OK).body(tradingService.buyingStocks(tradeOrderRequestDto));
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(tradingService.sellingStocks(tradeOrderRequestDto));
        }
    }

    @GetMapping("/api/v1/logs")
    public ResponseEntity<List<TradingLogVO>> showAccountLogs(@RequestParam String userId, @RequestParam Long competitionId) {
        AccountId id = new AccountId(userId, competitionId);
        return ResponseEntity.status(HttpStatus.OK).body(TradingLogVO.transformList(tradingService.showLogsOfUserByUserId(id)));
    }

    @GetMapping("/api/v1/stock")
    public ResponseEntity<List<StockInfoVO>> showStockInfo() {
        return ResponseEntity.status(HttpStatus.OK).body(StockInfoVO.transformList(tradingService.showStockInfo()));
    }
}

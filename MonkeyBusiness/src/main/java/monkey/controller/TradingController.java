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
    public ResponseEntity<String> placeOrder(@RequestBody TradeRequestVO tradeRequestVO) throws NoSuchElementException, IllegalArgumentException {
        if (tradeRequestVO.isBuying()) {
            return ResponseEntity.status(HttpStatus.OK).body(tradingService.buyingStocks(tradeRequestVO));
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(tradingService.sellingStocks(tradeRequestVO));
        }
    }

    @GetMapping("/api/v1/logs")
    public ResponseEntity<List<TradingLogVO>> showAccountLogs(@RequestBody AccountId id) {
        return ResponseEntity.status(HttpStatus.OK).body(TradingLogVO.transformList(tradingService.showLogsOfUserByUserId(id)));
    }
}

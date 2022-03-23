package monkey.controller;

import lombok.RequiredArgsConstructor;
import monkey.domain.trading.*;
import monkey.service.StockUpdateService;
import monkey.service.TradingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.Schedules;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
@RestController
public class TradingController {
    private final TradingService tradingService;
    private final StockUpdateService stockUpdateService;

    @Scheduled(cron = "15 0 9 ? * 1-5")
    public void openMarket() throws IOException, Exception {
        stockUpdateService.updateStocks(true);
        tradingService.checkProfit();
    }

    @Schedules({
            @Scheduled(cron = "15 0/2 9-14 ? * 1-5"),
            @Scheduled(cron = "15 0-30/2 15 ? * 1-5")
    })
    public void updateStock() throws IOException, Exception {
        stockUpdateService.updateStocks(false);
        tradingService.checkProfit();
    }

    @PostMapping("/api/v1/account")
    public String saveStrategy(@RequestBody AccountSaveRequestDto saveRequestDto) throws Exception {
        return tradingService.setStrategy(saveRequestDto);
    }

    @PutMapping("/api/v1/account")
    public String editStrategy(@RequestBody AccountSaveRequestDto saveRequestDto) throws Exception {
        return tradingService.setStrategy(saveRequestDto);
    }

    @GetMapping("/api/v1/account")
    public ResponseEntity<List<AccountVO>> showAccounts() {
        return ResponseEntity.status(HttpStatus.OK).body(AccountVO.transformList(tradingService.showTradingData()));
    }

    @GetMapping("/api/v1/account/{user_id}")
    public ResponseEntity<AccountVO> showAccountById(@PathVariable String user_id) {
        return ResponseEntity.status(HttpStatus.OK).body(new AccountVO(tradingService.showTradingDataOfId(user_id)));
    }

    @GetMapping("/api/v1/logs")
    public ResponseEntity<List<TradingLogVO>> showLogs() {
        return ResponseEntity.status(HttpStatus.OK).body(TradingLogVO.transformList(tradingService.showLogs()));
    }

    @GetMapping("/api/v1/logs/{user_id}")
    public ResponseEntity<List<TradingLogVO>> showAccountLogs(@PathVariable String user_id) {
        return ResponseEntity.status(HttpStatus.OK).body(TradingLogVO.transformList(tradingService.showLogsOfUserByUserId(user_id)));
    }
}

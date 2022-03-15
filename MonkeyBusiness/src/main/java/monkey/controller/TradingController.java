package monkey.controller;

import lombok.RequiredArgsConstructor;
import monkey.domain.trading.TradingData;
import monkey.domain.trading.TradingDataSaveRequestDto;
import monkey.domain.trading.TradingDataVO;
import monkey.domain.trading.TradingLog;
import monkey.service.StockUpdateService;
import monkey.service.TradingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class TradingController {
    private final TradingService tradingService;
    private final StockUpdateService stockUpdateService;

    @Scheduled(cron = "0 2 9 ? * 1-5")
    public void openMarket() throws IOException {
        stockUpdateService.updateStocks(true);
        tradingService.checkProfit();
    }

    //    @Schedules({
//            @Scheduled(cron = "0 6/5 9-14 ? * 1-5"),
//            @Scheduled(cron = "0 1-26/1 15 ? * 1-5")
//    })
    // 테스트용 스케줄링
    @Scheduled(cron = "0 0/2 * * * *")
    public void updateStock() throws IOException {
        stockUpdateService.updateStocks(false);
        tradingService.checkProfit();
    }

    @PostMapping("/api/v1/trading")
    public String saveStrategy(@RequestBody TradingDataSaveRequestDto saveRequestDto) {
        return tradingService.makeStrategy(saveRequestDto);
    }

    @PutMapping("/api/v1/trading")
    public String editStrategy(@RequestBody TradingDataSaveRequestDto saveRequestDto) {
        return tradingService.adjustStrategy(saveRequestDto);
    }

    @GetMapping("/api/v1/trading")
    public ResponseEntity<List<TradingDataVO>> showTradingData() {
        return ResponseEntity.status(HttpStatus.OK).body(TradingDataVO.transformList(tradingService.showTradingData()));
    }

    @GetMapping("/api/v1/logs")
    public ResponseEntity<List<TradingLog>> showLogs() {
        return ResponseEntity.status(HttpStatus.OK).body(tradingService.showLogs());
    }
}

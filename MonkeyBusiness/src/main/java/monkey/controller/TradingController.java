package monkey.controller;

import lombok.RequiredArgsConstructor;
import monkey.domain.trading.*;
import monkey.service.TradingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
@RestController
public class TradingController {
    private final TradingService tradingService;

    @PostMapping("/api/v1/trade")
    public ResponseEntity<String> placeOrder(@RequestBody TradeRequestVO tradeRequestVO) throws Exception {
        if (tradeRequestVO.isBuying()) {
            return ResponseEntity.status(HttpStatus.OK).body(tradingService.buyingStocks(tradeRequestVO));
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(tradingService.sellingStocks(tradeRequestVO));
        }
    }

    @GetMapping("/api/v1/account")
    public ResponseEntity<List<AccountVO>> showAccounts() {
        return ResponseEntity.status(HttpStatus.OK).body(AccountVO.transformList(tradingService.showTradingData()));
    }

    @GetMapping("/api/v1/account/{user_id}")
    public ResponseEntity<AccountVO> showAccountById(@PathVariable String user_id) {
        return ResponseEntity.status(HttpStatus.OK).body(new AccountVO(tradingService.showTradingDataOfId(user_id)));
    }

    @GetMapping("/api/v1/portfolio/{user_id}")
    public ResponseEntity<List<PortfolioVO>> showPortfolios(@PathVariable String user_id) {
        List<PortfolioVO> portfolioVOList = tradingService.showPortfolios(user_id).stream().map(portfolio -> new PortfolioVO(portfolio)).collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(portfolioVOList);
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

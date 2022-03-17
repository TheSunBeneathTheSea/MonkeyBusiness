package monkey.controller;

import lombok.RequiredArgsConstructor;
import monkey.domain.trading.StockInfo;
import monkey.domain.trading.StockInfoVO;
import monkey.service.StockUpdateService;
import monkey.service.TradingService;
import monkey.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class indexController {
    private final UserService userService;
    private final TradingService tradingService;
    private final StockUpdateService stockUpdateService;

    @GetMapping("/api/v1/stock")
    public ResponseEntity<List<StockInfoVO>> showStockInfoNow() {
        return ResponseEntity.status(HttpStatus.OK).body(StockInfoVO.transformList(stockUpdateService.findAllStockInfoAsc()));
    }
}

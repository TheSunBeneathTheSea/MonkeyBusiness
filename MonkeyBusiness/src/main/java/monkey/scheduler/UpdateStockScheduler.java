package monkey.scheduler;

import lombok.RequiredArgsConstructor;
import monkey.service.StockUpdateService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.Schedules;
import org.springframework.stereotype.Component;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class UpdateStockScheduler {
    private final StockUpdateService stockUpdateService;

    @Scheduled(cron = "3/15 0 9 ? * 1-5")
    public void openMarket() throws IOException, Exception {
        stockUpdateService.updateStocks(true);
    }

//    @Schedules({
//            @Scheduled(cron = "15 0/2 9-14 ? * 1-5"),
//            @Scheduled(cron = "15 0-30/2 15 ? * 1-5")
//    })
    @Scheduled(cron = "3/15 * * ? * 1-5")
    public void updateStock() throws IOException, Exception {
        stockUpdateService.updateStocks(false);
    }
}

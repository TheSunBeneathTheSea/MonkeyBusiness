package monkey.service;

import lombok.RequiredArgsConstructor;
import monkey.domain.trading.*;
import monkey.domain.user.User;
import monkey.domain.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class TradingService {
    private final UserRepository userRepository;
    private final TradingDataRepository tradingDataRepository;
    private final TradingLogRepository tradingLogRepository;
    private final StockInfoRepository stockInfoRepository;

    @Transactional
    public String makeStrategy(TradingDataSaveRequestDto saveRequestDto) {
        User user = userRepository.findById(saveRequestDto.getUserId()).orElseThrow(() -> new NoSuchElementException("no such user id: " + saveRequestDto.getUserId()));

        TradingStrategy strategy = TradingStrategy.builder()
                .takeProfitPoint(saveRequestDto.getTakeProfitPoint())
                .stopLossPoint(saveRequestDto.getStopLossPoint()).build();

        List<StockInfo> stockInfoList = stockInfoRepository.findAll();
        StockInfo randomInitialTarget = stockInfoList.get((int)(Math.random() * stockInfoList.size()));

        TradingData tradingData = TradingData.builder()
                .id(user.getId())
                .stockInfo(randomInitialTarget)
                .strategy(strategy)
                .build();

        tradingDataRepository.save(tradingData);

        buyingStocks(user.getId());

        return "make profile: " + user.getId();
    }

    @Transactional
    public String adjustStrategy(TradingDataSaveRequestDto saveRequestDto) {
        TradingStrategy strategy = TradingStrategy.builder()
                .takeProfitPoint(saveRequestDto.getTakeProfitPoint())
                .stopLossPoint(saveRequestDto.getStopLossPoint()).build();

        TradingData tradingData = tradingDataRepository.getById(saveRequestDto.getUserId());

        tradingData.updateStrategy(strategy);

        return "update strategy of user: " + saveRequestDto.getUserId();
    }

    @Transactional
    public String buyingStocks(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("no such user id: " + userId));
        TradingData tradingData = tradingDataRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("no such data id: " + userId));
        List<StockInfo> stockInfoList = stockInfoRepository.findAll();

        StockInfo randomTarget = stockInfoList.get((int)(Math.random() * stockInfoList.size()));

        TradingLogDto newLogDto = tradingData.buyingStocks(randomTarget);
        TradingLog newLog = new TradingLog(user.getId(), newLogDto);

        tradingLogRepository.save(newLog);

        return userId + ": " + randomTarget.getTicker() + " Buy";
    }

    @Transactional
    public String sellingStocks(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("no such user id: " + userId));
        TradingData tradingData = tradingDataRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("no such data id: " + userId));

        TradingLogDto newLogDto = tradingData.sellingStocks();

        tradingLogRepository.save(new TradingLog(user.getId(), newLogDto));

        return userId + ": " + newLogDto.getTicker() + " Sell";
    }

    @Transactional
    public String checkProfit() {
        List<TradingData> tradingDataList = tradingDataRepository.findAll();

        for (TradingData data : tradingDataList) {
            if (!data.makeDecision()) {
                continue;
            }

            sellingStocks(data.getId());
            buyingStocks(data.getId());
        }

        return "checked";
    }

    @Transactional
    public List<TradingData> showTradingData() {
        return tradingDataRepository.findAll();
    }
    @Transactional
    public List<TradingLog> showLogs() {
        return tradingLogRepository.findAllDesc();
    }
}

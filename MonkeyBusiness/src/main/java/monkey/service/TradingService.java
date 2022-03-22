package monkey.service;

import lombok.RequiredArgsConstructor;
import monkey.domain.trading.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class TradingService {
    private final AccountRepository accountRepository;
    private final TradingLogRepository tradingLogRepository;
    private final StockInfoRepository stockInfoRepository;

    @Transactional
    public String setStrategy(AccountSaveRequestDto saveRequestDto) throws Exception {
        TradingStrategy strategy = TradingStrategy.builder()
                .takeProfitPoint(saveRequestDto.getTakeProfitPoint())
                .stopLossPoint(saveRequestDto.getStopLossPoint()).build();

        Account account = accountRepository.getById(saveRequestDto.getUser_id());

        account.updateStrategy(strategy);

        if(ObjectUtils.isEmpty(account.getStockInfo())){
            buyingStocks(account.getUser_id());
        }

        return "edit profile: " + account.getUser_id();
    }

    @Transactional
    public String buyingStocks(String user_id) throws Exception {
        Account account = accountRepository.findById(user_id).orElseThrow(() -> new NoSuchElementException("no such data id: " + user_id));

        List<StockInfo> stockInfoList = stockInfoRepository.findAll();

        StockInfo randomTarget = stockInfoList.get((int)(Math.random() * stockInfoList.size()));

        TradingLogDto newLogDto = account.buyingStocks(randomTarget).orElseThrow(() -> new NullPointerException("cannot afford points"));
        TradingLog newLog = new TradingLog(account, newLogDto);

        tradingLogRepository.save(newLog);

        return user_id + ": " + randomTarget.getTicker() + " Buy";
    }

    @Transactional
    public String sellingStocks(String user_id) {
        Account account = accountRepository.findById(user_id).orElseThrow(() -> new NoSuchElementException("no such account id: " + user_id));

        TradingLogDto newLogDto = account.sellingStocks();

        tradingLogRepository.save(new TradingLog(account, newLogDto));

        return account.getUser_id() + ": " + newLogDto.getTicker() + " Sell";
    }

    @Transactional
    public String checkProfit() throws Exception {
        List<Account> accountList = accountRepository.findAll();

        for (Account account : accountList) {
            if (!account.makeDecision()) {
                continue;
            }

            sellingStocks(account.getUser_id());
            buyingStocks(account.getUser_id());
        }

        return "checked";
    }

    @Transactional
    public List<Account> showTradingData() {
        return accountRepository.findAll();
    }

    @Transactional
    public Account showTradingDataOfId(String user_id) {
        return accountRepository.getById(user_id);
    }

    @Transactional
    public List<TradingLog> showLogs() {
        return tradingLogRepository.findAllDesc();
    }

    @Transactional
    public List<TradingLog> showLogsOfUserByUserId(String user_id) {
        return tradingLogRepository.findAllByUserIdDesc(user_id);
    }
}

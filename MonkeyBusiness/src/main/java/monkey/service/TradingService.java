package monkey.service;

import lombok.RequiredArgsConstructor;
import monkey.domain.account.Account;
import monkey.domain.account.AccountRepository;
import monkey.domain.account.Portfolio;
import monkey.domain.account.PortfolioRepository;
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
    private final PortfolioRepository portfolioRepository;

    @Transactional
    public String buyingStocks(TradeRequestVO requestVO) throws NoSuchElementException, IllegalArgumentException {
        Account account = accountRepository.getById(requestVO.getUserId());
        if(ObjectUtils.isEmpty(account)){
            throw new NoSuchElementException("no such data id: " + requestVO.getUserId());
        }

        StockInfo stockInfo = stockInfoRepository.getById(requestVO.getTicker());
        if(ObjectUtils.isEmpty(account)){
            throw new NoSuchElementException("no such stock ticker: " + requestVO.getTicker());
        }

        if(!account.canBuy(stockInfo)){
            throw new IllegalArgumentException("not enough points");
        }

        if (stockInfo.getCurrentPrice() <= 0) {
            throw new IllegalArgumentException("invalid stock price");
        }

        Portfolio portfolio = portfolioRepository.getPortfolioByAccountIdAndTicker(requestVO.getUserId(), requestVO.getTicker())
                .orElse(Portfolio.builder()
                    .stockInfo(stockInfo)
                    .account(account)
                    .amount(0)
                    .buyingPrice(0)
                    .build());

        TradeRequestDto requestDto = new TradeRequestDto(requestVO, portfolio);
        TradingLogDto newLogDto = account.buyingStocks(requestDto);

        portfolioRepository.save(portfolio);
        tradingLogRepository.save(new TradingLog(account, newLogDto));

        return "userId:" + requestVO.getUserId() + " buy " + newLogDto.getAmount()
                + " share of: " + newLogDto.getCompanyName() + " at " + newLogDto.getBuyingPrice();
    }

    @Transactional
    public String sellingStocks(TradeRequestVO requestVO) {
        Account account = accountRepository.findById(requestVO.getUserId()).orElseThrow(() -> new NoSuchElementException("no such account id: " + requestVO.getUserId()));
        StockInfo stockInfo = stockInfoRepository.findById(requestVO.getTicker()).orElseThrow(() -> new NoSuchElementException("no such stock ticker: " + requestVO.getTicker()));

        Portfolio portfolio = portfolioRepository.getPortfolioByAccountIdAndTicker(requestVO.getUserId(), requestVO.getTicker())
                .orElseThrow(() -> new NullPointerException("user: " + requestVO.getUserId() + "does not have ticker: " + requestVO.getTicker()));

        TradeRequestDto requestDto = new TradeRequestDto(requestVO, portfolio);
        TradingLogDto newLogDto = account.sellingStocks(requestDto);

        tradingLogRepository.save(new TradingLog(account, newLogDto));

        if (portfolio.getAmount() == 0) {
            portfolioRepository.delete(portfolio);
        } else {
            portfolioRepository.save(portfolio);
        }

        return "userId:" + requestVO.getUserId() + " " + " sell " + newLogDto.getAmount()
                + " share of: " + newLogDto.getCompanyName() + " at " + newLogDto.getSellingPrice();
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

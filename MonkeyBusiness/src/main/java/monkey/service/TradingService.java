package monkey.service;

import lombok.RequiredArgsConstructor;
import monkey.domain.account.*;
import monkey.domain.competition.CompetitionRepository;
import monkey.domain.trading.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class TradingService {
    private final AccountRepository accountRepository;
    private final TradingLogRepository tradingLogRepository;
    private final StockInfoRepository stockInfoRepository;
    private final PortfolioRepository portfolioRepository;
    private final CompetitionRepository competitionRepository;

    @Transactional
    public String buyingStocks(TradeOrderRequestDto requestVO) throws NoSuchElementException, IllegalArgumentException {
        if (!checkCompetitionActiveness(requestVO.getCompetitionId())) {
            throw new IllegalArgumentException("competition: " + requestVO.getCompetitionId() + " is not active");
        }

        if (requestVO.getAmount() <= 0) {
            throw new IllegalArgumentException("you cannot buy " + requestVO.getAmount() + " amount");
        }

        AccountId id = new AccountId(requestVO.getUserId(), requestVO.getCompetitionId());
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(
                        "no such account for user id: "
                                + requestVO.getUserId() + ", competition id: "
                                + requestVO.getCompetitionId()
                ));

        StockInfo stockInfo = stockInfoRepository.findById(requestVO.getTicker())
                .orElseThrow(() -> new NoSuchElementException(
                        "no such stock ticker: " + requestVO.getTicker()
                ));

        if (!account.canBuy(stockInfo)) {
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
    public String sellingStocks(TradeOrderRequestDto requestVO) {
        if (!checkCompetitionActiveness(requestVO.getCompetitionId())) {
            throw new IllegalArgumentException("competition: " + requestVO.getCompetitionId() + " is not active");
        }

        if (requestVO.getAmount() <= 0) {
            throw new IllegalArgumentException("you cannot sell " + requestVO.getAmount() + " amount");
        }

        AccountId id = new AccountId(requestVO.getUserId(), requestVO.getCompetitionId());
        Account account = accountRepository.findById(id).orElseThrow(() -> new NoSuchElementException("no such account id: " + requestVO.getUserId()));
        StockInfo stockInfo = stockInfoRepository.findById(requestVO.getTicker()).orElseThrow(() -> new NoSuchElementException("no such stock ticker: " + requestVO.getTicker()));

        Portfolio portfolio = portfolioRepository.getPortfolioByAccountIdAndTicker(requestVO.getUserId(), requestVO.getTicker())
                .orElseThrow(() -> new NullPointerException("user: " + requestVO.getUserId() + "does not have ticker: " + requestVO.getTicker()));

        TradeRequestDto requestDto = new TradeRequestDto(requestVO, portfolio);
        TradingLogDto newLogDto = account.sellingStocks(requestDto);
        TradingLog newLog = new TradingLog(account, newLogDto);

        tradingLogRepository.save(newLog);

        if (portfolio.getAmount() == 0) {
            portfolioRepository.delete(portfolio);
        } else {
            portfolioRepository.save(portfolio);
        }

        return "userId:" + requestVO.getUserId() + " " + " sell " + newLogDto.getAmount()
                + " share of: " + newLogDto.getCompanyName() + " at " + newLogDto.getSellingPrice();
    }

    @Transactional
    public List<TradingLog> showLogsOfUserByUserId(AccountId id) {
        return tradingLogRepository.findAllByUserIdAndCompetitionIdDesc(id.getUserId(), id.getCompetitionId());
    }

    @Transactional
    public List<StockInfo> showStockInfo() {
        return stockInfoRepository.findAll();
    }

    public boolean checkCompetitionActiveness(Long competitionId) {
        // base account
        if (competitionId == 0L) {
            return true;
        }
        return competitionRepository.isActive(competitionId);
    }
}

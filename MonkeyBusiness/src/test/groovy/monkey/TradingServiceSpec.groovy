package monkey

import monkey.domain.account.Account
import monkey.domain.account.AccountId
import monkey.domain.account.AccountRepository
import monkey.domain.account.PortfolioRepository
import monkey.domain.trading.StockInfo
import monkey.domain.trading.StockInfoRepository
import monkey.domain.trading.TradeOrderRequestDto
import monkey.domain.trading.TradingLogRepository
import monkey.service.TradingService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import spock.lang.Shared
import spock.lang.Specification

@SpringBootTest
class TradingServiceSpec extends Specification {
    @Autowired
    AccountRepository accountRepository
    @Autowired
    StockInfoRepository stockInfoRepository
    @Autowired
    PortfolioRepository portfolioRepository
    @Autowired
    TradingLogRepository tradingLogRepository

    @Autowired
    TradingService tradingService

    @Shared
    TradeOrderRequestDto tradeOrderRequestDto = new TradeOrderRequestDto()

    @Transactional
    void setup() {
        //account set up
        AccountId id1 = new AccountId("aaa", 0L)
        AccountId id1_1 = new AccountId("aaa", 1L)
        AccountId id2 = new AccountId("bbb", 0L)
        AccountId id2_1 = new AccountId("bbb", 1L)
        def account1 = new Account(id1, "abcd")
        def account1_1 = new Account(id1_1, "abcd")
        def account2 = new Account(id2, "tester")
        def account2_1 = new Account(id2_1, "tester")
        accountRepository.save(account1)
        accountRepository.save(account1_1)
        accountRepository.save(account2)
        accountRepository.save(account2_1)

        //stock info set up
        StockInfo stock1 = new StockInfo("001", "stock1", 1000)
        StockInfo stock2 = new StockInfo("002", "stock2", 20000)
        StockInfo stock3 = new StockInfo("003", "stock3", 3000000)
        stockInfoRepository.save(stock1)
        stockInfoRepository.save(stock2)
        stockInfoRepository.save(stock3)

        tradeOrderRequestDto.setTicker("001")
        tradeOrderRequestDto.setCompetitionId(0L)
        tradeOrderRequestDto.setBuying(true)
        tradeOrderRequestDto.setUserId("aaa")
        tradeOrderRequestDto.setAmount(10)
    }

    @Transactional
    def "일반 매수 성공"() {
        given:
        tradeOrderRequestDto.setTicker(ticker)
        tradeOrderRequestDto.setAmount(amount)

        when:
        tradingService.buyingStocks(tradeOrderRequestDto)
        value = portfolioRepository.getPortfolioByAccountIdAndTicker("aaa", ticker).get().calculateValue()

        then:
        AccountId accountId = new AccountId("aaa", 0L)
        Account account = accountRepository.getById(accountId)
        account.getPoints() == result


        where:
        ticker  | amount  || result  | value
        "001"   | 11      || 989000  | 11000L
        "001"   | 999     || 1000    | 999000L
        "002"   | 3       || 940000  | 60000L
        "002"   | 10      || 800000  | 200000L
    }

    @Transactional
    def "일반 매수 실패"() {
        given:
        tradeOrderRequestDto.setTicker(ticker)
        tradeOrderRequestDto.setUserId(account)
        tradeOrderRequestDto.setAmount(amount)

        when:
        tradingService.buyingStocks(tradeOrderRequestDto)

        then:
        thrown(exception)

        where:
        account | ticker  | amount  || exception
        "aaa"   | "001"   | -1      || IllegalArgumentException     //invalid amount
        "aaa"   | "003"   | 2       || IllegalArgumentException     //not enough points
        "bbb"   | "004"   | 9       || NoSuchElementException       //stock info not exists
        "ccc"   | "002"   | 10      || NoSuchElementException       //account not exists
    }

    @Transactional
    def "가지고 있는 종목 추가 매수"() {
        given:
        tradeOrderRequestDto.setTicker("test")

        def stockInfo = new StockInfo("test", "BDD", defaultPrice)
        stockInfoRepository.save(stockInfo)


        when:
        tradingService.buyingStocks(tradeOrderRequestDto)
        stockInfo.updateCurrentPrice(nextPrice)
        stockInfoRepository.save(stockInfo)
        tradeOrderRequestDto.setAmount(7)
        tradingService.buyingStocks(tradeOrderRequestDto)

        then:
        portfolioRepository.getPortfolioByAccountIdAndTicker("aaa", "test").get().getBuyingPrice() == result

        where:
        defaultPrice | nextPrice   || result
        25000        | 40000       || 31176
        17000        | 12500       || 15147
        35712        | 68027       || 49018
    }


    @Transactional
    def "일부 매도"() {
        given:
        tradeOrderRequestDto.setAmount(buy)

        tradingService.buyingStocks(tradeOrderRequestDto)

        when:
        tradeOrderRequestDto.setBuying(false)
        tradeOrderRequestDto.setAmount(sell)
        tradingService.sellingStocks(tradeOrderRequestDto)

        then:
        accountRepository.findAccountById("aaa", 0L).getPoints() == points
        portfolioRepository.getPortfolioByAccountIdAndTicker("aaa","001").get().getAmount() == remain
        tradingLogRepository.findAllByUserIdAndCompetitionIdDesc("aaa", 0L).get(1).getAmount() == buy
        tradingLogRepository.findAllByUserIdAndCompetitionIdDesc("aaa", 0L).get(0).getAmount() == sell

        where:
        buy     | sell      || points       | remain
        10      | 7         || 997000       | 3
        123     | 23        || 900000       | 100
        2       | 1         || 999000       | 1
    }

    @Transactional
    def "전량 매도"() {
        given:
        tradingService.buyingStocks(tradeOrderRequestDto)

        tradingService.sellingStocks(tradeOrderRequestDto)

        expect:
        accountRepository.findAccountById("aaa", 0L).getPoints() == 1000000L
        portfolioRepository.getPortfolioByAccountIdAndTicker("aaa","001").isEmpty()
    }

    @Transactional
    def "매도 실패"() {
        given:
        tradingService.buyingStocks(tradeOrderRequestDto)

        tradeOrderRequestDto.setUserId(account)
        tradeOrderRequestDto.setTicker(ticker)
        tradeOrderRequestDto.setAmount(amount)

        when:
        tradingService.sellingStocks(tradeOrderRequestDto)

        then:
        thrown(exception)

        where:
        account | ticker  | amount  || exception
        "aaa"   | "001"   | -1      || IllegalArgumentException     //invalid amount
        "aaa"   | "002"   | 2       || NullPointerException         //user does not have the stock
        "aaa"   | "444"   | 9       || NoSuchElementException       //stock info not exists
        "zzz"   | "002"   | 10      || NoSuchElementException       //account not exists
    }
}

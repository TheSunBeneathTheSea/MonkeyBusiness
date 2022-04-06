package monkey

import monkey.domain.account.Account
import monkey.domain.account.AccountRepository
import monkey.domain.account.PortfolioRepository
import monkey.domain.trading.StockInfo
import monkey.domain.trading.StockInfoRepository
import monkey.domain.trading.StockUpdateDto
import monkey.domain.trading.TradeRequestVO
import monkey.domain.trading.TradingLogRepository
import monkey.service.TradingService
import spock.lang.Specification

class TradingServiceSpec extends Specification{
    def tradeRequestVO = new TradeRequestVO()
    def tradingService
    def account = new Account("abcd")
    def accountRepository = Mock(AccountRepository)
    def tradingLogRepository = Mock(TradingLogRepository)
    def stockInfoRepository = Mock(StockInfoRepository)
    def portfolioRepository = Mock(PortfolioRepository)

    void setup(){
        accountRepository.save(account)
        tradingService = new TradingService(accountRepository, tradingLogRepository, stockInfoRepository, portfolioRepository)
        tradeRequestVO.setTicker("001")
        tradeRequestVO.setUserId("abcd")
        tradeRequestVO.setAmount(11)
        accountRepository.getById(account.getUser_id()) >> account
    }

    def "일반 매수"(){
        given:
        def stockInfo = new StockInfo("001", "BDD", price)
        stockInfoRepository.save(stockInfo)
        stockInfoRepository.getById(stockInfo.getTicker()) >> stockInfo
        tradeRequestVO.setBuying(true)
        portfolioRepository.getPortfolioByAccountIdAndTicker(account.getUser_id(),stockInfo.getTicker()) >> Optional.ofNullable()

        when:
        tradingService.buyingStocks(tradeRequestVO)

        then:
        account.getPoints() == result

        where:
        price  | result
        30000  | 670000
        2540   | 972060
        7777   | 914453
        100000 | 0
    }

    def "가지고 있는 종목 추가 매수"(){
        given:
        def stockInfo = new StockInfo("001", "BDD", defaultPrice)
        def stockUpdateDto = new StockUpdateDto()
        stockUpdateDto.setTicker("001")
        stockUpdateDto.setCompanyName("BDD")
        stockUpdateDto.setCurrentPrice(lowerPrice)
        stockInfoRepository.save(stockInfo)
        stockInfoRepository.getById(stockInfo.getTicker()) >> stockInfo
        tradeRequestVO.setBuying(true)
        portfolioRepository.getPortfolioByAccountIdAndTicker(account.getUser_id(),stockInfo.getTicker()) >> Optional.ofNullable()

        when:
        tradingService.buyingStocks(tradeRequestVO)
        stockUpdateDto.setCurrentPrice(lowerPrice)
        stockInfo.updateCurrentPrice(stockUpdateDto)
        tradingService.buyingStocks(tradeRequestVO)

        then:
        account.getPoints() == result

        where:
        lowerPrice  | defaultPrice  | higherPrice   || result
        1           | 2500          | 40000         || 972489
    }

    def "존재하지 않는 종목 매수"(){

    }

    def "가격이 0 이하인 종목 매수"(){

    }

    def "현금이 부족하여 매수할 수 없는 경우"(){

    }

    def "일부 매도"(){

    }

    def "전량 매도"(){

    }

    def "가지고 있지 않은 종목 매도"(){

    }
}

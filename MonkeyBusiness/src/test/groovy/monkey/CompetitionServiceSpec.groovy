package monkey

import monkey.controller.CompetitionController
import monkey.domain.account.Account
import monkey.domain.account.AccountId
import monkey.domain.account.AccountRepository
import monkey.domain.account.PortfolioRepository
import monkey.domain.competition.CompetitionCreateRequestDto
import monkey.domain.competition.CompetitionRepository
import monkey.domain.competition.RankingRepository
import monkey.domain.trading.StockInfo
import monkey.domain.trading.StockInfoRepository
import monkey.domain.trading.TradeRequestVO
import monkey.domain.trading.TradingLogRepository
import monkey.service.AccountService
import monkey.service.CompetitionService
import monkey.service.RankingService
import monkey.service.TradingService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.ResponseEntity
import org.springframework.test.annotation.Commit
import org.springframework.transaction.annotation.Transactional
import spock.lang.Specification
import spock.lang.Stepwise

import java.time.LocalDate

@Stepwise
@SpringBootTest
@Commit
class CompetitionServiceSpec extends Specification{
    @Autowired
    AccountRepository accountRepository
    @Autowired
    StockInfoRepository stockInfoRepository
    @Autowired
    CompetitionRepository competitionRepository
    @Autowired
    RankingRepository rankingRepository
    @Autowired
    TradingLogRepository tradingLogRepository
    @Autowired
    PortfolioRepository portfolioRepository

    @Autowired
    CompetitionService competitionService
    @Autowired
    RankingService rankingService
    @Autowired
    TradingService tradingService
    @Autowired
    AccountService accountService

    @Autowired
    CompetitionController competitionController

    def beforeTest() {
        given:
        accountRepository.deleteAll()
        stockInfoRepository.deleteAll()
        competitionRepository.deleteAll()
        rankingRepository.deleteAll()
        tradingLogRepository.deleteAll()
        portfolioRepository.deleteAll()

        expect:
        accountRepository.findAll().isEmpty()
        stockInfoRepository.findAll().isEmpty()
        competitionRepository.findAll().isEmpty()
        rankingRepository.findAll().isEmpty()
        tradingLogRepository.findAll().isEmpty()
        portfolioRepository.findAll().isEmpty()
    }

    def setupTest() {
        given:
        //account setup
        AccountId id1 = new AccountId("aaa", 0L)
        AccountId id2 = new AccountId("bbb", 0L)
        AccountId id3 = new AccountId("ccc", 0L)
        AccountId id4 = new AccountId("ddd", 0L)
        def account1 = new Account(id1, "abcd")
        def account2 = new Account(id2, "tester")
        def account3 = new Account(id3, "star")
        def account4 = new Account(id4, "nick")
        accountRepository.save(account1)
        accountRepository.save(account2)
        accountRepository.save(account3)
        accountRepository.save(account4)

        //stock setup
        StockInfo stock1 = new StockInfo("001", "stock1", 1000)
        StockInfo stock2 = new StockInfo("002", "stock2", 20000)
        StockInfo stock3 = new StockInfo("003", "stock3", 3000000)
        stockInfoRepository.save(stock1)
        stockInfoRepository.save(stock2)
        stockInfoRepository.save(stock3)
    }

    @Transactional
    def "대회 개최"() {
        given:
        CompetitionCreateRequestDto requestDto = new CompetitionCreateRequestDto("testComp", LocalDate.of(2022, 4, 12), LocalDate.of(2022, 4, 12))
        CompetitionCreateRequestDto requestDto2 = new CompetitionCreateRequestDto("willBeDeleted", LocalDate.of(2022, 4, 12), LocalDate.of(2022, 4, 12))

        when:
        ResponseEntity<String> response = competitionController.createCompetition(requestDto)
        ResponseEntity<String> response2 = competitionController.createCompetition(requestDto2)

        then:
        response.getStatusCodeValue() == 201
        response2.getStatusCodeValue() == 201
        competitionRepository.findCompetitionStartsToday().size() == 2
    }

    @Transactional
    def "대회 삭제"() {
        given:
        List<Long> compIds = competitionRepository.findCompetitionStartsToday()

        when:
        competitionController.deleteCompetition(compIds.get(1))

        then:
        competitionRepository.findCompetitionStartsToday().size() == 1
        competitionRepository.getById(compIds.get(0)).getName() == "testComp"
    }

    @Transactional
    def "대회 참가"() {
        given:
        Long compId = competitionRepository.findCompetitionStartsToday().get(0)

        when:
        AccountId id1 = new AccountId("aaa", compId)
        AccountId id2 = new AccountId("bbb", compId)
        AccountId id3 = new AccountId("ccc", compId)
        AccountId id4 = new AccountId("ddd", compId)
        competitionController.enrollParticipant(id1)
        competitionController.enrollParticipant(id2)
        competitionController.enrollParticipant(id3)
        competitionController.enrollParticipant(id4)

        then:
        accountRepository.findAllByCompetitionId(compId).size() == 4
    }

    @Transactional
    def "대회 조회"() {
        expect:
        competitionController.getCompetitions().getBody().get(0).getName() == "testComp"
        !competitionController.getCompetitions().getBody().get(0).isActive()
    }

    @Transactional
    def "대회 시작"() {
        given:
        Long compId = competitionRepository.findCompetitionStartsToday().get(0)
        competitionService.startCompetition(compId)

        expect:
        competitionRepository.getById(competitionRepository.findCompetitionStartsToday().get(0)).isActive()
    }

    @Transactional
    def "대회 상세"() {
        given:
        Long compId = competitionRepository.findCompetitionStartsToday().get(0)

        expect:
        competitionController.getCompetitionById(compId).getBody().getName() == "testComp"
    }

    @Transactional
    def "대회 진행"() {
        given:
        Long compId = competitionRepository.findCompetitionStartsToday().get(0)
        //competition progress
        //everyone buy same stock on same price
        TradeRequestVO tradeRequestVO = new TradeRequestVO()
        tradeRequestVO.setUserId("aaa")
        tradeRequestVO.setCompetitionId(compId)
        tradeRequestVO.setAmount(10)
        tradeRequestVO.setBuying(true)
        tradeRequestVO.setTicker("001")
        tradingService.buyingStocks(tradeRequestVO)
        tradeRequestVO.setUserId("bbb")
        tradingService.buyingStocks(tradeRequestVO)
        tradeRequestVO.setUserId("ccc")
        tradingService.buyingStocks(tradeRequestVO)
        tradeRequestVO.setUserId("ddd")
        tradingService.buyingStocks(tradeRequestVO)

        //but sell different time
        StockInfo s = stockInfoRepository.getById("001")
        s.updateCurrentPrice(1500)
        stockInfoRepository.save(s)
        tradeRequestVO.setBuying(false)
        //aaa earn 5000
        tradeRequestVO.setUserId("aaa")
        tradingService.sellingStocks(tradeRequestVO)
        //bbb earn 5000
        tradeRequestVO.setUserId("bbb")
        tradingService.sellingStocks(tradeRequestVO)
        //ccc earn 5010
        s.updateCurrentPrice(1501)
        tradeRequestVO.setUserId("ccc")
        tradingService.sellingStocks(tradeRequestVO)
        //ddd earn 10000
        s.updateCurrentPrice(2000)
        tradeRequestVO.setUserId("ddd")
        tradingService.sellingStocks(tradeRequestVO)
    }

    @Transactional
    def "현재 대회 랭킹 조회"() {
        given:
        Long compId = competitionRepository.findCompetitionStartsToday().get(0)

        when:
        def response = competitionController.getRankingData(compId).getBody()

        then:
        response.get(0).getNickname() == "nick"
        response.get(1).getTotalCapital() == 1005010
        response.get(2).getRank() == response.get(3).getRank()
    }

    @Transactional
    def "대회 종료"() {
        given:
        Long compId = competitionRepository.findCompetitionEndsToday().get(0)
        competitionService.endCompetition(compId)

        expect:
        !competitionRepository.getById(competitionRepository.findCompetitionEndsToday().get(0)).isActive()
    }

    @Transactional
    def "랭킹 기록"() {
        given:
        Long compId = competitionRepository.findCompetitionEndsToday().get(0)

        when:
        rankingService.storeRankData(compId)

        then:
        rankingRepository.findAll().get(0).getCompetitionId() == compId
    }

    @Transactional
    def "대회 참여 계좌 삭제"() {
        given:
        Long compId = competitionRepository.findCompetitionEndsToday().get(0)

        when:
        accountService.prepareDelete(compId)
        accountService.deleteAccountInCompetition(compId)

        then:
        accountRepository.findAllByCompetitionId(compId).isEmpty()
    }

    @Transactional
    def "지난 대회 랭킹 조회"() {
        given:
        Long compId = competitionRepository.findCompetitionEndsToday().get(0)

        when:
        def rank = competitionController.getRankingData(compId).getBody()

        then:
        rank.get(0).getNickname() == "nick"
        rank.get(1).getTotalCapital() == 1005010
        rank.get(2).getRank() == rank.get(3).getRank()
    }

    def teardown() {
        given:
        accountRepository.deleteAll()
        stockInfoRepository.deleteAll()
        competitionRepository.deleteAll()
        rankingRepository.deleteAll()
        tradingLogRepository.deleteAll()
        portfolioRepository.deleteAll()

        expect:
        accountRepository.findAll().isEmpty()
        stockInfoRepository.findAll().isEmpty()
        competitionRepository.findAll().isEmpty()
        rankingRepository.findAll().isEmpty()
        tradingLogRepository.findAll().isEmpty()
        portfolioRepository.findAll().isEmpty()
    }
}

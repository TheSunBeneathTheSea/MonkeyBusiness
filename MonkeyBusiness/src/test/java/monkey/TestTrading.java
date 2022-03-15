package monkey;

import monkey.domain.trading.StockUpdateDto;
import monkey.domain.trading.TradingStrategy;
import monkey.domain.user.User;
import monkey.domain.user.UserRepository;
import monkey.service.StockUpdateService;
import monkey.service.TradingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class TestTrading {
    @Autowired
    TradingService tradingService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    StockUpdateService stockUpdateService;

    @Test
    @Transactional
    public void buyingStock() throws IOException {
        User user = User.builder()
                .nickname("james")
                .build();

        userRepository.save(user);

    }
}

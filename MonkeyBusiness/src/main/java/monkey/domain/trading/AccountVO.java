package monkey.domain.trading;

import lombok.Data;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class AccountVO {
    private String user_id;
    private Long points;
    private Long capital;

    public AccountVO(Account account) {
        this.user_id = account.getUser_id();
        this.points = account.getPoints();
        this.capital = account.getHoldingStocks().stream().mapToLong(Portfolio::calculateValue).sum();
    }

    public static List<AccountVO> transformList(List<Account> accountList) {
        return accountList.stream().map(account -> new AccountVO(account)).collect(Collectors.toList());
    }
}

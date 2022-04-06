package monkey.domain.account;

import lombok.Data;

@Data
public class AccountSaveRequestDto {
    private String userId;
    private String nickname;
}

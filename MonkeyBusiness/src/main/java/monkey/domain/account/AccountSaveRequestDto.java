package monkey.domain.account;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor(staticName = "of")
public class AccountSaveRequestDto {
    private final String userId;
    private final String nickname;
}

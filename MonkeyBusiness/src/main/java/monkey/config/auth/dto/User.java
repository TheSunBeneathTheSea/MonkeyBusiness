package monkey.config.auth.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(staticName = "of")
public class User {
    private final String id;
    private final String nickname;
}

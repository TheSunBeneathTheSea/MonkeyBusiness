package monkey.service;

import lombok.RequiredArgsConstructor;
import monkey.domain.user.User;
import monkey.domain.user.UserRepository;
import monkey.domain.user.UserSaveRequestDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public String save(UserSaveRequestDto userSaveRequestDto) {
        User user = User.builder()
                .nickname(userSaveRequestDto.getNickname()).build();

        userRepository.save(user);

        return "user created. id: " + user.getId();
    }

    @Transactional
    public List<User> show() {
        return userRepository.findAll();
    }
}

package monkey.controller;

import lombok.RequiredArgsConstructor;
import monkey.domain.user.User;
import monkey.domain.user.UserSaveRequestDto;
import monkey.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class UserController {
    private final UserService userService;

    @PostMapping("/api/v1/user")
    public String save(@RequestBody String nickname) {
        UserSaveRequestDto saveRequestDto = new UserSaveRequestDto();
        saveRequestDto.setNickname(nickname);
        return userService.save(saveRequestDto);
    }

    @GetMapping("/api/v1/user")
    public ResponseEntity<List<User>> show() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.show());
    }
}

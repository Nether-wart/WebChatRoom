package io.github.nether_wart.controller;

import io.github.nether_wart.entity.ApiResponse;
import io.github.nether_wart.entity.User;
import io.github.nether_wart.entity.vo.request.UserLoginVO;
import io.github.nether_wart.repository.UserRepository;
import io.github.nether_wart.util.JwtUtil;
import io.github.nether_wart.util.PasswordUtil;
import jakarta.validation.Valid;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final PasswordUtil passwordUtil;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;


    public AuthController(PasswordUtil passwordUtil, JwtUtil jwtUtil, UserRepository userRepository) {
        this.passwordUtil = passwordUtil;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public ApiResponse<?> login(@RequestBody @Valid UserLoginVO loginVO) throws UsernameNotFoundException {
        if (loginVO.getUid() != null) {
            return match(loginVO.getPassword(), loginVO.getUid());
        } else if (loginVO.getName() != null) {
            User user = userRepository.findByName(loginVO.getName())
                    .orElseThrow(() -> new UsernameNotFoundException(loginVO.getName()));
            return match(loginVO.getPassword(), user.getUid());
        } else {
            return new ApiResponse<>(-3, "username or uid must be provided", null);
        }
    }

    private ApiResponse<?> match(String rawPassword, Long uid) {
        if (passwordUtil.matches(rawPassword, uid)) {
            return ApiResponse.success(jwtUtil.sign(uid));
        } else {
            return new ApiResponse<>(-2, "username or password incorrect", null);
        }
    }
}

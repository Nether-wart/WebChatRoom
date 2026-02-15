package io.github.nether_wart.controller;

import io.github.nether_wart.entity.ApiResponse;
import io.github.nether_wart.entity.User;
import io.github.nether_wart.entity.vo.request.UserRegVO;
import io.github.nether_wart.repository.UserRepository;
import io.github.nether_wart.service.UserService;
import io.github.nether_wart.util.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegController {
    private final UserRepository userRepository;
    private final UserService userService;
    private final JwtUtil jwtUtil;


    public RegController(UserRepository userRepository, UserService userService, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/api/auth/signup")
    public ApiResponse<?> signup(@RequestBody @Valid UserRegVO regVO) {
        if (userRepository.existsByName(regVO.getName())) {
            return new ApiResponse<>(-1, "username already exists", null);
        }
        User user = userService.createUser(regVO.getName(), regVO.getPassword());
        return ApiResponse.success(jwtUtil.sign(user.getUid()));
    }
}
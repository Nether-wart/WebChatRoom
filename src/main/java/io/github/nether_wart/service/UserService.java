package io.github.nether_wart.service;

import io.github.nether_wart.entity.User;
import io.github.nether_wart.repository.UserRepository;
import io.github.nether_wart.util.PasswordUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordUtil passwordUtil;

    public UserService(UserRepository userRepository, PasswordUtil passwordUtil) {
        this.userRepository = userRepository;
        this.passwordUtil = passwordUtil;
    }

    @Transactional
    public User createUser(String name, String rawPassword) {
        User user = new User();
        user.setName(name);
        user.setCreated(System.currentTimeMillis());
        userRepository.save(user);
        passwordUtil.save(rawPassword, user.getUid());
        return user;
    }
}

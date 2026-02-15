package io.github.nether_wart.util;

import io.github.nether_wart.entity.UserPassword;
import io.github.nether_wart.repository.PasswordRepository;
import io.github.nether_wart.repository.UserRepository;
import org.jspecify.annotations.Nullable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class PasswordUtil {

    private final BCryptPasswordEncoder encoder;
    private final PasswordRepository passwordRepository;


    public PasswordUtil(BCryptPasswordEncoder encoder, UserRepository userRepository, PasswordRepository passwordRepository) {
        this.encoder = encoder;
        this.passwordRepository = passwordRepository;
    }

    public void save(@Nullable CharSequence rawPassword, Long uid) {
        String salt = UUID.randomUUID().toString().replace("-", "");
        UserPassword user = new UserPassword();
        user.setUid(uid);
        user.setSalt(salt);
        user.setPassword(encoder.encode(rawPassword + salt));
        passwordRepository.save(user);
    }


    public boolean matches(@Nullable CharSequence rawPassword, Long uid) {
        UserPassword user = passwordRepository.findByUid(uid);
        if (user == null) return false;

        return encoder.matches(rawPassword + user.getSalt(), user.getPassword());
    }
}

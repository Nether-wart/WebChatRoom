package io.github.nether_wart.repository;

import io.github.nether_wart.entity.UserPassword;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordRepository extends JpaRepository<UserPassword, Long> {
    UserPassword findByUid(Long uid);
}

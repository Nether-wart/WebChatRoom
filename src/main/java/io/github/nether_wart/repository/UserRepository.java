package io.github.nether_wart.repository;

import io.github.nether_wart.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByName(String name);

    Optional<User> findByName(String name);

    Optional<User> findByUid(Long uid);

    List<User> findByNameContainsIgnoreCase(String name, Pageable pageable);

    List<User> findByCreatedAfter(long createdAfter, Pageable pageable);

    List<User> findByCreatedBefore(long createdAfter, Pageable pageable);

    List<User> findByCreatedBetween(long createdAfter, long createdBefore, Pageable pageable);
}

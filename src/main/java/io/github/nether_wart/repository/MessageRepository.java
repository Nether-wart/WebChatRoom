package io.github.nether_wart.repository;

import io.github.nether_wart.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
    Page<Message> findByRid(long rid, Pageable pageable);

    Page<Message> findByRidAndUid(long rid, long uid, Pageable pageable);

    Page<Message> findByRidAndCreatedAfter(long rid, long created, Pageable pageable);

    Page<Message> findByRidAndCreatedBefore(long rid, long created, Pageable pageable);

    Page<Message> findByRidAndCreatedBetween(long rid, long start, long end, Pageable pageable);
}

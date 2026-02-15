package io.github.nether_wart.repository;

import io.github.nether_wart.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {
    void deleteRoomByRidIs(Long rid);

    Optional<Room> findByRid(Long rid);
}

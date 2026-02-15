package io.github.nether_wart.controller;

import io.github.nether_wart.entity.ApiResponse;
import io.github.nether_wart.entity.Room;
import io.github.nether_wart.entity.User;
import io.github.nether_wart.entity.vo.request.RequestRoomVO;
import io.github.nether_wart.entity.vo.response.ResponseRoomVO;
import io.github.nether_wart.exception.NotFoundException;
import io.github.nether_wart.exception.UnauthorizedException;
import io.github.nether_wart.exception.VerifiedUserNotFoundException;
import io.github.nether_wart.repository.RoomRepository;
import io.github.nether_wart.repository.UserRepository;
import io.github.nether_wart.util.SecurityContextUtil;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api/room")
public class RoomController {
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    public RoomController(RoomRepository roomRepository, UserRepository userRepository) {
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/create")
    public ApiResponse<?> create(@RequestBody @Valid RequestRoomVO roomVO) {
        Long uid = SecurityContextUtil.getUid();
        if (uid == null) {
            throw new UnauthorizedException();//不应该达到这里，除非安全配置错误
        }

        var user = userRepository.findByUid(uid).orElseThrow(VerifiedUserNotFoundException::new);
        Room room = new Room();
        room.setPublic(roomVO.isPublic);
        room.setTraceless(roomVO.isTraceless);
        Set<User> users = new HashSet<>();
        users.add(user);
        room.setUsers(users);
        roomRepository.save(room);
        return ApiResponse.success(room.getRid());

    }

    @GetMapping("/findByRid/{rid}")
    public ApiResponse<?> findByRid(@PathVariable Long rid) {
        Room room = roomRepository.findByRid(rid).orElseThrow(NotFoundException::new);
        return ApiResponse.success(ResponseRoomVO.of(room));
    }

    @PostMapping("/addUser/{rid}")
    public ApiResponse<?> addUser(@PathVariable Long rid, @RequestParam Long uid) {
        Room room = roomRepository.findByRid(rid).orElseThrow(NotFoundException::new);
        if (isForbidden(room)) {
            return ApiResponse.forbidden();
        }
        User user = userRepository.findByUid(uid).orElseThrow(NotFoundException::new);
        room.getUsers().add(user);
        roomRepository.save(room);
        return ApiResponse.success();
    }

    private boolean isForbidden(Room room) {
        Long callerUid = SecurityContextUtil.getUid();
        if (room.isPublic()) return false;

        if (callerUid == null && !room.isPublic()) {
            return true;
        } else return room.getUsers().stream().noneMatch(u -> u.getUid().equals(callerUid));
    }
}

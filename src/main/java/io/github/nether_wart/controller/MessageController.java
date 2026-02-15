package io.github.nether_wart.controller;

import io.github.nether_wart.entity.ApiResponse;
import io.github.nether_wart.entity.Message;
import io.github.nether_wart.entity.PageResponse;
import io.github.nether_wart.entity.Room;
import io.github.nether_wart.exception.NotFoundException;
import io.github.nether_wart.repository.MessageRepository;
import io.github.nether_wart.repository.RoomRepository;
import io.github.nether_wart.util.SecurityContextUtil;
import jakarta.validation.constraints.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/api/message")
public class MessageController {
    private final MessageRepository messageRepository;
    private final RoomRepository roomRepository;

    public MessageController(MessageRepository messageRepository, RoomRepository roomRepository) {
        this.messageRepository = messageRepository;
        this.roomRepository = roomRepository;
    }

    @GetMapping("/findAll/{rid}")
    public ApiResponse<?> findAllByRid(@PathVariable Long rid,
                                       @RequestParam(defaultValue = "1") @Size() int page,
                                       @RequestParam(defaultValue = "100")
                                       @Size(min = 5, max = 1000, message = "page size must be between 5 and 1000") int pageSize) {
        page--;
        if (isForbidden(rid)) return ApiResponse.forbidden();
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "created"));
        Page<Message> messages = messageRepository.findByRid(rid, pageable);
        return ApiResponse.success(new PageResponse(messages.getTotalPages(), page, pageSize, messages.getContent()));
    }

    @GetMapping("/findByRidAndUid/{rid}")
    public ApiResponse<?> findByRidAndUid(
            @PathVariable Long rid, Long uid,
            @RequestParam(defaultValue = "1") @Size() int page,
            @RequestParam(defaultValue = "100")
            @Size(min = 5, max = 1000, message = "page size must be between 5 and 1000") int pageSize
    ) {
        page--;
        if (isForbidden(rid)) return ApiResponse.forbidden();

        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "created"));
        Page<Message> messages = messageRepository.findByRidAndUid(rid, uid, pageable);
        return ApiResponse.success(new PageResponse(messages.getTotalPages(), page, pageSize, messages.getContent()));
    }

    @GetMapping("/findByRidAndCreatedBetween/{rid}")
    public ApiResponse<?> findByRidAndCreatedBetween(
            @PathVariable Long rid, Long start, Long end,
            @RequestParam(defaultValue = "1") @Size() int page,
            @RequestParam(defaultValue = "100")
            @Size(min = 5, max = 1000, message = "page size must be between 5 and 1000") int pageSize
    ) {
        page--;
        if (isForbidden(rid)) return ApiResponse.forbidden();

        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "created"));
        Page<Message> messages = messageRepository.findByRidAndCreatedBetween(rid, start, end, pageable);
        return ApiResponse.success(new PageResponse(messages.getTotalPages(), page, pageSize, messages.getContent()));
    }

    @GetMapping("/findByRidAndCreatedAfter/{rid}")
    public ApiResponse<?> findByRidAndCreatedAfter(
            @PathVariable Long rid, long created,
            @RequestParam(defaultValue = "1") @Size() int page,
            @RequestParam(defaultValue = "100")
            @Size(min = 5, max = 1000, message = "page size must be between 5 and 1000") int pageSize
    ) {
        page--;
        if (isForbidden(rid)) return ApiResponse.forbidden();

        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "created"));
        Page<Message> messages = messageRepository.findByRidAndCreatedAfter(rid, created, pageable);
        return ApiResponse.success(new PageResponse(messages.getTotalPages(), page, pageSize, messages.getContent()));
    }

    @GetMapping("/findByRidAndCreatedBefore/{rid}")
    public ApiResponse<?> findByRidAndCreatedBefore(
            @PathVariable Long rid, long created,
            @RequestParam(defaultValue = "1") @Size() int page,
            @RequestParam(defaultValue = "100")
            @Size(min = 5, max = 1000, message = "page size must be between 5 and 1000") int pageSize
    ) {
        page--;
        if (isForbidden(rid)) return ApiResponse.forbidden();

        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "created"));
        Page<Message> messages = messageRepository.findByRidAndCreatedBefore(rid, created, pageable);
        return ApiResponse.success(new PageResponse(messages.getTotalPages(), page, pageSize, messages.getContent()));
    }

    private boolean isForbidden(Long rid) {
        Room room = roomRepository.findByRid(rid).orElseThrow(NotFoundException::new);
        Long callerUid = SecurityContextUtil.getUid();
        if (room.isPublic()) return false;

        if (callerUid == null && !room.isPublic()) {
            return true;
        } else return room.getUsers().stream().noneMatch(u -> u.getUid().equals(callerUid));
    }
}

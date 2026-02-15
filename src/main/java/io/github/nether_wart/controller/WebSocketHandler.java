package io.github.nether_wart.controller;

import io.github.nether_wart.entity.Room;
import io.github.nether_wart.entity.User;
import io.github.nether_wart.entity.vo.MessageVO;
import io.github.nether_wart.entity.vo.request.RequestEventVO;
import io.github.nether_wart.entity.vo.response.EventVO;
import io.github.nether_wart.entity.vo.response.events.ErrorVO;
import io.github.nether_wart.entity.vo.response.events.RoomInfo;
import io.github.nether_wart.exception.IllegalDataException;
import io.github.nether_wart.exception.VerifiedUserNotFoundException;
import io.github.nether_wart.repository.RoomRepository;
import io.github.nether_wart.repository.UserRepository;
import io.github.nether_wart.service.ChatService;
import io.github.nether_wart.util.JwtUtil;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.util.UriComponentsBuilder;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Optional;

@Component
public class WebSocketHandler extends TextWebSocketHandler {
    private final ObjectMapper objectMapper;
    private final ChatService chatService;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;

    public WebSocketHandler(ObjectMapper objectMapper, ChatService chatService, JwtUtil jwtUtil, UserRepository userRepository, RoomRepository roomRepository) {
        this.objectMapper = objectMapper;
        this.chatService = chatService;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.roomRepository = roomRepository;
    }

    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) {
        Long rid = getRid(session);
        if (rid == null) handleError(session, new ErrorVO("room not found"));
        else {
            Optional<Room> room = roomRepository.findByRid(rid);
            if (room.isEmpty()) handleError(session, new ErrorVO("room not found"));
            else {
                if (room.get().isPublic())
                    chatService.addUnAuthSession(rid, session);
            }
        }
    }

    @Override
    protected void handleTextMessage(@NonNull WebSocketSession session, TextMessage message) {
        RequestEventVO event;
        try {
            event = objectMapper.readValue(message.getPayload(), RequestEventVO.class);
        } catch (JacksonException | IllegalDataException e) {
            try {
                session.sendMessage(new TextMessage(
                        objectMapper.writeValueAsString(new EventVO(
                                "error", new ErrorVO("invalid json", false, true)
                        ))
                ));
            } catch (IOException _) {
            }
            return;
        }
        switch (event.type) {
            case "login.jwt" -> {
                try {
                    handleLogin(session, (String) event.data);
                } catch (VerifiedUserNotFoundException e) {
                    handleError(session, new ErrorVO("user not found"));
                }
            }
            case "message.simple" -> {
                if ("all".equals(getPermission(session))) {
                    Long rid = getRid(session);
                    if (rid == null) {
                        handleError(session, new ErrorVO("room not found"));
                        return;
                    }
                    chatService.onMessage(getUid(session), rid, (MessageVO) event.data);
                } else handleError(session, new ErrorVO("permission denied", false));
            }
        }
    }

    @NonNull
    private String getPermission(WebSocketSession session) {
        Object object = session.getAttributes().get("permission");
        if (object instanceof String permission) {
            return permission;
        }
        return "none";
    }

    @Nullable
    private Long getUid(WebSocketSession session) {
        Object object = session.getAttributes().get("uid");
        if (object instanceof Long uid) {
            return uid;
        }
        return null;
    }

    private Long getRid(WebSocketSession session) {
        if (session.getUri() == null) {
            return null;
        }
        String rid = UriComponentsBuilder
                .fromUri(session.getUri())
                .build()
                .getQueryParams()
                .getFirst("rid");

        if (rid == null) return null;
        try {
            return Long.parseLong(rid);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private void handleLogin(WebSocketSession session, String token) {
        if (jwtUtil.verify(token)) {
            User user = userRepository.findByUid(jwtUtil.extractId(token)).orElseThrow(VerifiedUserNotFoundException::new);
            session.getAttributes().put("uid", user.getUid());
            Optional<Room> room = roomRepository.findByRid(getRid(session));
            if (user.getAccessibleRooms()
                    .stream().mapToLong(Room::getRid)
                    .anyMatch(value -> {
                        Long rid = getRid(session);
                        if (rid == null) return false;
                        return rid.equals(value);
                    })
            ) {
                session.getAttributes().put("permission", "all");
                if (!room.get().isPublic())
                    chatService.authSession(getUid(session), session);
                try {
                    session.sendMessage(new TextMessage(objectMapper.writeValueAsString(
                            new EventVO("login.success",
                                    new RoomInfo(
                                            room.get().getDescription(),
                                            chatService.getOnlineUsersByRid(room.get().getRid()),
                                            chatService.countOpenSessionByRid(room.get().getRid()),
                                            (session.getTextMessageSizeLimit() / 4) - 20
                                    )
                            )
                    )));
                } catch (IOException _) {
                }
            } else {
                if (room.isEmpty()) {
                    handleError(session, new ErrorVO("room not found"));
                } else {
                    if (room.get().isPublic()) {
                        handleError(session, new ErrorVO("read only access", false));
                        session.getAttributes().put("permission", "read");
                    } else {
                        handleError(session, new ErrorVO("permission denied"));
                        session.getAttributes().put("permission", "none");
                    }
                    if (room.get().isTraceless()) {
                        session.getAttributes().put("traceless", "true");
                    }
                }
            }
        } else handleError(session, new ErrorVO("login failed", false));
    }

    private void handleError(WebSocketSession session, ErrorVO errorVO) {
        TextMessage message = new TextMessage(objectMapper.writeValueAsString(new EventVO("error", errorVO)));
        try {
            session.sendMessage(message);
        } catch (IOException _) {
        }
        if (errorVO.isFatal) {
            try {
                session.close(new CloseStatus(4000, "fatal error"));
            } catch (IOException _) {
            }
        }
    }


}
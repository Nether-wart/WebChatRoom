package io.github.nether_wart.service;

import io.github.nether_wart.entity.Message;
import io.github.nether_wart.entity.vo.MessageVO;
import io.github.nether_wart.entity.vo.response.EventVO;
import io.github.nether_wart.exception.IllegalDataException;
import io.github.nether_wart.repository.MessageRepository;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@Service
public class ChatService {
    private final Long2ObjectOpenHashMap<Set<WebSocketSession>> allSessions = new Long2ObjectOpenHashMap<>();
    private final Long2ObjectOpenHashMap<Set<WebSocketSession>> sessionByRid = new Long2ObjectOpenHashMap<>();
    private final Set<WebSocketSession> unAuthSession = new CopyOnWriteArraySet<>();
    private final MessageRepository messageRepository;
    private final ObjectMapper objectMapper;

    public ChatService(MessageRepository messageRepository, ObjectMapper objectMapper) {
        this.messageRepository = messageRepository;
        this.objectMapper = objectMapper;
    }

    public long countOpenSessionByRid(long rid) {
        return sessionByRid.get(rid).size();
    }

    //接口最多返回100个用户
    public Set<Long> getOnlineUsersByRid(long rid) {
        Set<WebSocketSession> sessions = sessionByRid.get(rid);
        Set<Long> result = new HashSet<>();
        for (WebSocketSession session : sessions) {
            if (session.isOpen() && result.size() < 100
                    && session.getAttributes().get("uid") != null) {
                result.add((Long) session.getAttributes().get("uid"));
            }
        }
        return result;
    }

    public void addUnAuthSession(long rid, WebSocketSession session) {
        unAuthSession.add(session);
        Set<WebSocketSession> roomSessions = sessionByRid.get(rid);
        if (roomSessions == null) {
            roomSessions = new CopyOnWriteArraySet<>();
        }
        roomSessions.add(session);
        sessionByRid.put(rid, roomSessions);
    }

    public void authSession(long uid, WebSocketSession session) {
        unAuthSession.remove(session);
        Set<WebSocketSession> userSessions = allSessions.get(uid);
        if (userSessions == null) {
            userSessions = new CopyOnWriteArraySet<>();
        }
        userSessions.add(session);
        allSessions.put(uid, userSessions);
    }

    public void onMessage(Long uid, long rid, MessageVO mVO) {
        Message message = new Message();
        message.setContent(mVO.content);
        message.setUid(uid);
        message.setRid(rid);
        if (mVO.isEnableHTML && mVO.isEnableMarkDown) {
            throw new IllegalDataException("HTML and Markdown cannot be enabled simultaneously");
        }
        message.setEnableHTML(mVO.isEnableHTML);
        message.setEnableMarkDown(mVO.isEnableMarkDown);
        messageRepository.save(message);
        publishMessage(rid, message);
    }

    public void publishMessage(long rid, Message message) {
        for (WebSocketSession session : sessionByRid.get(rid)) {
            if (session.isOpen()) {
                try {
                    session.sendMessage(new TextMessage(objectMapper.writeValueAsString(
                            new EventVO("message.simple", message))));
                } catch (IOException _) {
                }
            }
        }
    }

    @Scheduled(fixedRate = 1000)
    public void clearClosedSessions() {
        remove(allSessions);
        remove(sessionByRid);
        unAuthSession.removeIf(session -> !session.isOpen());
    }

    private void remove(Long2ObjectOpenHashMap<Set<WebSocketSession>> map) {
        Iterator<Set<WebSocketSession>> iterator = map.values().iterator();
        while (iterator.hasNext()) {
            Set<WebSocketSession> sessions = iterator.next();
            sessions.removeIf(session -> !session.isOpen());
            if (sessions.isEmpty()) {
                iterator.remove();
            }
        }
    }
}

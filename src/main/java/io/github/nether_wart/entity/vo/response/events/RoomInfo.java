package io.github.nether_wart.entity.vo.response.events;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Set;

public class RoomInfo {
    public String description;
    @JsonProperty("limited_online_users")
    public Set<Long> limitedOnlineUsers;
    @JsonProperty("online_session_num")
    public long onlineSessionsNum;
    @JsonProperty("max_message_size")
    public int maxMessageSize;

    public RoomInfo(String description, Set<Long> limitedOnlineUsers, long onlineSessionsNum, int maxMessageSize) {
        this.description = description;
        this.limitedOnlineUsers = limitedOnlineUsers;
        this.onlineSessionsNum = onlineSessionsNum;
        this.maxMessageSize = maxMessageSize;
    }
}

package io.github.nether_wart.entity.vo.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.nether_wart.entity.Room;
import io.github.nether_wart.entity.User;

import java.util.Set;
import java.util.stream.Collectors;

public class ResponseRoomVO {
    public Long rid;
    public String description;
    @JsonProperty("is_public")
    public boolean isPublic;
    @JsonProperty("is_traceless")
    public boolean isTraceless;
    public Set<Long> users;

    public ResponseRoomVO(Long rid, String description, boolean isPublic, boolean isTraceless, Set<Long> users) {
        this.rid = rid;
        this.description = description;
        this.isPublic = isPublic;
        this.isTraceless = isTraceless;
        this.users = users;
    }

    public static ResponseRoomVO of(Room room) {
        return new ResponseRoomVO(
                room.getRid(), room.getDescription(), room.isPublic(), room.isTraceless(),
                room.getUsers().stream().mapToLong(User::getUid).boxed().collect(Collectors.toSet())
        );
    }
}

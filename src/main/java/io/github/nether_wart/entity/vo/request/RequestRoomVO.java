package io.github.nether_wart.entity.vo.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.Size;

public class RequestRoomVO {
    @JsonAlias("is_public")
    public boolean isPublic = true;
    @JsonAlias("is_traceless")
    public boolean isTraceless = false;
    @Size(min = 1, max = 512)
    public String description;
}

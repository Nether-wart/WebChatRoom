package io.github.nether_wart.entity.vo.request;

import io.github.nether_wart.util.EventDeserializer;
import tools.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(using = EventDeserializer.class)
public class RequestEventVO {
    public String type;
    /*type与body的关系
    login.jwt->String
    message.simple->io.github.nether_wart.entity.vo.MessageVO
     */
    public Object data;
}

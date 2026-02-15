package io.github.nether_wart.entity.vo.response;

public class EventVO {
    public String type;
    /*type与body的关系
    message.simple-> io.github.nether_wart.entity.vo.response.events.ErrorVO.MessageVO
    login.success-> io.github.nether_wart.entity.vo.response.events.RoomInfo
    user.join-> String
    user.left-> String
    error-> io.github.nether_wart.entity.vo.response.events.ErrorVO
    */
    public Object body;

    public EventVO(String type, Object body) {
        this.type = type;
        this.body = body;
    }
}

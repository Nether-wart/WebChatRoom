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
    public Object data;

    public EventVO(String type, Object data) {
        this.type = type;
        this.data = data;
    }
}

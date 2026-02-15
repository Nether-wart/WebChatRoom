package io.github.nether_wart.util;

import io.github.nether_wart.entity.vo.MessageVO;
import io.github.nether_wart.entity.vo.request.RequestEventVO;
import io.github.nether_wart.exception.IllegalDataException;
import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.deser.std.StdDeserializer;

public class EventDeserializer extends StdDeserializer<RequestEventVO> {
    public EventDeserializer() {
        super(RequestEventVO.class);
    }

    @Override
    public RequestEventVO deserialize(JsonParser p, DeserializationContext ctxt) throws JacksonException {
        JsonNode root = p.readValueAsTree();
        String type = root.get("type").asString();
        JsonNode data = root.get("data");

        if (type != null && data != null) {
            RequestEventVO eventVO = new RequestEventVO();
            eventVO.type = type;
            eventVO.data = switch (type) {
                case "login.jwt" -> data.asString();
                case "message.simple" -> ctxt.readTreeAsValue(data, MessageVO.class);
                default -> null;
            };
            return eventVO;
        }
        throw new IllegalDataException("unknown data type");
    }
}

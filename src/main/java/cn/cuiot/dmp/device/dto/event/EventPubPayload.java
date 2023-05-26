package cn.cuiot.dmp.device.dto.event;

public class EventPubPayload extends EventBasePayload {

    public EventPubPayload(String messageId, EventParam params) {
        super(messageId);
        this.params = params;
    }

    public EventParam params;
}

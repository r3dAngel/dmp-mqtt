package cn.cuiot.dmp.device.dto.event;

public class EventBasePayload {

    public EventBasePayload(String messageId) {
        this.messageId = messageId;
    }

    public String messageId;

}

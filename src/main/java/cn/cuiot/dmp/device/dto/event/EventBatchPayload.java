package cn.cuiot.dmp.device.dto.event;

public class EventBatchPayload extends EventBasePayload {


    public EventBatchPayload(String messageId, EventPubData params) {
        super(messageId);
        this.params = params;
    }

    public EventPubData params;
}

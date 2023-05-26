package cn.cuiot.dmp.device.dto.property;

public class PropertyBatchPayload extends PropertyBasePayload {

    public PropertyBatchPayload(String messageId, PropertyBatchParams params) {
        super(messageId);
        this.params = params;
    }

    public PropertyBatchParams params;
}

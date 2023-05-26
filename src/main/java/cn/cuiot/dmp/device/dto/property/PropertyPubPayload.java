package cn.cuiot.dmp.device.dto.property;

public class PropertyPubPayload extends PropertyBasePayload {

    public PropertyPubPayload(String messageId, PropertyKeyValue params) {
        super(messageId);
        this.params = params;
    }

    public PropertyKeyValue params;

}

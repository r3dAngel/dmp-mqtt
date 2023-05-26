package cn.cuiot.dmp.device.dto.property;

public class PropertyKeyValue {

    public PropertyKeyValue(String key, String value, String ts) {
        this.key = key;
        this.value = value;
        this.ts = ts;
    }

    public String key;

    public String value;

    public String ts;

}

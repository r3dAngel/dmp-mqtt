package cn.cuiot.dmp.device.dto.property;

import java.util.List;

public class PropertyBatchParams {

    public PropertyBatchParams(List<PropertyKeyValue> data) {
        this.data = data;
    }

    public List<PropertyKeyValue> data;
}

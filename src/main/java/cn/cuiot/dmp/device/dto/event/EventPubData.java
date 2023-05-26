package cn.cuiot.dmp.device.dto.event;

import java.util.List;

public class EventPubData {

    public List<EventParam> data;

    public EventPubData(List<EventParam> data) {
        this.data = data;
    }
}

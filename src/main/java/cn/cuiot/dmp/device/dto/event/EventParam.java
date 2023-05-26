package cn.cuiot.dmp.device.dto.event;

import java.util.List;

public class EventParam {

    public EventParam(String key, String ts, List<EventInfo> info) {
        this.key = key;
        this.ts = ts;
        this.info = info;
    }

    public String key;
    public String ts;
    public List<EventInfo> info;

}

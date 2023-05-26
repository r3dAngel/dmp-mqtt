package cn.cuiot.dmp.device.bussiness;

import cn.cuiot.dmp.device.util.MqttUtil;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.Arrays;
import java.util.List;

/**
 * 该接口用于设备上传事件
 */
@Slf4j
public class EventPub {
    public static void main(String[] args) {
        //用户填写，10-48位字母数字"-"
        String deviceId = "qwert987456321";
        //平台新建设备后生成
        String deviceKey = "UFxwA78H0a0u8uh";
        //平台新建设备后生成
        String deviceSecret = "A40082564DA479B324CA710052C6947B";
        //平台新建产品时生成
        String productKey = "cucpa77i58vdcYdG";
        //消息Id,自行填写
        String messageId = "132456";
        //平台服务地址 格式： "dmp-mqtt.cuiot.cn:1883"
        String host = "tcp://dmp-mqtt.cuiot.cn:1883";
        //事件1的参数  第一位是事件key； 往后依次是参数key、参数value
        List<String> eventList1 = Arrays.asList("event1", "property1", "value1", "property2", "value2");
        //新增事件按照此格式添加
        List<String> eventList2 = Arrays.asList("event2", "property3", "value1", "property4", "value2", "property5", "value2");
        List<List<String>> eventList = Arrays.asList(eventList1, eventList2);

        //工具类
        MqttUtil mqttUtil = new MqttUtil();
        try {
            //初始化mqttClient
            mqttUtil.connectClient(host, deviceId + "|" + productKey + "|0|0|0", deviceKey + "|" + productKey,
                    mqttUtil.generatePassword(deviceId, deviceKey, productKey, deviceSecret));
            //订阅
            mqttUtil.sub(productKey, deviceKey);
            //上报
            mqttUtil.pubEvent(messageId, eventList, productKey, deviceKey);
            //等待一段时间
            Thread.sleep(10 * 1000);
            //断开连接
            mqttUtil.disconnectClient();
        } catch (MqttException e) {
            log.info("!!! Reason Code is: " + e.getReasonCode());
            log.info("!!! Message is: " + e.getMessage());
            log.info("!!! Exception is: " + e);
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //关闭客户端
            mqttUtil.closeClient();
            //退出代码
            System.exit(0);
        }
    }
}

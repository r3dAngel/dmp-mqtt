package cn.cuiot.dmp.device.bussiness;

import cn.cuiot.dmp.device.util.MqttUtil;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttException;

public class PropertyPubTest {
    public static void main(String[] args) {
        //用户填写，10-48位字母数字"-"
        String deviceId = "zhiyangji01";
        //平台新建设备后生成
        String deviceKey = "SFIefovTnfRPiV6";
        //平台新建设备后生成
        String deviceSecret = "D3DE73DA544E82753C034D22B7CACAE4";
        //平台新建产品时生成
        String productKey = "cu59flm720qarrke";
        //消息Id，自行填写
        String messageId = "132456";
        //平台服务地址 格式： "dmp-mqtt.cuiot.cn:1883"
        String host = "tcp://dmp-mqtt.cuiot.cn:1883";
        //属性key，从产品物模型中获取
//        String[] propertyKeys = {"manufacturer", "deviceType", "IMEI", "oxygenFlow"};
        String[] propertyKeys = {"oxygenFlow"};
        //属性value，范围与类型从物模型中获取
//        String[] propertyValues = {"value4", "55", "0", "8"};
        String[] propertyValues = {"8"};

        //工具类
        MqttUtil mqttUtil = new MqttUtil();
        try {
            //初始化mqttClient
            mqttUtil.connectClient(host, deviceId + "|" + productKey + "|0|0|0", deviceKey + "|" + productKey,
                    mqttUtil.generatePassword(deviceId, deviceKey, productKey, deviceSecret));
            //订阅
            mqttUtil.sub(productKey, deviceKey);
            //上报
            mqttUtil.pubProperty(productKey, deviceKey, messageId, propertyKeys, propertyValues);
        } catch (MqttException e) {
            System.out.println("!!! Reason Code is: " + e.getReasonCode());
            System.out.println("!!! Message is: " + e.getMessage());
            System.out.println("!!! Exception is: " + e);
            e.printStackTrace();
        }
    }
}

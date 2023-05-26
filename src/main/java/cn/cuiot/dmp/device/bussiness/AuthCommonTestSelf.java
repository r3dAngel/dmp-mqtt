package cn.cuiot.dmp.device.bussiness;

import cn.cuiot.dmp.device.util.MqttUtil;
import org.eclipse.paho.client.mqttv3.MqttException;

public class AuthCommonTestSelf {
    public static void main(String[] args) {
        /*//用户填写，10-48位字母数字"-"
        String deviceId = "zhiyangji1";
        //平台新建设备后生成
        String deviceKey = "4wf35tv4gDaomfR";
        //平台新建设备后生成
        String deviceSecret = "B867B2C9D385B685E44A40C13F509455";
        //平台新建产品时生成
        String productKey = "cu1txpab9hycjgfX";*/

        //用户填写，10-48位字母数字"-"
        String deviceId = "huiyiji01";
        //平台新建设备后生成
        String deviceKey = "3Xhi8Cmd7OMQLW3";
        //平台新建设备后生成
        String deviceSecret = "91B882F3BC8C5F9819D02AD830FF54D4";
        //平台新建产品时生成
        String productKey = "cu1gge9qa8x7y6vo";

        //平台服务地址 格式： "dmp-mqtt.cuiot.cn:1883"
        String host = "tcp://dmp-mqtt.cuiot.cn:1883";
        //工具类
        MqttUtil mqttUtil = new MqttUtil();
        try {
            //初始化
            mqttUtil.connectClient(host, deviceId + "|" + productKey + "|0|0|0", deviceKey + "|" + productKey,
                    mqttUtil.generatePassword(deviceId, deviceKey, productKey, deviceSecret));
            //订阅
            mqttUtil.sub(productKey, deviceKey);
        } catch (MqttException e) {
            System.out.println("!!! Reason Code is: " + e.getReasonCode());
            System.out.println("!!! Message is: " + e.getMessage());
            System.out.println("!!! Exception is: " + e);
            e.printStackTrace();
        }
    }
}

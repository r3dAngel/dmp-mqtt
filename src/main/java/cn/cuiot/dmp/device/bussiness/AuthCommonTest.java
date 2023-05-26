package cn.cuiot.dmp.device.bussiness;

import cn.cuiot.dmp.device.util.MqttUtil;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttException;

public class AuthCommonTest {
    public static void main(String[] args) {
        // 设备1
        //用户填写，10-48位字母数字"-"
/*        String deviceId1 = "zhiyangji01";
        //平台新建设备后生成
        String deviceKey1 = "SFIefovTnfRPiV6";
        //平台新建设备后生成
        String deviceSecret1 = "D3DE73DA544E82753C034D22B7CACAE4";
        //平台新建产品时生成
        String productKey1 = "cu59flm720qarrke";
        //平台服务地址 格式： "dmp-mqtt.cuiot.cn:1883"

        // 设备2
        //用户填写，10-48位字母数字"-"
        String deviceId2 = "xuetangyi01";
        //平台新建设备后生成
        String deviceKey2 = "P8948q8ma0kPY5r";
        //平台新建设备后生成
        String deviceSecret2 = "9217E1574B1F8AC82BC45147E629217D";
        //平台新建产品时生成
        String productKey2 = "cu697dfal4iwbzND";
        //平台服务地址 格式： "dmp-mqtt.cuiot.cn:1883"*/

        // 设备3
        //用户填写，10-48位字母数字"-"
        String deviceId3 = "xueyayi01";
        //平台新建设备后生成
        String deviceKey3 = "mMKjziAOo1q3ehQ";
        //平台新建设备后生成
        String deviceSecret3 = "A7810D47B3E985ACFEE50B097FE1FD29";
        //平台新建产品时生成
        String productKey3 = "cu1gqwdbkr9mqxz3";
        //平台服务地址 格式： "dmp-mqtt.cuiot.cn:1883"

/*       // 设备4
        //用户填写，10-48位字母数字"-"
        String deviceId4 = "huxiji01";
        //平台新建设备后生成
        String deviceKey4 = "Jw7Zwy7ifUAwG9U";
        //平台新建设备后生成
        String deviceSecret4 = "947B4C03228AF433352E8EB87E3D3831";
        //平台新建产品时生成
        String productKey4 = "cu1caux4y0ynyavT";
        //平台服务地址 格式： "dmp-mqtt.cuiot.cn:1883"*/

        // 设备5
        //用户填写，10-48位字母数字"-"
        String deviceId5 = "xueyangyi01";
        //平台新建设备后生成
        String deviceKey5 = "YPBQ5eOpi5pvwTs";
        //平台新建设备后生成
        String deviceSecret5 = "7D9FBDEF9AFDF087E5836C2EB2C0FAC7";
        //平台新建产品时生成
        String productKey5 = "cu1gwpy8r7t14vbo";
        //平台服务地址 格式： "dmp-mqtt.cuiot.cn:1883"

        String host = "tcp://dmp-mqtt.cuiot.cn:1883";
        //工具类
        MqttUtil mqttUtil = new MqttUtil();
        try {
/*            //初始化
            mqttUtil.connectClient(host, deviceId1 + "|" + productKey1 + "|0|0|0", deviceKey1 + "|" + productKey1,
                    mqttUtil.generatePassword(deviceId1, deviceKey1, productKey1, deviceSecret1));
            //订阅
            mqttUtil.sub(productKey1,deviceKey1);

            //初始化
            mqttUtil.connectClient(host, deviceId2 + "|" + productKey2 + "|0|0|0", deviceKey2 + "|" + productKey2,
                    mqttUtil.generatePassword(deviceId2, deviceKey2, productKey2, deviceSecret2));
            //订阅
            mqttUtil.sub(productKey2,deviceKey2);*/


            //初始化
            mqttUtil.connectClient(host, deviceId3 + "|" + productKey3 + "|0|0|0", deviceKey3 + "|" + productKey3,
                    mqttUtil.generatePassword(deviceId3, deviceKey3, productKey3, deviceSecret3));
            //订阅
            mqttUtil.sub(productKey3, deviceKey3);


/*           //初始化
            mqttUtil.connectClient(host, deviceId4 + "|" + productKey4 + "|0|0|0", deviceKey4 + "|" + productKey4,
                    mqttUtil.generatePassword(deviceId4, deviceKey4, productKey4, deviceSecret4));
            //订阅
            mqttUtil.sub(productKey4,deviceKey4);*/


 /*            //初始化
            mqttUtil.connectClient(host, deviceId5 + "|" + productKey5 + "|0|0|0", deviceKey5 + "|" + productKey5,
                    mqttUtil.generatePassword(deviceId5, deviceKey5, productKey5, deviceSecret5));
            //订阅
            mqttUtil.sub(productKey5,deviceKey5);*/


        } catch (MqttException e) {
            System.out.println("!!! Reason Code is: " + e.getReasonCode());
            System.out.println("!!! Message is: " + e.getMessage());
            System.out.println("!!! Exception is: " + e);
            e.printStackTrace();
        }
    }
}

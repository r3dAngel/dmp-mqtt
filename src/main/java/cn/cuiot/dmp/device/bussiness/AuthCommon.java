package cn.cuiot.dmp.device.bussiness;

import cn.cuiot.dmp.device.util.MqttUtil;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttException;

/**
 * 该接口用于mqtt设备使用一机一密方式注册上线
 */
@Slf4j
public class AuthCommon {
    public static void main(String[] args) {
        //用户填写，10-48位字母数字"-"
        String deviceId = "zhiyangji01";
        //平台新建设备后生成
        String deviceKey = "SFIefovTnfRPiV6";
        //平台新建设备后生成
        String deviceSecret = "D3DE73DA544E82753C034D22B7CACAE4";
        //平台新建产品时生成
        String productKey = "cu59flm720qarrke";
        //平台服务地址 格式： "dmp-mqtt.cuiot.cn:1883"
        String host = "tcp://dmp-mqtt.cuiot.cn:1883";
        //工具类
        MqttUtil mqttUtil = new MqttUtil();
        try {
            //初始化
            mqttUtil.connectClient(host, deviceId + "|" + productKey + "|0|0|1", deviceKey + "|" + productKey,
                    mqttUtil.generatePassword(deviceId, deviceKey, productKey, deviceSecret));
            //订阅
            mqttUtil.sub(productKey, deviceKey);
            //等待一段时间
            Thread.sleep(10 * 1000);
            //断开连接
            mqttUtil.disconnectClient();
        } catch (MqttException e) {
            log.warn("!!! Reason Code is: " + e.getReasonCode());
            log.warn("!!! Message is: " + e.getMessage());
            log.warn("!!! Exception is: " + e);
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

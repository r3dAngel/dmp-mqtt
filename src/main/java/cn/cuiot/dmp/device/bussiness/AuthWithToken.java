package cn.cuiot.dmp.device.bussiness;

import cn.cuiot.dmp.device.util.MqttUtil;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttException;

/**
 * 该接口用于设备通过token上线
 */
@Slf4j
public class AuthWithToken {
    public static void main(String[] args) {
        //用户填写，10-48位字母数字"-"
        String deviceId = "12369874512369874510";
        //平台新建设备后生成
        String deviceKey = "20210609deviceKey01";
        //一型一密免预注册之后获取
        String token = "7552265751fbca6e643a0e68626b74ed9ff180fc453f8b3ff837ab4f01e07b31";
        //平台新建产品时生成
        String productKey = "cu559dtdb0tuk4BC";
        //平台服务地址 格式： "dmp-mqtt.cuiot.cn:1883"
//        String host = "dmp-mqtt.cuiot.cn:1883";
        String host = "tcp://172.30.125.50:1883";
        //工具类
        MqttUtil mqttUtil = new MqttUtil();
        try {
            //初始化
            mqttUtil.connectClient(host, deviceId + "|" + productKey + "|0|-2|1", deviceKey + "|" + productKey, token);
            //订阅
            mqttUtil.sub(productKey, deviceKey);
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

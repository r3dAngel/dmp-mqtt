package cn.cuiot.dmp.device.util;

import cn.cuiot.dmp.device.dto.event.*;
import cn.cuiot.dmp.device.dto.property.*;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class MqttUtil implements MqttCallbackExtended {

    public static MqttClient client;

    /**
     * 生成密码： 一机一密鉴权上报    一型一密获取deviceSecret后鉴权、上报
     *
     * @param deviceId
     * @param deviceKey
     * @param productKey
     * @param secret
     * @return
     * @throws MqttException
     */
    public String generatePassword(String deviceId, String deviceKey, String productKey, String secret) throws MqttException {
        //校验与生成密码
        if ((productKey == null || deviceKey == null) || deviceId == null) {
            log.warn("!!! Necessary param needed");
            throw new MqttException(MqttException.REASON_CODE_CLIENT_EXCEPTION);
        }

        String message = deviceId + deviceKey + productKey;

        if (secret == null) {
            log.warn("!!! DeviceSecret can not be empty");
            throw new MqttException(MqttException.REASON_CODE_CLIENT_EXCEPTION);
        } else {
            return sha256Hmac(message, secret);
        }

    }

    /**
     * 配置mqtt客户端
     *
     * @param clientId
     * @param username
     * @param password
     * @return
     * @throws MqttException
     */
    public void connectClient(String host, String clientId, String username, String password) throws MqttException {
        MemoryPersistence persistence = new MemoryPersistence();
        client = new MqttClient(host, clientId, persistence);
        MqttConnectOptions connOptions = new MqttConnectOptions();
        connOptions.setCleanSession(true);
        connOptions.setKeepAliveInterval(3 * 60);
        connOptions.setConnectionTimeout(10);
        connOptions.setAutomaticReconnect(false);
        connOptions.setUserName(username);
        connOptions.setPassword(password.toCharArray());
        client.setCallback(this);
        client.connect(connOptions);
    }

    public void sub(String productKey, String deviceKey) {
        if (client == null) {
            log.info("!!! Client not initialized...");
            return;
        }
        //订阅属性上报回复topic
        String topicPreRegister = String.format("$sys/%s/%s/ext/regist", productKey, deviceKey);
        String topicAutoRegister = String.format("$sys/%s/%s/ext/autoregist", productKey, deviceKey);
        String topicPropertyReply = String.format("$sys/%s/%s/property/pub_reply", productKey, deviceKey);
        String topicBatchPropertyReply = String.format("$sys/%s/%s/property/batch_reply", productKey, deviceKey);
        String topicEventReply = String.format("$sys/%s/%s/event/pub_reply", productKey, deviceKey);
        String topicBatchEventReply = String.format("$sys/%s/%s/event/batch_reply", productKey, deviceKey);
        String[] batchTopic = {topicPreRegister, topicAutoRegister, topicPropertyReply,
                topicBatchPropertyReply, topicEventReply, topicBatchEventReply};
        try {
            client.subscribe(batchTopic);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成属性上报报文
     * <p>
     * 单个
     * {
     * "messageId": "123",
     * "params": {
     * "key": "temperature",
     * "value": 23.4,
     * "ts": "1528292272000"
     * }
     * }
     * <p>
     * 批量
     * {
     * "messageId": "123",
     * "params": {
     * "data": [{
     * "key": "myattribute1",
     * "value": "my attribute1 Value",
     * "ts": "1528292272000"
     * },
     * {
     * "key": "myattribute2",
     * "value": "my attribute2 Value",
     * "ts": "1528292272000"
     * }
     * ]
     * }
     * }
     *
     * @param productKey
     * @param deviceKey
     * @param messageId
     * @param propertyKeys
     * @param propertyValues
     * @return
     * @throws MqttException
     */
    public void pubProperty(String productKey, String deviceKey,
                            String messageId, String[] propertyKeys, String[] propertyValues) throws MqttException {
        String ts = System.currentTimeMillis() - 10 * 1000 + "";
        //属性key-value Map
        Map<String, Object> keyValues = new HashMap<>();
        //将属性key-value放入map
        if (propertyKeys.length != propertyValues.length) {
            log.warn("!!! propertyKey and propertyValue are not matched...");
            throw new MqttException(
                    MqttException.REASON_CODE_CLIENT_EXCEPTION);
        }
        for (int i = 0; i < propertyKeys.length; i++) {
            keyValues.put(propertyKeys[i], propertyValues[i]);
        }

        PropertyBasePayload propertyBasePayload;
        List<String> propertyKeysList = new ArrayList<>(keyValues.keySet());
        if (keyValues.size() == 1) {
            PropertyKeyValue propertyKeyValue = new PropertyKeyValue(propertyKeysList.get(0), (String) keyValues.get(propertyKeysList.get(0)), ts);
            propertyBasePayload = new PropertyPubPayload(messageId, propertyKeyValue);
            client.publish(String.format("$sys/%s/%s/property/pub", productKey, deviceKey),
                    new MqttMessage(JSON.toJSONString(propertyBasePayload).getBytes(StandardCharsets.UTF_8)));
        } else {
            List<PropertyKeyValue> data = new ArrayList<>();
            for (int i = 0; i < propertyKeysList.size(); i++) {
                PropertyKeyValue propertyKeyValue = new PropertyKeyValue(propertyKeysList.get(i), (String) keyValues.get(propertyKeysList.get(i)), ts);
                data.add(propertyKeyValue);
            }
            PropertyBatchParams params = new PropertyBatchParams(data);
            propertyBasePayload = new PropertyBatchPayload(messageId, params);
            client.publish(String.format("$sys/%s/%s/property/batch", productKey, deviceKey),
                    new MqttMessage(JSON.toJSONString(propertyBasePayload).getBytes(StandardCharsets.UTF_8)));
        }
    }

    /**
     * 生成事件报文
     * <p>
     * 单个
     * {
     * "messageId": "123",
     * "params": {
     * "key": "alarm",
     * "ts": "1528292272000",
     * "info": [{
     * "key": "status1",
     * "value": "ON"
     * },
     * {
     * "key": "status2",
     * "value": "OFF"
     * }
     * ]
     * }
     * }
     * <p>
     * 批量
     * {
     * "messageId": "123",
     * "params": {
     * "data": [{
     * "key": "myattribute1",
     * "ts": "1528292272000",
     * "info": [{
     * "key": "status1",
     * "value": "ON"
     * },
     * {
     * "key": "status2",
     * "value": "OFF"
     * }
     * ]
     * },
     * {
     * "key": "myattribute2",
     * "ts": "1528292272000",
     * "info": [{
     * "key": "status1",
     * "value": "ON"
     * },
     * {
     * "key": "status2",
     * "value": "OFF"
     * }
     * ]
     * }
     * ]
     * }
     * }
     *
     * @param messageId
     * @param eventList
     * @param productKey
     * @param deviceKey
     * @return
     * @throws MqttException
     */
    public void pubEvent(String messageId, List<List<String>> eventList, String productKey, String deviceKey) throws MqttException {
        String ts = System.currentTimeMillis() - 10 * 1000 + "";
        Map<String, Map<String, String>> eventMap = new HashMap<>();
        for (int i = 0; i < eventList.size(); i++) {
            List<String> evenAndParamList = eventList.get(i);
            Map<String, String> eventParamMap = new HashMap<>();
            for (int j = 1; j < evenAndParamList.size(); j += 2) {
                eventParamMap.put(evenAndParamList.get(j), evenAndParamList.get(j + 1));
            }
            eventMap.put(evenAndParamList.get(0), eventParamMap);
        }


        EventBasePayload eventPayload;
        List<EventParam> data = new ArrayList<EventParam>();
        EventParam eventParam;
        EventPubData params;
        List<String> eventKeyList = new ArrayList<>(eventMap.keySet());
        if (eventMap.size() == 1) {
            Map<String, String> eventParamsMap = eventMap.get(eventKeyList.get(0));
            List<String> eventParamKeyList = new ArrayList<>(eventParamsMap.keySet());
            List<EventInfo> eventInfoList = new ArrayList<EventInfo>();
            for (int i = 0; i < eventParamKeyList.size(); i++) {
                EventInfo eventInfo = new EventInfo(eventParamKeyList.get(0), eventParamsMap.get(eventParamKeyList.get(0)));
                eventInfoList.add(eventInfo);
            }
            eventParam = new EventParam(eventKeyList.get(0), ts, eventInfoList);
            eventPayload = new EventPubPayload(messageId, eventParam);
            client.publish(String.format("$sys/%s/%s/event/pub", productKey, deviceKey),
                    new MqttMessage(JSON.toJSONString(eventPayload).getBytes(StandardCharsets.UTF_8)));
        } else {
            for (int i = 0; i < eventList.size(); i++) {
                Map<String, String> eventParamsMap = eventMap.get(eventKeyList.get(0));
                List<String> eventParamKeyList = new ArrayList<>(eventParamsMap.keySet());
                List<EventInfo> eventInfoList = new ArrayList<EventInfo>();
                for (int j = 0; j < eventParamKeyList.size(); j++) {
                    EventInfo eventInfo = new EventInfo(eventParamKeyList.get(j), eventParamsMap.get(eventParamKeyList.get(j)));
                    eventInfoList.add(eventInfo);
                }
                eventParam = new EventParam(eventKeyList.get(0), ts, eventInfoList);
                data.add(eventParam);
            }
            params = new EventPubData(data);
            eventPayload = new EventBatchPayload(messageId, params);
            String p = JSON.toJSONString(eventPayload);
            client.publish(String.format("$sys/%s/%s/event/batch", productKey, deviceKey),
                    new MqttMessage(JSON.toJSONString(eventPayload).getBytes(StandardCharsets.UTF_8)));
        }
    }


    /**
     * 加密算法
     *
     * @param message
     * @param secret
     * @return
     */
    private static String sha256Hmac(String message, String secret) {
        String sign = "";
        try {
            Mac sha256Hmac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
            sha256Hmac.init(secretKey);
            byte[] bytes = sha256Hmac.doFinal(message.getBytes());
            sign = byteArrayToHexString(bytes);
        } catch (Exception e) {
            log.warn("!!! Error HmacSHA256 ===========" + e.getMessage());
            e.printStackTrace();
        }
        log.info("### Sign enciphered by HmacSha256:" + sign);
        return sign;
    }

    /**
     * 将加密后的字节数组转换成字符串
     *
     * @param b 字节数组
     * @return 字符串
     */
    private static String byteArrayToHexString(byte[] b) {
        StringBuilder hs = new StringBuilder();
        String temp;
        for (int n = 0; b != null && n < b.length; n++) {
            temp = Integer.toHexString(b[n] & 0XFF);
            if (temp.length() == 1) {
                hs.append('0');
            }
            hs.append(temp);
        }
        return hs.toString().toLowerCase();
    }

    /**
     * 关闭客户端
     */
    public void disconnectClient() {
        log.info("### Client disconnected");
        try {
            client.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }


    /**
     * 关闭客户端
     *
     * @throws MqttException
     */
    public void closeClient() {
        log.info("### Client closed");
        try {
            client.close();
        } catch (MqttException e) {
            e.printStackTrace();
        }

    }


    public void connectionLost(Throwable throwable) {
        log.warn("!!! Connection lost for: " + throwable);
        throwable.printStackTrace();
    }

    public void messageArrived(String topic, MqttMessage message) {
        log.info("### Receive message topic: " + topic);
        log.info("### Receive message payload: " + message.toString());

    }

    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        log.info("### Delivery complete: " + iMqttDeliveryToken.isComplete());
    }

    public void connectComplete(boolean b, String s) {
        log.info("### Mqtt client connected");
    }


}




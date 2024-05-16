package com.example.spring.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Service;

@Service
public class MQTTService {

    @Autowired
    private MessageChannel mqttInputChannel;

    @Autowired
    private MessageChannel mqttOutboundChannel;

    // Xuất bản (Publish) một thông điệp MQTT
    public void sendMessage(String topic, String message) {
        mqttOutboundChannel.send(MessageBuilder.withPayload(message)
                .setHeader(MqttHeaders.TOPIC, topic)
                .build());
    }

    // Đăng ký (Subscribe) vào một chủ đề MQTT
    public void subscribeToTopic(String topic) {
        // Bạn có thể sử dụng cấu trúc Message chứa thông tin đăng ký
        Message<String> message = MessageBuilder.withPayload("subscribe")
                .setHeader(MqttHeaders.TOPIC, topic)
                .build();
        mqttOutboundChannel.send(message);
    }

    // Xử lý dữ liệu khi nhận được message
    public void handleMessage(String topic, String message) {
        // Xử lý thông điệp ở đây
    }

}

package com.example.spring.config;

import com.example.spring.payload.dto.DhtDataDTO;
import com.example.spring.payload.dto.DoorDataDTO;
import com.example.spring.payload.dto.GasSensorDTO;
import com.example.spring.payload.dto.LightDTO;
import com.example.spring.entity.DHTSensorLog;
import com.example.spring.entity.DoorLog;
import com.example.spring.entity.GasSensorLog;
import com.example.spring.service.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.integration.support.MessagingExceptionWrapper;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.Date;
import java.util.List;
import java.util.Objects;


@Configuration
public class MQTTConfig {
    @Value("${mqtt.broker-url}")
    private String brokerUrl;

    @Value("${mqtt.username}")
    private String username;

    @Value("${mqtt.password}")
    private String password;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private WebSocketEventListener webSocketEventListener;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private DHT11SensorService dht11Service;


    @Autowired
    private GasSensorService gasSensorService;

    @Autowired
    private DoorService doorService;

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @Bean
    public MqttPahoClientFactory mqttClientFactory() {

        try {
            DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
            MqttConnectOptions options = new MqttConnectOptions();
            options.setServerURIs(new String[] {brokerUrl});
            options.setUserName(username);
            String pass = password;
            options.setPassword(pass.toCharArray());
            options.setCleanSession(true);

            factory.setConnectionOptions(options);
            System.out.println("Connected to HiveMQ successfully!");
            return factory;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create MQTT client", e);
        }
    }

    @Bean
    public MessageChannel mqttInputChannel()  {
        return new DirectChannel();
    }

    @Bean
    public MessageProducer inbound() {
        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter("serverIn", mqttClientFactory(), "#");
        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(2);
        adapter.setOutputChannel(mqttInputChannel());
        return adapter;
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannel")
    public MessageHandler handler() {
        return new MessageHandler() {
            @Override
            public void handleMessage(Message<?> message) throws MessagingExceptionWrapper {
                String topic = Objects.requireNonNull(message.getHeaders().get(MqttHeaders.RECEIVED_TOPIC)).toString();
                ModelMapper modelMapper = new ModelMapper();
                if(topic.equals("DHT11_data")) {
                    String payload = message.getPayload().toString();
                    //System.out.println(payload);
                    try {
                        DhtDataDTO dhtDataDTO = mapper.readValue(payload, DhtDataDTO.class);
                        dhtDataDTO.setTimestamp(new Date());
                        DHTSensorLog dhtSensorLog = modelMapper.map(dhtDataDTO, DHTSensorLog.class);
                        dhtSensorLog.setTimestamp(new Date());
                        dht11Service.save(dhtSensorLog);
                        String sensorTopic = "/topic/DHT11_data/" + dhtDataDTO.getHome_id() + "/" + dhtDataDTO.getDhtid();
                        messagingTemplate.convertAndSend(sensorTopic, dhtDataDTO);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }

                }
                if(topic.equals("light_data")) {
                    String payload = message.getPayload().toString();
                    try {
                        LightDTO lightDTO = mapper.readValue(payload, LightDTO.class);
                        lightDTO.setTimestamp(new Date());

                        String sensorTopic = "/topic/light_data/" + lightDTO.getHome_id() + "/" + lightDTO.getLight_id() ;
                        messagingTemplate.convertAndSend(sensorTopic, lightDTO);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                   // System.out.println("led_state:123123132"+message.getPayload());

                }
                if(topic.equals("gas_data")) {
                    String payload = message.getPayload().toString();
                    try {
                        GasSensorDTO gasSensorDTO = mapper.readValue(payload, GasSensorDTO.class);
                        gasSensorDTO.setTimestamp(new Date());
                        gasSensorDTO.setTimestamp(new Date());
                        GasSensorLog gasSensorLog = modelMapper.map(gasSensorDTO,GasSensorLog.class);
                        gasSensorService.save(gasSensorLog);
                        if (gasSensorLog.getGasStatus().equals("1")) {
                            List<String> listEmail = userService.getEmailByHomeId(gasSensorDTO.getHome_id());
                            for (String e : listEmail) {
                                if (e != null) {
                                    // Gửi email bất đồng bộ
                                    new Thread(() -> emailService.sendFireAlertEmail(e)).start();
                                }
                            }
                        }

                        String sensorTopic = "/topic/gas_data/" + gasSensorDTO.getHome_id() + "/" + gasSensorDTO.getGas_sensor_id();
                        messagingTemplate.convertAndSend(sensorTopic, gasSensorDTO);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                }
                if(topic.equals("door_data")){
                    String payload = message.getPayload().toString();
                    try {
                        DoorDataDTO doorDataDTO = mapper.readValue(payload, DoorDataDTO.class);
                        doorDataDTO.setTimestamp(new Date());
                        DoorLog doorLog = modelMapper.map(doorDataDTO, DoorLog.class);
                        doorService.save(doorLog);
                        System.out.println(payload);
                        System.out.println(doorDataDTO.getHome_id());

                        String sensorTopic = "/topic/door_data/" + doorDataDTO.getHome_id() + "/" + doorDataDTO.getDoor_id();
                        messagingTemplate.convertAndSend(sensorTopic, doorDataDTO);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                }

            }
        };
    }
    @Bean
    public MessageChannel mqttOutboundChannel() {
        return new DirectChannel();
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttOutboundChannel")
    public MessageHandler mqttOutbound() {
        MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler("serverOut", mqttClientFactory());
        messageHandler.setAsync(true);
        messageHandler.setDefaultTopic("#");
        messageHandler.setDefaultRetained(false);
        return messageHandler;
    }

}

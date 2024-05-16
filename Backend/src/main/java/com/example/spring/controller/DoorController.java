package com.example.spring.controller;

import com.example.spring.payload.dto.DoorDataDTO;
import com.example.spring.entity.DoorLog;
import com.example.spring.service.DoorService;
import com.example.spring.service.MQTTService;
import com.example.spring.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@CrossOrigin
public class DoorController {
    @Autowired
    private DoorService doorService;

    @Autowired
    private MQTTService mqttService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private UserService userService;

    @PostMapping("/changeDoorStatus")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> changeDoorStatus(@RequestBody DoorDataDTO doorDataDTO){
        if(doorDataDTO==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("");
        }
        doorDataDTO.setTimestamp(new Date());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();

        System.out.println("Người dùng " + userName + " gửi yêu cầu đến URI /changeDoorStatus" );

//        String sensorTopic = "/topic/door_data/" + doorDataDTO.getHome_id();
//        messagingTemplate.convertAndSend(sensorTopic, doorDataDTO);
        String sensorTopic = "/topic/door_data/" + doorDataDTO.getHome_id() + "/" + doorDataDTO.getDoor_id();
        messagingTemplate.convertAndSend(sensorTopic, doorDataDTO);

        String topic =userService.getHomeIdByUserName(userName)+ "/door_state/" + doorDataDTO.getDoor_id() ;
        System.out.println(topic);
        mqttService.sendMessage(topic, doorDataDTO.getStatus());

        ModelMapper modelMapper = new ModelMapper();
        doorDataDTO.setCard_id(userService.getIdCardByUserName(userName));
        doorService.save(modelMapper.map(doorDataDTO, DoorLog.class));

        return ResponseEntity.status(HttpStatus.OK).body("open/close door successfully.");

    }
}

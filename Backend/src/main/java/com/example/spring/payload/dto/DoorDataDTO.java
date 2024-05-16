package com.example.spring.payload.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DoorDataDTO {

    private String home_id;
    private String door_id;
    private String card_id;
    private Date timestamp;
    private String status;
}

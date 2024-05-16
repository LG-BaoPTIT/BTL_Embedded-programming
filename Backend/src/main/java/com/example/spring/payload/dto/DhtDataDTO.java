package com.example.spring.payload.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DhtDataDTO {

    private String home_id;
    private Date timestamp;
    private String dhtid;
    private double humidity;
    private double temperature;
}

package com.example.spring.payload.response;

import lombok.Data;

import java.util.Date;

@Data
public class LogDhtDTO {
    private String dhtId;
    private String description;
    private Date timestamp;
    private double humidity;
    private double temperature;
}

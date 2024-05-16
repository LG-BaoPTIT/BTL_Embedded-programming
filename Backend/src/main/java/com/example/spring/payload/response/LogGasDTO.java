package com.example.spring.payload.response;

import lombok.Data;

import java.util.Date;
@Data
public class LogGasDTO {
    private String gas_sensor_id;
    private Date timestamp;
    private long value;
    private String gasStatus;//0/1 => (khong phat hien khi gas/co phat hien khi gas)
}

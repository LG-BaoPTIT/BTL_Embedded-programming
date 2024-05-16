package com.example.spring.payload.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GasSensorDTO {
    private String home_id;
    private String gas_sensor_id;
    private Date timestamp;
    private long value;
    private String gasStatus;//0/1 => (khong phat hien khi gas/co phat hien khi gas)
}

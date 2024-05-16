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
public class LightDTO {

    private String home_id;
    private String light_id;
    private Date timestamp;
    private String status;
}

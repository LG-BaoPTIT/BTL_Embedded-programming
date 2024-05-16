package com.example.spring.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LogDoorDTO {
    private String doorId;
    private Date timestamp;
    private String status;
    private String name;
    private String description;
}

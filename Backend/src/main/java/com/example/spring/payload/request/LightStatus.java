package com.example.spring.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LightStatus {

    private String lightId;

    private String deviceId;

    private String status; // Trạng thái (Bật/Tắt) -> (1/0)
}

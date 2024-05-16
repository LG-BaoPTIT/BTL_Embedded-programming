package com.example.spring.service;

import com.example.spring.entity.DHTSensorLog;

public interface DHT11SensorService {
    DHTSensorLog save(DHTSensorLog dhtSensorLog);
}

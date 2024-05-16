package com.example.spring.serviceImp;

import com.example.spring.entity.DHT11Sensor;
import com.example.spring.entity.DHTSensorLog;
import com.example.spring.repository.DHT11SensorRepository;
import com.example.spring.service.DHT11SensorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DHT11SensorServiceImp implements DHT11SensorService {
    @Autowired
    DHT11SensorRepository dht11Repository;


    @Override
    public DHTSensorLog save(DHTSensorLog dhtSensorLog) {
        return dht11Repository.save(dhtSensorLog);
    }
}

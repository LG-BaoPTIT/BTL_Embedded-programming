package com.example.spring.service;

import com.example.spring.entity.GasSensorLog;
import org.springframework.stereotype.Service;


public interface GasSensorService {
    GasSensorLog save(GasSensorLog gasSensorLog);
}

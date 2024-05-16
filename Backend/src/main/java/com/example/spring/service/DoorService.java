package com.example.spring.service;

import com.example.spring.entity.DoorLog;
import org.springframework.stereotype.Service;


public interface DoorService {
    DoorLog save (DoorLog doorLog);
}

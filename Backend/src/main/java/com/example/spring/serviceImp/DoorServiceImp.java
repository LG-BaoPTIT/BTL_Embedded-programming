package com.example.spring.serviceImp;

import com.example.spring.entity.DoorLog;
import com.example.spring.repository.DoorRepository;
import com.example.spring.service.DoorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DoorServiceImp implements DoorService {
    @Autowired
    private DoorRepository doorRepository;
    @Override
    public DoorLog save(DoorLog doorLog) {
        return doorRepository.save(doorLog);
    }
}

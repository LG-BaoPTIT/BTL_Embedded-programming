package com.example.spring.serviceImp;

import com.example.spring.entity.GasSensorLog;
import com.example.spring.repository.GasSensorRepository;
import com.example.spring.service.GasSensorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GasSensorServiceImp implements GasSensorService {
    @Autowired
    private GasSensorRepository gasSensorRepository;

    @Override
    public GasSensorLog save(GasSensorLog gasSensorLog) {
        return gasSensorRepository.save(gasSensorLog);
    }
}

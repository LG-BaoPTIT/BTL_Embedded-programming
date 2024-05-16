package com.example.spring.repository;

import com.example.spring.payload.request.DataSearch;
import com.example.spring.payload.response.LogDhtDTO;
import com.example.spring.payload.response.LogDoorDTO;
import com.example.spring.payload.response.LogGasDTO;

import java.util.List;

public interface LogRepositoryCustom {
    List<LogDoorDTO> getLogDoor(DataSearch dataSearch);
    List<LogDhtDTO> getLogDht(DataSearch dataSearch);
    List<LogGasDTO> getLogGas(DataSearch dataSearch);
}

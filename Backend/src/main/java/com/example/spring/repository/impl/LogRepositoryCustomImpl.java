package com.example.spring.repository.impl;

import com.example.spring.payload.request.DataSearch;
import com.example.spring.payload.response.LogDhtDTO;
import com.example.spring.payload.response.LogDoorDTO;
import com.example.spring.payload.response.LogGasDTO;
import com.example.spring.repository.LogRepositoryCustom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Repository
public class LogRepositoryCustomImpl implements LogRepositoryCustom {
    @Autowired
    EntityManager entityManager;

    @Override
    public List<LogDoorDTO> getLogDoor(DataSearch dataSearch) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT l.door_id as doorId, l.timestamp, l.status, u.name, d.description ");
        sql.append("FROM door_log l ");
        sql.append("LEFT JOIN users u ON l.id_card = u.id_card ");
        sql.append("LEFT JOIN door d ON l.door_id = d.door_id ");
        sql.append("WHERE 1 = 1 ");

        if (dataSearch.getHomeId() != null && !dataSearch.getHomeId().isEmpty()) {
            sql.append("AND d.home_id = :homeId ");
        }

        if (dataSearch.getStart() != null) {
            sql.append("AND l.timestamp >= :start ");
        }
        if (dataSearch.getEnd() != null) {
            sql.append("AND l.timestamp <= :end ");
        }

        if (dataSearch.getKeyWord() != null && !dataSearch.getKeyWord().isEmpty()) {
            sql.append("AND (LOWER(d.door_id) LIKE CONCAT('%', LOWER(:keyWord), '%') ");
            sql.append("OR LOWER(d.description) LIKE CONCAT('%', LOWER(:keyWord), '%')) ");
        }

        Query query = entityManager.createNativeQuery(sql.toString());

        if (dataSearch.getHomeId() != null && !dataSearch.getHomeId().isEmpty()) {
            query.setParameter("homeId", dataSearch.getHomeId());
        }
        if (dataSearch.getStart() != null) {
            query.setParameter("start", dataSearch.getStart());
        }
        if (dataSearch.getEnd() != null) {
            query.setParameter("end", dataSearch.getEnd());
        }
        if (dataSearch.getKeyWord() != null && !dataSearch.getKeyWord().isEmpty()) {
            query.setParameter("keyWord", dataSearch.getKeyWord());
        }

        List<Object[]> rows = query.getResultList();
        List<LogDoorDTO> result = new ArrayList<>();

        for (Object[] row : rows) {
            LogDoorDTO logHistoryDoorDTO = new LogDoorDTO();
            logHistoryDoorDTO.setDoorId((String) row[0]);
            logHistoryDoorDTO.setTimestamp((Date) row[1]);
            logHistoryDoorDTO.setStatus((String) row[2]);
            logHistoryDoorDTO.setName((String) row[3]);
            logHistoryDoorDTO.setDescription((String) row[4]);
            result.add(logHistoryDoorDTO);
        }


        return result;

    }

    @Override
    public List<LogDhtDTO> getLogDht(DataSearch dataSearch) {
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT l.dht_id, l.humidity, l.temperature, l.timestamp, s.description ");
        sql.append("FROM dhtlog l ");
        sql.append("LEFT JOIN sensordht11 s ON l.dht_id = s.dht_id ");
        sql.append("WHERE 1 = 1 ");

        if (dataSearch.getHomeId() != null && !dataSearch.getHomeId().isEmpty()) {
            sql.append("AND s.home_id = :homeId ");
        }
        if (dataSearch.getStart() != null) {
            sql.append("AND l.timestamp >= :start ");
        }
        if (dataSearch.getEnd() != null) {
            sql.append("AND l.timestamp <= :end ");
        }

        if (dataSearch.getKeyWord() != null && !dataSearch.getKeyWord().isEmpty()) {
            sql.append("AND (LOWER(s.dht_id) LIKE CONCAT('%', LOWER(:keyWord), '%') ");
            sql.append("OR LOWER(s.description) LIKE CONCAT('%', LOWER(:keyWord), '%')) ");
        }

        Query query = entityManager.createNativeQuery(sql.toString());

        if (dataSearch.getHomeId() != null && !dataSearch.getHomeId().isEmpty()) {
            query.setParameter("homeId", dataSearch.getHomeId());
        }
        if (dataSearch.getStart() != null) {
            query.setParameter("start", dataSearch.getStart());
        }
        if (dataSearch.getEnd() != null) {
            query.setParameter("end", dataSearch.getEnd());
        }
        if (dataSearch.getKeyWord() != null && !dataSearch.getKeyWord().isEmpty()) {
            query.setParameter("keyWord", dataSearch.getKeyWord());
        }
        List<Object[]> rows = query.getResultList();
        List<LogDhtDTO> result = new ArrayList<>();

        for (Object[] row :rows){
            LogDhtDTO logDhtDTO = new LogDhtDTO();

            logDhtDTO.setDhtId((String) row[0]);
            logDhtDTO.setHumidity((double) row[1]);
            logDhtDTO.setTemperature((double) row[2]);
            logDhtDTO.setTimestamp((Date) row[3]);
            logDhtDTO.setDescription((String) row[4]);

            result.add(logDhtDTO);
        }

        return result;
    }

    @Override
    public List<LogGasDTO> getLogGas(DataSearch dataSearch) {
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT l.gas_sensor_id, l.timestamp, l.value, l.gas_status ");
        sql.append("FROM gas_sensor_log l ");
        sql.append("LEFT JOIN gas_sensor s ON l.gas_sensor_id = s.gas_sensor_id ");
        sql.append("WHERE 1 = 1 ");

        if (dataSearch.getHomeId() != null && !dataSearch.getHomeId().isEmpty()) {
            sql.append("AND s.home_id = :homeId ");
        }
        if (dataSearch.getStart() != null) {
            sql.append("AND l.timestamp >= :start ");
        }
        if (dataSearch.getEnd() != null) {
            sql.append("AND l.timestamp <= :end ");
        }
        if (dataSearch.getKeyWord() != null && !dataSearch.getKeyWord().isEmpty()) {
            sql.append("AND (LOWER(s.gas_sensor_id) LIKE CONCAT('%', LOWER(:keyWord), '%') ");
            sql.append("OR LOWER(s.description) LIKE CONCAT('%', LOWER(:keyWord), '%')) ");
        }
        Query query = entityManager.createNativeQuery(sql.toString());

        if (dataSearch.getHomeId() != null && !dataSearch.getHomeId().isEmpty()) {
            query.setParameter("homeId", dataSearch.getHomeId());
        }
        if (dataSearch.getStart() != null) {
            query.setParameter("start", dataSearch.getStart());
        }
        if (dataSearch.getEnd() != null) {
            query.setParameter("end", dataSearch.getEnd());
        }
        if (dataSearch.getKeyWord() != null && !dataSearch.getKeyWord().isEmpty()) {
            query.setParameter("keyWord", dataSearch.getKeyWord());
        }

        List<Object[]> rows = query.getResultList();
        List<LogGasDTO> result = new ArrayList<>();

        for (Object[] row : rows) {
            LogGasDTO logGasDTO = new LogGasDTO();

            logGasDTO.setGas_sensor_id((String) row[0]);
            logGasDTO.setTimestamp((Date) row[1]);
            logGasDTO.setValue((long) row[2]);
            logGasDTO.setGasStatus((String) row[3]);

            result.add(logGasDTO);
        }

        return result;
    }



}

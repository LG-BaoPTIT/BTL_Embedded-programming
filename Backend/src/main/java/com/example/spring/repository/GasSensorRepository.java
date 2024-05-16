package com.example.spring.repository;

import com.example.spring.entity.GasSensorLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GasSensorRepository extends JpaRepository<GasSensorLog,Long> {
}

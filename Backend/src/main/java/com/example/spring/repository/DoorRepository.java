package com.example.spring.repository;

import com.example.spring.entity.DoorLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DoorRepository extends JpaRepository<DoorLog,Long> {
}

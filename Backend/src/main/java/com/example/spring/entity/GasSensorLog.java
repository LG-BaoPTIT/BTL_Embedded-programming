package com.example.spring.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "GasSensorLog")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GasSensorLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LogId")
    private Long logId;

    @ManyToOne
    @JoinColumn(name = "GasSensorId", referencedColumnName = "GasSensorId", insertable = false, updatable = false)
    private GasSensor gasSensor;

    @Column(name = "GasSensorId")
    private String gas_sensor_id;

    @Column(name = "timestamp", nullable = false)
    private Date timestamp;

    @Column(name = "value")
    private long value;

    @Column(name = "gasStatus")
    private String gasStatus;//0/1 => (khong phat hien khi gas/co phat hien khi gas)
}

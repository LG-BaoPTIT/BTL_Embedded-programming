package com.example.spring.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.example.spring.entity.DHT11Sensor;
import java.util.Date;

@Entity
@Table(name = "DHTLog")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DHTSensorLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LogId")
    private Long logId;

    @ManyToOne
    @JoinColumn(name = "DhtId", referencedColumnName = "DhtId", insertable = false,updatable = false)
    private DHT11Sensor dht11Sensor;

    @Column(name = "DhtId")
    private String dhtid;

    @Column(name = "timestamp", nullable = false)
    private Date timestamp;

    @Column(name = "humidity")
    private double humidity;

    @Column(name = "temperature")
    private  double temperature;


}

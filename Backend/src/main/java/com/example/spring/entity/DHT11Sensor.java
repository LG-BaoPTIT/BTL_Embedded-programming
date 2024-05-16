package com.example.spring.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "SensorDHT11")
public class DHT11Sensor {

    @Id
    @Column(name = "DhtId")
    private String DHTid;

    @ManyToOne
    @JoinColumn(name = "HomeId", referencedColumnName = "HomeId", insertable = false, updatable = false)
    private Homes homes;

    @Column(name = "HomeId")
    private String HomeId; // Trường này ánh xạ đến deviceId

    @Column(name = "description")
    private String description;

}

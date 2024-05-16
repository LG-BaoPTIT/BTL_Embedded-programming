package com.example.spring.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "DoorLog")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DoorLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LogId")
    private Long logId;

    @ManyToOne
    @JoinColumn(name = "doorId", referencedColumnName = "doorId", insertable = false, updatable = false)
    private Door door;

    @Column(name = "doorId")
    private String door_id;

    @Column(name = "timestamp")
    private Date timestamp;

    @Column(name = "status")
    private String status;

    @Column(name = "idCard")
    private String card_id;


}

package com.example.spring.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "GasSensor")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GasSensor {
    @Id
    @Column(name = "GasSensorId")
    private String GasSensorId;

    @ManyToOne
    @JoinColumn(name = "HomeId", referencedColumnName = "HomeId", insertable = false, updatable = false)
    private Homes homes;

    @Column(name = "HomeId")
    private String HomeId;

    @Column(name = "description")
    private String description;

}

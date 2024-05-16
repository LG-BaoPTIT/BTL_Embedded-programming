package com.example.spring.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Light")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Light {
    @Id
    @Column(name = "lightId")
    private String lightId;

    @ManyToOne
    @JoinColumn(name = "HomeId", referencedColumnName = "HomeId", insertable = false, updatable = false)
    private Homes homes;

    @Column(name = "HomeId")
    private String HomeId;

    @Column(name = "description")
    private String description;
}

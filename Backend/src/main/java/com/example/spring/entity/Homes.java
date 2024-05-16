package com.example.spring.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Homes")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Homes {

    @Id
    @Column(name = "HomeId")
    private String HomeId;

    @Column(name = "moduleType", nullable = false)
    private String moduleType;

    @Column(name = "location")
    private String location;

    @Column(name = "description")
    private String description;

}

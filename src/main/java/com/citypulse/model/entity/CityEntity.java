package com.citypulse.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@Table(name = "city")
public class CityEntity {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "city_name")
    private String cityName;

    @Column(name = "country")
    private String country;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}

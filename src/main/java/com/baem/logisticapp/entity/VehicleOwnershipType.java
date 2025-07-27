package com.baem.logisticapp.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "vehicle_ownership_type")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleOwnershipType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ownership_type_id")
    private Long id;

    @Column(name = "ownership_name", unique = true, nullable = false)
    private String ownershipName; // Özmal / Kiralık / Spedisyon
}
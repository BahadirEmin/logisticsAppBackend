package com.baem.logisticapp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "vehicle")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vehicle_id")
    private Long id;

    @Column(unique = true, nullable = false)
    private String plateNo;

    private String make;
    private String model;
    private Short modelYear;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ownership_type_id")
    private VehicleOwnershipType ownershipType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    private LocalDate purchaseDate;
    private String vin;

    @Builder.Default
    private Boolean isActive = true;
}

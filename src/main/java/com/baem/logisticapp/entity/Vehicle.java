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

    private LocalDate purchaseDate;
    private String vin;

    @Builder.Default
    private Boolean isActive = true;

    // Alert sistemi için gerekli field'lar
    private LocalDate inspectionExpiryDate; // Muayene bitiş tarihi
    private LocalDate insuranceExpiryDate;  // Sigorta bitiş tarihi

    // Alert sistemi için convenience methods
    public String getLicensePlate() {
        return plateNo;
    }

    public String getBrand() {
        return make;
    }

    public LocalDate getInspectionExpiryDate() {
        return inspectionExpiryDate;
    }
}

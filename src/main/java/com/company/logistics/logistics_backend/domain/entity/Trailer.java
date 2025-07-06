package com.company.logistics.logistics_backend.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "trailer")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Trailer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trailer_id")
    private Long id;

    @Column(unique = true, nullable = false)
    private String trailerNo;

    private String make;
    private String model;
    private Short modelYear;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ownership_type_id")
    private VehicleOwnershipType ownershipType;

    private LocalDate purchaseDate;
    private String vin;

    private String trailerType; // Flatbed, Container, Refrigerated, etc.
    private Double capacity; // Weight capacity in tons
    private Double length; // Length in meters
    private Double width; // Width in meters
    private Double height; // Height in meters

    @Builder.Default
    private Boolean isActive = true;
}

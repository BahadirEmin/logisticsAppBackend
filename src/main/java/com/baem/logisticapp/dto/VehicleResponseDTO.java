package com.baem.logisticapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehicleResponseDTO {
    private Long id;
    private String plateNo;
    private String make;
    private String model;
    private Short modelYear;
    private Long ownershipTypeId;
    private String ownershipTypeName;
    private LocalDate purchaseDate;
    private String vin;
    private Boolean isActive;
}
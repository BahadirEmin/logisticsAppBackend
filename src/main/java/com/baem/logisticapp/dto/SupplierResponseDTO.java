package com.baem.logisticapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupplierResponseDTO {
    
    private Long id;
    private String companyName;
    private String taxNumber;
    private String taxOffice;
    private String tradeRegistryNumber;
    private String phoneNumber;
    private String email;
    private String address;
    private String city;
    private String country;
    private Boolean isActive;
    private LocalDate contractStartDate;
    private LocalDate contractEndDate;
    private String notes;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    
    // Vehicle and trailer counts
    private Integer activeVehicleCount;
    private Integer activeTrailerCount;
}

package com.baem.logisticapp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupplierUpdateDTO {
    
    @Size(max = 255, message = "Company name cannot exceed 255 characters")
    private String companyName;

    @Size(max = 255, message = "Tax office cannot exceed 255 characters")
    private String taxOffice;

    @Size(max = 50, message = "Trade registry number cannot exceed 50 characters")
    private String tradeRegistryNumber;

    @Size(max = 20, message = "Phone number cannot exceed 20 characters")
    private String phoneNumber;

    @Email(message = "Email should be valid")
    @Size(max = 255, message = "Email cannot exceed 255 characters")
    private String email;

    @Size(max = 500, message = "Address cannot exceed 500 characters")
    private String address;

    @Size(max = 100, message = "City cannot exceed 100 characters")
    private String city;

    @Size(max = 100, message = "Country cannot exceed 100 characters")
    private String country;

    private Boolean isActive;

    private LocalDate contractStartDate;

    private LocalDate contractEndDate;

    @Size(max = 1000, message = "Notes cannot exceed 1000 characters")
    private String notes;
}

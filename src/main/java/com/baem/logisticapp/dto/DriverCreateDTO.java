package com.baem.logisticapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DriverCreateDTO {
    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "License number is required")
    private String licenseNo;

    @NotBlank(message = "License class is required")
    private String licenseClass;

    @NotNull(message = "Passport expiry date is required")
    private LocalDate passportExpiry;

    @NotNull(message = "Visa expiry date is required")
    private LocalDate visaExpiry;

    @NotNull(message = "Residence permit expiry date is required")
    private LocalDate residencePermitExpiry;

    @NotBlank(message = "Phone number is required")
    private String phoneNumber;

    @NotBlank(message = "Email is required")
    private String email;
}
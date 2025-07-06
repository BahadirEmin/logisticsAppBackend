package com.company.logistics.logistics_backend.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DriverDTO {
    private Long id;
    private Long personelId;
    private String licenseNo;
    private String licenseClass;
    private LocalDate passportExpiry;
    private LocalDate visaExpiry;
    private LocalDate residencePermitExpiry;
}
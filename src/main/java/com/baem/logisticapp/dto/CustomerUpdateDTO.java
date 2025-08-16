package com.baem.logisticapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerUpdateDTO {
    @NotBlank(message = "Customer name is required")
    private String name;

    private String taxNo; // Nullable

    // Contact Information
    private String contactName; // Ulaşılacak kişi adı
    private String phoneNumber; // Telefon numarası
    private String address; // Adres

    @NotNull(message = "Risk status is required")
    private Long riskStatusId;

    private Boolean isBlacklisted;

    private Boolean isInLawsuit;

    private BigDecimal creditLimit; // Nullable
}
package com.baem.logisticapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerResponseDTO {
    private Long id;
    private String name;
    private String taxNo;
    private Long riskStatusId;
    private String riskStatusName;
    private Boolean isBlacklisted;
    private Boolean isInLawsuit;
    private BigDecimal creditLimit;
    private OffsetDateTime createdAt;
}
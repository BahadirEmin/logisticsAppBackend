package com.baem.logisticapp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "customer")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Long id;

    private String name; // Ünvan
    private String taxNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "risk_status_id")
    private CustomerRiskStatus riskStatus;

    @Builder.Default
    private Boolean isBlacklisted = false;

    @Builder.Default
    private Boolean isInLawsuit = false;

    private BigDecimal creditLimit;

    @Builder.Default
    private OffsetDateTime createdAt = OffsetDateTime.now();
}
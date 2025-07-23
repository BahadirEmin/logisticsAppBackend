package com.baem.logisticapp.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "customer_risk_status")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class CustomerRiskStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "risk_status_id")
    private Long id;

    @Column(name = "status_name", unique = true, nullable = false)
    private String statusName; // Örn. “Düşük Risk”, “Kara Liste”
}
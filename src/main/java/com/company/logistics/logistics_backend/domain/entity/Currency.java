package com.company.logistics.logistics_backend.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "currency")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Currency {

    @Id
    @Column(name = "currency_code", length = 3)
    private String currencyCode; // ISO 4217 (TRY, USD…)ß

    private String name; // Tam adı
    private String symbol; // ₺, $, €, £ …
}
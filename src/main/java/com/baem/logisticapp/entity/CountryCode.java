package com.baem.logisticapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "country_codes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CountryCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "country_name", nullable = false, length = 100)
    private String countryName;

    @Column(name = "country_name_tr", length = 100)
    private String countryNameTr;

    @Column(name = "country_code_iso", length = 3)
    private String countryCodeIso; // TR, DE, FR, etc.

    @Column(name = "country_code_numeric", nullable = false, length = 2)
    private String countryCodeNumeric; // 90, 49, 33, etc.

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;
}

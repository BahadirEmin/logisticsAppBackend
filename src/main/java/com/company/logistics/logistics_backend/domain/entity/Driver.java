package com.company.logistics.logistics_backend.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "driver")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "driver_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "personnel_id", unique = true)
    private Personnel personnel;

    private String licenseNo;
    private String licenseClass;

    private LocalDate passportExpiry;
    private LocalDate visaExpiry;
    private LocalDate residencePermitExpiry;
}

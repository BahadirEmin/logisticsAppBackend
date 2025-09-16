package com.baem.logisticapp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

@Entity
@Table(name = "supplier")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "supplier_id")
    private Long id;

    // Temel bilgiler
    @Column(nullable = false)
    private String companyName;

    @Column(unique = true, nullable = false)
    private String taxNumber;

    private String taxOffice;
    private String tradeRegistryNumber;

    // İletişim bilgileri
    private String phoneNumber;
    private String email;
    private String address;
    private String city;
    private String country;

    // Sözleşme/iş bilgileri
    @Builder.Default
    private Boolean isActive = true;

    private LocalDate contractStartDate;
    private LocalDate contractEndDate;

    @Column(columnDefinition = "TEXT")
    private String notes;

    // İlişkiler
    @OneToMany(mappedBy = "supplier", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Vehicle> vehicles;

    @OneToMany(mappedBy = "supplier", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Trailer> trailers;

    @Builder.Default
    private OffsetDateTime createdAt = OffsetDateTime.now();

    @Builder.Default
    private OffsetDateTime updatedAt = OffsetDateTime.now();

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = OffsetDateTime.now();
    }
}

package com.baem.logisticapp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    // Mantıklı sipariş numarası (16 haneli: YYMMDDCCSSSSSSSS)
    @Column(name = "order_number", unique = true, length = 16)
    private String orderNumber;

    // Müşteri ilişkisi
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    // Yola çıkış bilgileri
    @Column(name = "departure_country")
    private String departureCountry;

    @Column(name = "departure_city")
    private String departureCity;

    @Column(name = "departure_district")
    private String departureDistrict;

    @Column(name = "departure_postal_code")
    private String departurePostalCode;

    @Column(name = "departure_address", columnDefinition = "TEXT")
    private String departureAddress;

    @Column(name = "departure_contact_name")
    private String departureContactName;

    @Column(name = "departure_contact_phone")
    private String departureContactPhone;

    @Column(name = "departure_contact_email")
    private String departureContactEmail;

    // Varış bilgileri
    @Column(name = "arrival_country")
    private String arrivalCountry;

    @Column(name = "arrival_city")
    private String arrivalCity;

    @Column(name = "arrival_district")
    private String arrivalDistrict;

    @Column(name = "arrival_postal_code")
    private String arrivalPostalCode;

    @Column(name = "arrival_address", columnDefinition = "TEXT")
    private String arrivalAddress;

    @Column(name = "arrival_contact_name")
    private String arrivalContactName;

    @Column(name = "arrival_contact_phone")
    private String arrivalContactPhone;

    @Column(name = "arrival_contact_email")
    private String arrivalContactEmail;

    // Yük bilgileri
    @Column(name = "cargo_width")
    private BigDecimal cargoWidth;

    @Column(name = "cargo_length")
    private BigDecimal cargoLength;

    @Column(name = "cargo_height")
    private BigDecimal cargoHeight;

    @Column(name = "cargo_weight_kg")
    private BigDecimal cargoWeightKg;

    @Column(name = "cargo_type")
    private String cargoType;

    @Column(name = "can_transfer")
    private Boolean canTransfer;

    // Personel atamaları
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sales_person_id")
    private User salesPerson; // Teklifi oluşturan satış personeli

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "operation_person_id")
    private User operationPerson; // Operasyoncu

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fleet_person_id")
    private User fleetPerson; // Filocu

    // Araç atamaları
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_truck_id")
    private Vehicle assignedTruck; // Atanacak kamyon

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_trailer_id")
    private Trailer assignedTrailer; // Atanacak trailer

    // Şoför ataması (opsiyonel)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_driver_id")
    private Driver assignedDriver; // Atanacak şoför

    // Fiyat bilgileri
    @Column(name = "quote_price", precision = 10, scale = 2)
    private BigDecimal quotePrice; // Teklif edilen fiyat

    @Column(name = "actual_price", precision = 10, scale = 2)
    private BigDecimal actualPrice; // Gerçekleşen fiyat

    // Tedarik türü (Kiralık, Özmal)
    @Column(name = "supply_type")
    private String supplyType; // "KIRALIK", "OZMAL"

    // Sefer numarası
    @Column(name = "trip_number", unique = true)
    private String tripNumber; // Otomatik atanan sefer numarası

    // Gümrük bilgileri
    @Column(name = "customs_address", columnDefinition = "TEXT")
    private String customsAddress;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customs_person_id")
    private User customsPerson; // Gümrükçü personel

    // Tarih bilgileri
    @Column(name = "loading_date")
    private LocalDate loadingDate;

    @Column(name = "deadline_date")
    private LocalDate deadlineDate;

    @Column(name = "estimated_arrival_date")
    private LocalDate estimatedArrivalDate;

    // Sefer durumu
    @Enumerated(EnumType.STRING)
    @Column(name = "trip_status")
    private TripStatus tripStatus;

    // Sistem bilgileri
    @Builder.Default
    @Column(name = "created_at")
    private OffsetDateTime createdAt = OffsetDateTime.now();

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;
}
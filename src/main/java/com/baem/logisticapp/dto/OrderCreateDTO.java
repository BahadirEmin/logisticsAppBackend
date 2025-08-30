package com.baem.logisticapp.dto;

import com.baem.logisticapp.entity.TripStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreateDTO {

    @NotNull(message = "Customer ID is required")
    private Long customerId;

    // Yola çıkış bilgileri
    private String departureCountry;
    private String departureCity;
    private String departureDistrict;
    private String departurePostalCode;
    private String departureAddress;
    private String departureContactName;
    private String departureContactPhone;
    private String departureContactEmail;

    // Varış bilgileri
    private String arrivalCountry;
    private String arrivalCity;
    private String arrivalDistrict;
    private String arrivalPostalCode;
    private String arrivalAddress;
    private String arrivalContactName;
    private String arrivalContactPhone;
    private String arrivalContactEmail;

    // Yük bilgileri
    private BigDecimal cargoWidth;
    private BigDecimal cargoLength;
    private BigDecimal cargoHeight;
    private BigDecimal cargoWeightKg;
    private String cargoType;
    private Boolean canTransfer;

    // Personel atamaları
    private Long salesPersonId; // Teklifi oluşturan satış personeli

    // Araç ve şoför atamaları
    private Long assignedTruckId;
    private Long assignedTrailerId;
    private Long assignedDriverId; // Opsiyonel

    // Fiyat bilgileri
    private BigDecimal quotePrice; // Teklif edilen fiyat
    private BigDecimal actualPrice; // Gerçekleşen fiyat

    // Tedarik türü
    private String supplyType; // "KIRALIK", "OZMAL"

    // Gümrük bilgileri
    private String customsAddress;

    // Tarih bilgileri
    private LocalDate loadingDate;
    private LocalDate deadlineDate;
    private LocalDate estimatedArrivalDate;

    // Sefer durumu
    @Builder.Default
    private TripStatus tripStatus = TripStatus.TEKLIF_ASAMASI;
}
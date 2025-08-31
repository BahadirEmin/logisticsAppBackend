package com.baem.logisticapp.dto;

import com.baem.logisticapp.entity.TripStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDTO {

    private Long id;

    // Mantıklı sipariş numarası (16 haneli: YYMMDDCCSSSSSSSS)
    private String orderNumber;

    // Müşteri bilgileri
    private Long customerId;
    private String customerName;

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
    private Long salesPersonId;
    private String salesPersonName;
    private Long operationPersonId;
    private String operationPersonName;
    private Long fleetPersonId;
    private String fleetPersonName;

    // Araç atamaları
    private Long assignedTruckId;
    private String assignedTruckPlateNo;
    private Long assignedTrailerId;
    private String assignedTrailerNo;
    
    // Şoför ataması
    private Long assignedDriverId;
    private String assignedDriverName;
    
    // Fiyat bilgileri
    private BigDecimal quotePrice;
    private BigDecimal actualPrice;
    
    // Tedarik türü
    private String supplyType;
    
    // Sefer numarası
    private String tripNumber;

    // Gümrük bilgileri
    private String customsAddress;
    private Long customsPersonId;
    private String customsPersonName;

    // Tarih bilgileri
    private LocalDate loadingDate;
    private LocalDate deadlineDate;
    private LocalDate estimatedArrivalDate;

    // Sefer durumu
    private TripStatus tripStatus;
    private String tripStatusDisplayName;

    // Sistem bilgileri
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
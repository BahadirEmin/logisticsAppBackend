package com.baem.logisticapp.dto;

import com.baem.logisticapp.entity.TripStatus;
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
public class OrderUpdateDTO {

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
    private Long salesPersonId;
    private Long operationPersonId;
    private Long fleetPersonId;

    // Araç atamaları
    private Long assignedTruckId;
    private Long assignedTrailerId;
    private Long assignedDriverId;

    // Gümrük bilgileri
    private String customsAddress;
    private Long customsPersonId;

    // Tarih bilgileri
    private LocalDate loadingDate;
    private LocalDate deadlineDate;
    private LocalDate estimatedArrivalDate;

    // Sefer durumu
    private TripStatus tripStatus;
}
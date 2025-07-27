package com.baem.logisticapp.repository;

import com.baem.logisticapp.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    // Plate number ile araç bulma
    Optional<Vehicle> findByPlateNo(String plateNo);

    // VIN ile araç bulma
    Optional<Vehicle> findByVin(String vin);

    // Make ve model ile araç bulma
    List<Vehicle> findByMakeAndModel(String make, String model);

    // Aktif araç bulma
    List<Vehicle> findByIsActiveTrue();

    // Ownership type ile araç bulma
    List<Vehicle> findByOwnershipTypeId(Long ownershipTypeId);

    // Model year ile araç bulma
    List<Vehicle> findByModelYear(Short modelYear);

    // Satın alma tarihine göre araç bulma
    List<Vehicle> findByPurchaseDateAfter(LocalDate date);

    // Plate number ile araç var mı kontrol etme
    boolean existsByPlateNo(String plateNo);

    // VIN ile araç var mı kontrol etme
    boolean existsByVin(String vin);

    // Make ile araç bulma
    List<Vehicle> findByMake(String make);

    // Model ile araç bulma
    List<Vehicle> findByModel(String model);

    // Ownership type ve aktiflik durumuna göre araç bulma
    List<Vehicle> findByOwnershipTypeIdAndIsActiveTrue(Long ownershipTypeId);

    // Make ve aktiflik durumuna göre araç bulma
    List<Vehicle> findByMakeAndIsActiveTrue(String make);

    // Model ve aktiflik durumuna göre araç bulma
    List<Vehicle> findByModelAndIsActiveTrue(String model);

    // Model year ve aktiflik durumuna göre araç bulma
    List<Vehicle> findByModelYearAndIsActiveTrue(Short modelYear);

    // Belirli bir tarihten sonra satın alınan aktif araçlar
    List<Vehicle> findByPurchaseDateAfterAndIsActiveTrue(LocalDate date);

    // Make ve model year ile araç bulma
    List<Vehicle> findByMakeAndModelYear(String make, Short modelYear);

    // Model ve model year ile araç bulma
    List<Vehicle> findByModelAndModelYear(String model, Short modelYear);
}
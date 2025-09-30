package com.baem.logisticapp.repository;

import com.baem.logisticapp.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    // Plate number ile araç var mı kontrol etme
    boolean existsByPlateNo(String plateNo);

    // VIN ile araç var mı kontrol etme
    boolean existsByVin(String vin);

    // Consolidated search method with multiple filters
    @Query("SELECT v FROM Vehicle v WHERE " +
           "(:plateNo IS NULL OR v.plateNo = :plateNo) AND " +
           "(:vin IS NULL OR v.vin = :vin) AND " +
           "(:make IS NULL OR LOWER(v.make) LIKE LOWER(CONCAT('%', :make, '%'))) AND " +
           "(:model IS NULL OR LOWER(v.model) LIKE LOWER(CONCAT('%', :model, '%'))) AND " +
           "(:modelYear IS NULL OR v.modelYear = :modelYear) AND " +
           "(:ownershipTypeId IS NULL OR v.ownershipType.id = :ownershipTypeId) AND " +
           "(:active IS NULL OR v.isActive = :active) AND " +
           "(:purchasedAfter IS NULL OR v.purchaseDate > :purchasedAfter)")
    List<Vehicle> findVehiclesWithFilters(@Param("plateNo") String plateNo,
                                        @Param("vin") String vin,
                                        @Param("make") String make,
                                        @Param("model") String model,
                                        @Param("modelYear") Short modelYear,
                                        @Param("ownershipTypeId") Long ownershipTypeId,
                                        @Param("active") Boolean active,
                                        @Param("purchasedAfter") LocalDate purchasedAfter);

    // Statistics methods
    @Query("SELECT COUNT(v) FROM Vehicle v")
    Integer countAll();

    @Query("SELECT COUNT(v) FROM Vehicle v WHERE v.isActive = true")
    Integer countByStatus(@Param("status") String status);

    @Query("SELECT COUNT(v) FROM Vehicle v WHERE v.isActive = true")
    Integer countByActiveTrue();
}
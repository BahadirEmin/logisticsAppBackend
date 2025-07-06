package com.company.logistics.logistics_backend.domain.repository;

import com.company.logistics.logistics_backend.domain.entity.Trailer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TrailerRepository extends JpaRepository<Trailer, Long> {

    // Trailer number ile trailer bulma
    Optional<Trailer> findByTrailerNo(String trailerNo);

    // VIN ile trailer bulma
    Optional<Trailer> findByVin(String vin);

    // Make ve model ile trailer bulma
    List<Trailer> findByMakeAndModel(String make, String model);

    // Trailer type ile trailer bulma
    List<Trailer> findByTrailerType(String trailerType);

    // Aktif trailer bulma
    List<Trailer> findByIsActiveTrue();

    // Ownership type ile trailer bulma
    List<Trailer> findByOwnershipTypeId(Long ownershipTypeId);

    // Model year ile trailer bulma
    List<Trailer> findByModelYear(Short modelYear);

    // Belirli bir kapasiteden büyük trailer bulma
    List<Trailer> findByCapacityGreaterThan(Double capacity);

    // Belirli bir kapasiteden küçük trailer bulma
    List<Trailer> findByCapacityLessThan(Double capacity);

    // Kapasite aralığında trailer bulma
    List<Trailer> findByCapacityBetween(Double minCapacity, Double maxCapacity);

    // Satın alma tarihine göre trailer bulma
    List<Trailer> findByPurchaseDateAfter(LocalDate date);

    // Trailer number ile trailer var mı kontrol etme
    boolean existsByTrailerNo(String trailerNo);

    // VIN ile trailer var mı kontrol etme
    boolean existsByVin(String vin);

    // Make ile trailer bulma
    List<Trailer> findByMake(String make);

    // Model ile trailer bulma
    List<Trailer> findByModel(String model);

    // Ownership type ve aktiflik durumuna göre trailer bulma
    List<Trailer> findByOwnershipTypeIdAndIsActiveTrue(Long ownershipTypeId);

    // Trailer type ve aktiflik durumuna göre trailer bulma
    List<Trailer> findByTrailerTypeAndIsActiveTrue(String trailerType);

    // Belirli bir kapasiteden büyük aktif trailer bulma
    List<Trailer> findByCapacityGreaterThanAndIsActiveTrue(Double capacity);
}
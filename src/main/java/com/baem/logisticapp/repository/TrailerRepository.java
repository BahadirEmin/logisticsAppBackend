package com.baem.logisticapp.repository;

import com.baem.logisticapp.entity.Trailer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    // Trailer number ile trailer var mı kontrol etme
    boolean existsByTrailerNo(String trailerNo);

    // VIN ile trailer var mı kontrol etme
    boolean existsByVin(String vin);

    // Aktif trailer bulma
    List<Trailer> findByIsActiveTrue();

    // Consolidated search method with multiple filters
    @Query("SELECT t FROM Trailer t WHERE " +
           "(:trailerNo IS NULL OR t.trailerNo = :trailerNo) AND " +
           "(:vin IS NULL OR t.vin = :vin) AND " +
           "(:trailerType IS NULL OR LOWER(t.trailerType) LIKE LOWER(CONCAT('%', :trailerType, '%'))) AND " +
           "(:ownershipTypeId IS NULL OR t.ownershipType.id = :ownershipTypeId) AND " +
           "(:minCapacity IS NULL OR t.capacity >= :minCapacity) AND " +
           "(:maxCapacity IS NULL OR t.capacity <= :maxCapacity) AND " +
           "(:active IS NULL OR t.isActive = :active) AND " +
           "(:purchasedAfter IS NULL OR t.purchaseDate > :purchasedAfter)")
    List<Trailer> findTrailersWithFilters(@Param("trailerNo") String trailerNo,
                                        @Param("vin") String vin,
                                        @Param("trailerType") String trailerType,
                                        @Param("ownershipTypeId") Long ownershipTypeId,
                                        @Param("minCapacity") Double minCapacity,
                                        @Param("maxCapacity") Double maxCapacity,
                                        @Param("active") Boolean active,
                                        @Param("purchasedAfter") LocalDate purchasedAfter);
}
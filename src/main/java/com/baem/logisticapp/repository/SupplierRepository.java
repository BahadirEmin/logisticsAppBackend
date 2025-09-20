package com.baem.logisticapp.repository;

import com.baem.logisticapp.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {

    // Aktif tedarikçiler
    List<Supplier> findByIsActiveTrueOrderByCompanyNameAsc();

    // Şirket adına göre arama
    List<Supplier> findByCompanyNameContainingIgnoreCaseAndIsActiveTrue(String companyName);

    // Vergi numarasına göre bulma
    Optional<Supplier> findByTaxNumber(String taxNumber);

    // Şehre göre tedarikçiler
    List<Supplier> findByCityContainingIgnoreCaseAndIsActiveTrue(String city);

    // Ülkeye göre tedarikçiler
    List<Supplier> findByCountryContainingIgnoreCaseAndIsActiveTrue(String country);

    // Vergi numarası benzersizlik kontrolü (güncelleme için)
    @Query("SELECT s FROM Supplier s WHERE s.taxNumber = :taxNumber AND s.id != :id")
    Optional<Supplier> findByTaxNumberAndNotId(@Param("taxNumber") String taxNumber, @Param("id") Long id);

    // Aktif araç sayısına göre tedarikçiler
    @Query("SELECT s FROM Supplier s LEFT JOIN s.vehicles v WHERE s.isActive = true AND v.isActive = true GROUP BY s.id HAVING COUNT(v) > 0 ORDER BY s.companyName")
    List<Supplier> findSuppliersWithActiveVehicles();

    // Aktif trailer sayısına göre tedarikçiler
    @Query("SELECT s FROM Supplier s LEFT JOIN s.trailers t WHERE s.isActive = true AND t.isActive = true GROUP BY s.id HAVING COUNT(t) > 0 ORDER BY s.companyName")
    List<Supplier> findSuppliersWithActiveTrailers();

    // Tedarikçi istatistikleri için
    @Query("SELECT s.id, s.companyName, COUNT(v), COUNT(t) FROM Supplier s " +
           "LEFT JOIN s.vehicles v ON v.isActive = true " +
           "LEFT JOIN s.trailers t ON t.isActive = true " +
           "WHERE s.isActive = true " +
           "GROUP BY s.id, s.companyName " +
           "ORDER BY s.companyName")
    List<Object[]> getSupplierStatistics();
}

package com.baem.logisticapp.repository;

import com.baem.logisticapp.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    // Tax number ile müşteri bulma
    Optional<Customer> findByTaxNo(String taxNo);

    // Risk durumuna göre müşterileri bulma
    List<Customer> findByRiskStatusId(Long riskStatusId);

    // Kara listeye alınmış müşterileri bulma
    List<Customer> findByIsBlacklistedTrue();

    // Davalı müşterileri bulma
    List<Customer> findByIsInLawsuitTrue();

    // İsme göre müşteri arama (içerir)
    List<Customer> findByNameContainingIgnoreCase(String name);

    // Tax number ile müşteri var mı kontrol etme
    boolean existsByTaxNo(String taxNo);

    // Tax number ile müşteri var mı kontrol etme (null-safe)
    default boolean existsByTaxNoSafe(String taxNo) {
        return taxNo != null && !taxNo.trim().isEmpty() && existsByTaxNo(taxNo);
    }

    // Consolidated search method with multiple filters
    @Query("SELECT c FROM Customer c WHERE " +
           "(:name IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
           "(:riskStatusId IS NULL OR c.riskStatus.id = :riskStatusId) AND " +
           "(:blacklisted IS NULL OR c.isBlacklisted = :blacklisted) AND " +
           "(:inLawsuit IS NULL OR c.isInLawsuit = :inLawsuit) AND " +
           "(:taxNo IS NULL OR c.taxNo = :taxNo)")
    List<Customer> findCustomersWithFilters(@Param("name") String name, 
                                          @Param("riskStatusId") Long riskStatusId,
                                          @Param("blacklisted") Boolean blacklisted,
                                          @Param("inLawsuit") Boolean inLawsuit,
                                          @Param("taxNo") String taxNo);
}
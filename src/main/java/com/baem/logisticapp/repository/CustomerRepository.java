package com.baem.logisticapp.repository;

import com.baem.logisticapp.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
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

    // Risk durumu ve kara liste durumuna göre müşterileri bulma
    List<Customer> findByRiskStatusIdAndIsBlacklistedTrue(Long riskStatusId);

    // Risk durumu ve dava durumuna göre müşterileri bulma
    List<Customer> findByRiskStatusIdAndIsInLawsuitTrue(Long riskStatusId);
}
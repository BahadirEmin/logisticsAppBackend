package com.baem.logisticapp.repository;

import com.baem.logisticapp.entity.CustomerRiskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRiskStatusRepository extends JpaRepository<CustomerRiskStatus, Long> {
}
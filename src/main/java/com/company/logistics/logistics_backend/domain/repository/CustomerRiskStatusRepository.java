package com.company.logistics.logistics_backend.domain.repository;

import com.company.logistics.logistics_backend.domain.entity.CustomerRiskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRiskStatusRepository extends JpaRepository<CustomerRiskStatus, Long> {
}
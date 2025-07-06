package com.company.logistics.logistics_backend.domain.repository;

import com.company.logistics.logistics_backend.domain.entity.VehicleOwnershipType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleOwnershipTypeRepository extends JpaRepository<VehicleOwnershipType, Long> {
}
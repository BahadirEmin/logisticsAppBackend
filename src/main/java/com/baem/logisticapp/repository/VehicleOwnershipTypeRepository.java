package com.baem.logisticapp.repository;

import com.baem.logisticapp.entity.VehicleOwnershipType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleOwnershipTypeRepository extends JpaRepository<VehicleOwnershipType, Long> {
}
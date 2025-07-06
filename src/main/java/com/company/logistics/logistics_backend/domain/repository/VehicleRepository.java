package com.company.logistics.logistics_backend.domain.repository;

import com.company.logistics.logistics_backend.domain.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    List<Vehicle> findByIsActiveTrue();

    Optional<Vehicle> findByPlateNo(String plateNo);
}
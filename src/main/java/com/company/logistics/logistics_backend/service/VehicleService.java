package com.company.logistics.logistics_backend.service;

import com.company.logistics.logistics_backend.domain.dto.VehicleCreateDTO;
import com.company.logistics.logistics_backend.domain.dto.VehicleResponseDTO;
import com.company.logistics.logistics_backend.domain.dto.VehicleUpdateDTO;
import com.company.logistics.logistics_backend.domain.entity.Vehicle;
import com.company.logistics.logistics_backend.domain.entity.VehicleOwnershipType;
import com.company.logistics.logistics_backend.domain.repository.VehicleRepository;
import com.company.logistics.logistics_backend.domain.repository.VehicleOwnershipTypeRepository;
import com.company.logistics.logistics_backend.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final VehicleOwnershipTypeRepository vehicleOwnershipTypeRepository;

    public VehicleResponseDTO createVehicle(VehicleCreateDTO createDTO) {
        // Check if vehicle with same plate number already exists
        if (vehicleRepository.existsByPlateNo(createDTO.getPlateNo())) {
            throw new IllegalArgumentException(
                    "Vehicle with plate number " + createDTO.getPlateNo() + " already exists");
        }

        // Check if vehicle with same VIN already exists
        if (vehicleRepository.existsByVin(createDTO.getVin())) {
            throw new IllegalArgumentException("Vehicle with VIN " + createDTO.getVin() + " already exists");
        }

        // Get ownership type
        VehicleOwnershipType ownershipType = vehicleOwnershipTypeRepository.findById(createDTO.getOwnershipTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("Ownership type not found"));

        Vehicle vehicle = Vehicle.builder()
                .plateNo(createDTO.getPlateNo())
                .make(createDTO.getMake())
                .model(createDTO.getModel())
                .modelYear(createDTO.getModelYear())
                .ownershipType(ownershipType)
                .purchaseDate(createDTO.getPurchaseDate())
                .vin(createDTO.getVin())
                .isActive(true)
                .build();

        return convertToDTO(vehicleRepository.save(vehicle));
    }

    public VehicleResponseDTO getVehicleById(Long id) {
        return vehicleRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found"));
    }

    public List<VehicleResponseDTO> getAllVehicles() {
        return vehicleRepository.findAll().stream()
                .map(this::convertToDTO)
                .toList();
    }

    public VehicleResponseDTO updateVehicle(Long id, VehicleUpdateDTO updateDTO) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found"));

        // Check if plate number is being changed and if it already exists
        if (!vehicle.getPlateNo().equals(updateDTO.getPlateNo()) &&
                vehicleRepository.existsByPlateNo(updateDTO.getPlateNo())) {
            throw new IllegalArgumentException(
                    "Vehicle with plate number " + updateDTO.getPlateNo() + " already exists");
        }

        // Check if VIN is being changed and if it already exists
        if (!vehicle.getVin().equals(updateDTO.getVin()) &&
                vehicleRepository.existsByVin(updateDTO.getVin())) {
            throw new IllegalArgumentException("Vehicle with VIN " + updateDTO.getVin() + " already exists");
        }

        // Get ownership type
        VehicleOwnershipType ownershipType = vehicleOwnershipTypeRepository.findById(updateDTO.getOwnershipTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("Ownership type not found"));

        vehicle.setPlateNo(updateDTO.getPlateNo());
        vehicle.setMake(updateDTO.getMake());
        vehicle.setModel(updateDTO.getModel());
        vehicle.setModelYear(updateDTO.getModelYear());
        vehicle.setOwnershipType(ownershipType);
        vehicle.setPurchaseDate(updateDTO.getPurchaseDate());
        vehicle.setVin(updateDTO.getVin());
        vehicle.setIsActive(updateDTO.getIsActive() != null ? updateDTO.getIsActive() : true);

        return convertToDTO(vehicleRepository.save(vehicle));
    }

    public void deleteVehicle(Long id) {
        if (!vehicleRepository.existsById(id)) {
            throw new ResourceNotFoundException("Vehicle not found");
        }
        vehicleRepository.deleteById(id);
    }

    public VehicleResponseDTO getVehicleByPlateNo(String plateNo) {
        return vehicleRepository.findByPlateNo(plateNo)
                .map(this::convertToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found"));
    }

    public VehicleResponseDTO getVehicleByVin(String vin) {
        return vehicleRepository.findByVin(vin)
                .map(this::convertToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found"));
    }

    public List<VehicleResponseDTO> getVehiclesByMakeAndModel(String make, String model) {
        return vehicleRepository.findByMakeAndModel(make, model).stream()
                .map(this::convertToDTO)
                .toList();
    }

    public List<VehicleResponseDTO> getActiveVehicles() {
        return vehicleRepository.findByIsActiveTrue().stream()
                .map(this::convertToDTO)
                .toList();
    }

    public List<VehicleResponseDTO> getVehiclesByOwnershipType(Long ownershipTypeId) {
        return vehicleRepository.findByOwnershipTypeId(ownershipTypeId).stream()
                .map(this::convertToDTO)
                .toList();
    }

    public List<VehicleResponseDTO> getVehiclesByModelYear(Short modelYear) {
        return vehicleRepository.findByModelYear(modelYear).stream()
                .map(this::convertToDTO)
                .toList();
    }

    public List<VehicleResponseDTO> getVehiclesPurchasedAfter(java.time.LocalDate date) {
        return vehicleRepository.findByPurchaseDateAfter(date).stream()
                .map(this::convertToDTO)
                .toList();
    }

    public List<VehicleResponseDTO> getVehiclesByMake(String make) {
        return vehicleRepository.findByMake(make).stream()
                .map(this::convertToDTO)
                .toList();
    }

    public List<VehicleResponseDTO> getVehiclesByModel(String model) {
        return vehicleRepository.findByModel(model).stream()
                .map(this::convertToDTO)
                .toList();
    }

    public List<VehicleResponseDTO> getActiveVehiclesByOwnershipType(Long ownershipTypeId) {
        return vehicleRepository.findByOwnershipTypeIdAndIsActiveTrue(ownershipTypeId).stream()
                .map(this::convertToDTO)
                .toList();
    }

    public List<VehicleResponseDTO> getActiveVehiclesByMake(String make) {
        return vehicleRepository.findByMakeAndIsActiveTrue(make).stream()
                .map(this::convertToDTO)
                .toList();
    }

    public List<VehicleResponseDTO> getActiveVehiclesByModel(String model) {
        return vehicleRepository.findByModelAndIsActiveTrue(model).stream()
                .map(this::convertToDTO)
                .toList();
    }

    public List<VehicleResponseDTO> getActiveVehiclesByModelYear(Short modelYear) {
        return vehicleRepository.findByModelYearAndIsActiveTrue(modelYear).stream()
                .map(this::convertToDTO)
                .toList();
    }

    public List<VehicleResponseDTO> getActiveVehiclesPurchasedAfter(java.time.LocalDate date) {
        return vehicleRepository.findByPurchaseDateAfterAndIsActiveTrue(date).stream()
                .map(this::convertToDTO)
                .toList();
    }

    public List<VehicleResponseDTO> getVehiclesByMakeAndModelYear(String make, Short modelYear) {
        return vehicleRepository.findByMakeAndModelYear(make, modelYear).stream()
                .map(this::convertToDTO)
                .toList();
    }

    public List<VehicleResponseDTO> getVehiclesByModelAndModelYear(String model, Short modelYear) {
        return vehicleRepository.findByModelAndModelYear(model, modelYear).stream()
                .map(this::convertToDTO)
                .toList();
    }

    private VehicleResponseDTO convertToDTO(Vehicle vehicle) {
        return VehicleResponseDTO.builder()
                .id(vehicle.getId())
                .plateNo(vehicle.getPlateNo())
                .make(vehicle.getMake())
                .model(vehicle.getModel())
                .modelYear(vehicle.getModelYear())
                .ownershipTypeId(vehicle.getOwnershipType().getId())
                .ownershipTypeName(vehicle.getOwnershipType().getOwnershipName())
                .purchaseDate(vehicle.getPurchaseDate())
                .vin(vehicle.getVin())
                .isActive(vehicle.getIsActive())
                .build();
    }
}
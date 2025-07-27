package com.baem.logisticapp.service;

import com.baem.logisticapp.dto.VehicleCreateDTO;
import com.baem.logisticapp.dto.VehicleResponseDTO;
import com.baem.logisticapp.dto.VehicleUpdateDTO;
import com.baem.logisticapp.entity.Vehicle;
import com.baem.logisticapp.entity.VehicleOwnershipType;
import com.baem.logisticapp.exception.ResourceNotFoundException;
import com.baem.logisticapp.repository.VehicleOwnershipTypeRepository;
import com.baem.logisticapp.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;


@Service
@RequiredArgsConstructor
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepository;
    private final VehicleOwnershipTypeRepository vehicleOwnershipTypeRepository;

    @Override
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

    @Override
    public VehicleResponseDTO getVehicleById(Long id) {
        return vehicleRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found"));
    }

    @Override
    public List<VehicleResponseDTO> getAllVehicles() {
        return vehicleRepository.findAll().stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
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

    @Override
    public void deleteVehicle(Long id) {
        if (!vehicleRepository.existsById(id)) {
            throw new ResourceNotFoundException("Vehicle not found");
        }
        vehicleRepository.deleteById(id);
    }

    @Override
    public VehicleResponseDTO getVehicleByPlateNo(String plateNo) {
        return vehicleRepository.findByPlateNo(plateNo)
                .map(this::convertToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found"));
    }

    @Override
    public VehicleResponseDTO getVehicleByVin(String vin) {
        return vehicleRepository.findByVin(vin)
                .map(this::convertToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found"));
    }

    @Override
    public List<VehicleResponseDTO> getVehiclesByMakeAndModel(String make, String model) {
        return vehicleRepository.findByMakeAndModel(make, model).stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    public List<VehicleResponseDTO> getActiveVehicles() {
        return vehicleRepository.findByIsActiveTrue().stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    public List<VehicleResponseDTO> getVehiclesByOwnershipType(Long ownershipTypeId) {
        return vehicleRepository.findByOwnershipTypeId(ownershipTypeId).stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    public List<VehicleResponseDTO> getVehiclesByModelYear(Short modelYear) {
        return vehicleRepository.findByModelYear(modelYear).stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    public List<VehicleResponseDTO> getVehiclesPurchasedAfter(java.time.LocalDate date) {
        return vehicleRepository.findByPurchaseDateAfter(date).stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    public List<VehicleResponseDTO> getVehiclesByMake(String make) {
        return vehicleRepository.findByMake(make).stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    public List<VehicleResponseDTO> getVehiclesByModel(String model) {
        return vehicleRepository.findByModel(model).stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    public List<VehicleResponseDTO> getActiveVehiclesByOwnershipType(Long ownershipTypeId) {
        return vehicleRepository.findByOwnershipTypeIdAndIsActiveTrue(ownershipTypeId).stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    public List<VehicleResponseDTO> getActiveVehiclesByMake(String make) {
        return vehicleRepository.findByMakeAndIsActiveTrue(make).stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    public List<VehicleResponseDTO> getActiveVehiclesByModel(String model) {
        return vehicleRepository.findByModelAndIsActiveTrue(model).stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    public List<VehicleResponseDTO> getActiveVehiclesByModelYear(Short modelYear) {
        return vehicleRepository.findByModelYearAndIsActiveTrue(modelYear).stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    public List<VehicleResponseDTO> getActiveVehiclesPurchasedAfter(LocalDate date) {
        return vehicleRepository.findByPurchaseDateAfterAndIsActiveTrue(date).stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    public List<VehicleResponseDTO> getVehiclesByMakeAndModelYear(String make, Short modelYear) {
        return vehicleRepository.findByMakeAndModelYear(make, modelYear).stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
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

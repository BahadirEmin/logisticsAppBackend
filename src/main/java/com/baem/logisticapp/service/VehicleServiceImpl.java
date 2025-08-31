package com.baem.logisticapp.service;

import com.baem.logisticapp.dto.VehicleCreateDTO;
import com.baem.logisticapp.dto.VehicleResponseDTO;
import com.baem.logisticapp.dto.VehicleUpdateDTO;
import com.baem.logisticapp.entity.Vehicle;
import com.baem.logisticapp.entity.VehicleOwnershipType;
import com.baem.logisticapp.exception.ResourceNotFoundException;
import com.baem.logisticapp.repository.VehicleOwnershipTypeRepository;
import com.baem.logisticapp.repository.VehicleRepository;
import com.baem.logisticapp.validator.VehicleValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;


@Service
@RequiredArgsConstructor
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepository;
    private final VehicleOwnershipTypeRepository vehicleOwnershipTypeRepository;
    private final VehicleValidator vehicleValidator;

    @Override
    public VehicleResponseDTO createVehicle(VehicleCreateDTO createDTO) {
        vehicleValidator.validateForCreate(createDTO);

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

        vehicleValidator.validateForUpdate(updateDTO, vehicle.getPlateNo(), vehicle.getVin());

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
    public List<VehicleResponseDTO> searchVehicles(String plateNo, String vin, String make, String model, 
                                                   Short modelYear, Long ownershipTypeId, Boolean active, LocalDate purchasedAfter) {
        return vehicleRepository.findVehiclesWithFilters(plateNo, vin, make, model, modelYear, ownershipTypeId, active, purchasedAfter).stream()
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

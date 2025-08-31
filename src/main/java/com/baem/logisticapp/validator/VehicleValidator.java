package com.baem.logisticapp.validator;

import com.baem.logisticapp.dto.VehicleCreateDTO;
import com.baem.logisticapp.dto.VehicleUpdateDTO;
import com.baem.logisticapp.repository.VehicleRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class VehicleValidator {
    private final VehicleRepository vehicleRepository;

    public VehicleValidator(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public void validateForCreate(VehicleCreateDTO createDTO) {
        int currentYear = LocalDate.now().getYear();
        if (createDTO.getModelYear() != null && createDTO.getModelYear() > currentYear) {
            throw new IllegalArgumentException("Model year cannot be in the future.");
        }
        if (createDTO.getPurchaseDate() != null && createDTO.getPurchaseDate().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Purchase date cannot be in the future.");
        }
        if (vehicleRepository.existsByPlateNo(createDTO.getPlateNo())) {
            throw new IllegalArgumentException(
                    "Vehicle with plate number " + createDTO.getPlateNo() + " already exists");
        }
        if (vehicleRepository.existsByVin(createDTO.getVin())) {
            throw new IllegalArgumentException("Vehicle with VIN " + createDTO.getVin() + " already exists");
        }
    }

    public void validateForUpdate(VehicleUpdateDTO updateDTO, String currentPlateNo, String currentVin) {
        int currentYear = LocalDate.now().getYear();
        if (updateDTO.getModelYear() != null && updateDTO.getModelYear() > currentYear) {
            throw new IllegalArgumentException("Model year cannot be in the future.");
        }
        if (updateDTO.getPurchaseDate() != null && updateDTO.getPurchaseDate().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Purchase date cannot be in the future.");
        }
        if (!currentPlateNo.equals(updateDTO.getPlateNo()) && vehicleRepository.existsByPlateNo(updateDTO.getPlateNo())) {
            throw new IllegalArgumentException(
                    "Vehicle with plate number " + updateDTO.getPlateNo() + " already exists");
        }
        if (!currentVin.equals(updateDTO.getVin()) && vehicleRepository.existsByVin(updateDTO.getVin())) {
            throw new IllegalArgumentException("Vehicle with VIN " + updateDTO.getVin() + " already exists");
        }
    }
}

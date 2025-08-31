package com.baem.logisticapp.validator;

import com.baem.logisticapp.dto.TrailerCreateDTO;
import com.baem.logisticapp.dto.TrailerUpdateDTO;
import com.baem.logisticapp.repository.TrailerRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class TrailerValidator {
    private final TrailerRepository trailerRepository;

    public TrailerValidator(TrailerRepository trailerRepository) {
        this.trailerRepository = trailerRepository;
    }

    public void validateForCreate(TrailerCreateDTO createDTO) {
        int currentYear = LocalDate.now().getYear();
        if (createDTO.getModelYear() != null && createDTO.getModelYear() > currentYear) {
            throw new IllegalArgumentException("Model year cannot be in the future.");
        }
        if (createDTO.getPurchaseDate() != null && createDTO.getPurchaseDate().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Purchase date cannot be in the future.");
        }
        if (createDTO.getCapacity() != null && createDTO.getCapacity() <= 0) {
            throw new IllegalArgumentException("Capacity must be positive.");
        }
        if (createDTO.getLength() != null && createDTO.getLength() <= 0) {
            throw new IllegalArgumentException("Length must be positive.");
        }
        if (createDTO.getWidth() != null && createDTO.getWidth() <= 0) {
            throw new IllegalArgumentException("Width must be positive.");
        }
        if (createDTO.getHeight() != null && createDTO.getHeight() <= 0) {
            throw new IllegalArgumentException("Height must be positive.");
        }
        if (trailerRepository.existsByTrailerNo(createDTO.getTrailerNo())) {
            throw new IllegalArgumentException("Trailer with number " + createDTO.getTrailerNo() + " already exists");
        }
    }

    public void validateForUpdate(TrailerUpdateDTO updateDTO, String currentTrailerNo) {
        int currentYear = LocalDate.now().getYear();
        if (updateDTO.getModelYear() != null && updateDTO.getModelYear() > currentYear) {
            throw new IllegalArgumentException("Model year cannot be in the future.");
        }
        if (updateDTO.getPurchaseDate() != null && updateDTO.getPurchaseDate().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Purchase date cannot be in the future.");
        }
        if (updateDTO.getCapacity() != null && updateDTO.getCapacity() <= 0) {
            throw new IllegalArgumentException("Capacity must be positive.");
        }
        if (updateDTO.getLength() != null && updateDTO.getLength() <= 0) {
            throw new IllegalArgumentException("Length must be positive.");
        }
        if (updateDTO.getWidth() != null && updateDTO.getWidth() <= 0) {
            throw new IllegalArgumentException("Width must be positive.");
        }
        if (updateDTO.getHeight() != null && updateDTO.getHeight() <= 0) {
            throw new IllegalArgumentException("Height must be positive.");
        }
        if (!currentTrailerNo.equals(updateDTO.getTrailerNo()) && trailerRepository.existsByTrailerNo(updateDTO.getTrailerNo())) {
            throw new IllegalArgumentException("Trailer with number " + updateDTO.getTrailerNo() + " already exists");
        }
    }
}

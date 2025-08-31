package com.baem.logisticapp.validator;

import com.baem.logisticapp.dto.OrderCreateDTO;
import com.baem.logisticapp.dto.OrderUpdateDTO;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class OrderValidator {
    public void validateForCreate(OrderCreateDTO createDTO) {
        LocalDate today = LocalDate.now();
        if (createDTO.getLoadingDate() != null && createDTO.getLoadingDate().isBefore(today)) {
            throw new IllegalArgumentException("Loading date cannot be in the past.");
        }
        if (createDTO.getDeadlineDate() != null && createDTO.getDeadlineDate().isBefore(today)) {
            throw new IllegalArgumentException("Deadline date cannot be in the past.");
        }
        if (createDTO.getEstimatedArrivalDate() != null && createDTO.getEstimatedArrivalDate().isBefore(today)) {
            throw new IllegalArgumentException("Estimated arrival date cannot be in the past.");
        }
        if (createDTO.getLoadingDate() != null && createDTO.getDeadlineDate() != null && createDTO.getDeadlineDate().isBefore(createDTO.getLoadingDate())) {
            throw new IllegalArgumentException("Deadline date cannot be before loading date.");
        }
        if (createDTO.getCargoWeightKg() != null && createDTO.getCargoWeightKg().signum() < 0) {
            throw new IllegalArgumentException("Cargo weight cannot be negative.");
        }
        if (createDTO.getCargoWidth() != null && createDTO.getCargoWidth().signum() < 0) {
            throw new IllegalArgumentException("Cargo width cannot be negative.");
        }
        if (createDTO.getCargoLength() != null && createDTO.getCargoLength().signum() < 0) {
            throw new IllegalArgumentException("Cargo length cannot be negative.");
        }
        if (createDTO.getCargoHeight() != null && createDTO.getCargoHeight().signum() < 0) {
            throw new IllegalArgumentException("Cargo height cannot be negative.");
        }
    }

    public void validateForUpdate(OrderUpdateDTO updateDTO) {
        LocalDate today = LocalDate.now();
        if (updateDTO.getLoadingDate() != null && updateDTO.getLoadingDate().isBefore(today)) {
            throw new IllegalArgumentException("Loading date cannot be in the past.");
        }
        if (updateDTO.getDeadlineDate() != null && updateDTO.getDeadlineDate().isBefore(today)) {
            throw new IllegalArgumentException("Deadline date cannot be in the past.");
        }
        if (updateDTO.getEstimatedArrivalDate() != null && updateDTO.getEstimatedArrivalDate().isBefore(today)) {
            throw new IllegalArgumentException("Estimated arrival date cannot be in the past.");
        }
        if (updateDTO.getLoadingDate() != null && updateDTO.getDeadlineDate() != null && updateDTO.getDeadlineDate().isBefore(updateDTO.getLoadingDate())) {
            throw new IllegalArgumentException("Deadline date cannot be before loading date.");
        }
        if (updateDTO.getCargoWeightKg() != null && updateDTO.getCargoWeightKg().signum() < 0) {
            throw new IllegalArgumentException("Cargo weight cannot be negative.");
        }
        if (updateDTO.getCargoWidth() != null && updateDTO.getCargoWidth().signum() < 0) {
            throw new IllegalArgumentException("Cargo width cannot be negative.");
        }
        if (updateDTO.getCargoLength() != null && updateDTO.getCargoLength().signum() < 0) {
            throw new IllegalArgumentException("Cargo length cannot be negative.");
        }
        if (updateDTO.getCargoHeight() != null && updateDTO.getCargoHeight().signum() < 0) {
            throw new IllegalArgumentException("Cargo height cannot be negative.");
        }
    }
}

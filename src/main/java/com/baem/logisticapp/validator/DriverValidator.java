package com.baem.logisticapp.validator;

import com.baem.logisticapp.dto.DriverCreateDTO;
import com.baem.logisticapp.dto.DriverUpdateDTO;
import com.baem.logisticapp.repository.DriverRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DriverValidator {
    private final DriverRepository driverRepository;

    public DriverValidator(DriverRepository driverRepository) {
        this.driverRepository = driverRepository;
    }

    public void validateForCreate(DriverCreateDTO createDTO) {
        if (createDTO.getFirstName() == null || createDTO.getFirstName().trim().isEmpty()) {
            throw new IllegalArgumentException("First name is required");
        }
        if (createDTO.getLastName() == null || createDTO.getLastName().trim().isEmpty()) {
            throw new IllegalArgumentException("Last name is required");
        }
        if (createDTO.getLicenseNo() == null || createDTO.getLicenseNo().trim().isEmpty()) {
            throw new IllegalArgumentException("License number is required");
        }
        if (driverRepository.findByLicenseNo(createDTO.getLicenseNo()).isPresent()) {
            throw new IllegalArgumentException("A driver with this license number already exists");
        }
        if (createDTO.getLicenseClass() == null || createDTO.getLicenseClass().trim().isEmpty()) {
            throw new IllegalArgumentException("License class is required");
        }
        if (createDTO.getPassportExpiry() == null || createDTO.getPassportExpiry().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Passport expiry date must be in the future");
        }
        if (createDTO.getVisaExpiry() == null || createDTO.getVisaExpiry().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Visa expiry date must be in the future");
        }
        if (createDTO.getResidencePermitExpiry() == null || createDTO.getResidencePermitExpiry().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Residence permit expiry date must be in the future");
        }
        if (createDTO.getPhoneNumber() == null || createDTO.getPhoneNumber().trim().isEmpty()) {
            throw new IllegalArgumentException("Phone number is required");
        }
        if (driverRepository.findByPhoneNumber(createDTO.getPhoneNumber()).isPresent()) {
            throw new IllegalArgumentException("A driver with this phone number already exists");
        }
        if (!createDTO.getPhoneNumber().matches("^\\+?[0-9]{10,15}$")) {
            throw new IllegalArgumentException("Phone number must be valid and contain only digits, optionally starting with +");
        }
        if (createDTO.getEmail() == null || createDTO.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (driverRepository.findByEmail(createDTO.getEmail()).isPresent()) {
            throw new IllegalArgumentException("A driver with this email already exists");
        }
        if (!createDTO.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new IllegalArgumentException("Email must be a valid email address");
        }
    }

    public void validateForUpdate(Long driverId, DriverUpdateDTO updateDTO) {
        if (updateDTO.getFirstName() == null || updateDTO.getFirstName().trim().isEmpty()) {
            throw new IllegalArgumentException("First name is required");
        }
        if (updateDTO.getLastName() == null || updateDTO.getLastName().trim().isEmpty()) {
            throw new IllegalArgumentException("Last name is required");
        }
        if (updateDTO.getLicenseNo() == null || updateDTO.getLicenseNo().trim().isEmpty()) {
            throw new IllegalArgumentException("License number is required");
        }
        
        // License number uniqueness check
        driverRepository.findByLicenseNo(updateDTO.getLicenseNo().trim())
                .ifPresent(driver -> {
                    if (!driver.getId().equals(driverId)) {
                        throw new IllegalArgumentException("A driver with this license number already exists");
                    }
                });
                
        if (updateDTO.getLicenseClass() == null || updateDTO.getLicenseClass().trim().isEmpty()) {
            throw new IllegalArgumentException("License class is required");
        }
        if (updateDTO.getPassportExpiry() == null || updateDTO.getPassportExpiry().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Passport expiry date must be in the future");
        }
        if (updateDTO.getVisaExpiry() == null || updateDTO.getVisaExpiry().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Visa expiry date must be in the future");
        }
        if (updateDTO.getResidencePermitExpiry() == null || updateDTO.getResidencePermitExpiry().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Residence permit expiry date must be in the future");
        }
        if (updateDTO.getPhoneNumber() == null || updateDTO.getPhoneNumber().trim().isEmpty()) {
            throw new IllegalArgumentException("Phone number is required");
        }
        
        // Phone number uniqueness check
        driverRepository.findByPhoneNumber(updateDTO.getPhoneNumber().trim())
                .ifPresent(driver -> {
                    if (!driver.getId().equals(driverId)) {
                        throw new IllegalArgumentException("A driver with this phone number already exists");
                    }
                });
                
        if (!updateDTO.getPhoneNumber().matches("^\\+?[0-9]{10,15}$")) {
            throw new IllegalArgumentException("Phone number must be valid and contain only digits, optionally starting with +");
        }
        if (updateDTO.getEmail() == null || updateDTO.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        
        // Email uniqueness check
        driverRepository.findByEmail(updateDTO.getEmail().trim())
                .ifPresent(driver -> {
                    if (!driver.getId().equals(driverId)) {
                        throw new IllegalArgumentException("A driver with this email already exists");
                    }
                });
                
        if (!updateDTO.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new IllegalArgumentException("Email must be a valid email address");
        }
    }
}

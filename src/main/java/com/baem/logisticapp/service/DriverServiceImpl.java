package com.baem.logisticapp.service;

import com.baem.logisticapp.dto.DriverCreateDTO;
import com.baem.logisticapp.dto.DriverResponseDTO;
import com.baem.logisticapp.dto.DriverUpdateDTO;
import com.baem.logisticapp.entity.Driver;
import com.baem.logisticapp.repository.DriverRepository;
import com.baem.logisticapp.exception.ResourceNotFoundException;
import com.baem.logisticapp.validator.DriverValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class DriverServiceImpl implements DriverService {

    private final DriverRepository driverRepository;
    private final DriverValidator driverValidator;
    
    public DriverServiceImpl(DriverRepository driverRepository, DriverValidator driverValidator) {
        this.driverRepository = driverRepository;
        this.driverValidator = driverValidator;
    }

    @Override
    public DriverResponseDTO createDriver(DriverCreateDTO createDTO) {
        // Use validator for validation
        driverValidator.validateForCreate(createDTO);

        Driver driver = Driver.builder()
                .firstName(createDTO.getFirstName().trim())
                .lastName(createDTO.getLastName().trim())
                .licenseNo(createDTO.getLicenseNo().trim())
                .licenseClass(createDTO.getLicenseClass().trim())
                .passportExpiry(createDTO.getPassportExpiry())
                .visaExpiry(createDTO.getVisaExpiry())
                .residencePermitExpiry(createDTO.getResidencePermitExpiry())
                .phoneNumber(createDTO.getPhoneNumber().trim())
                .email(createDTO.getEmail().trim())
                .isActive(true)
                .build();

        return convertToDTO(driverRepository.save(driver));
    }

    @Override
    public DriverResponseDTO getDriverById(Long id) {
        return driverRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found"));
    }

    @Override
    public List<DriverResponseDTO> getAllDrivers() {
        return driverRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public DriverResponseDTO updateDriver(Long id, DriverUpdateDTO updateDTO) {
        Driver driver = driverRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found"));

        // Use validator for validation
        driverValidator.validateForUpdate(id, updateDTO);

        driver.setFirstName(updateDTO.getFirstName().trim());
        driver.setLastName(updateDTO.getLastName().trim());
        driver.setLicenseNo(updateDTO.getLicenseNo().trim());
        driver.setLicenseClass(updateDTO.getLicenseClass().trim());
        driver.setPassportExpiry(updateDTO.getPassportExpiry());
        driver.setVisaExpiry(updateDTO.getVisaExpiry());
        driver.setResidencePermitExpiry(updateDTO.getResidencePermitExpiry());
        driver.setPhoneNumber(updateDTO.getPhoneNumber().trim());
        driver.setEmail(updateDTO.getEmail().trim());
        if (updateDTO.getIsActive() != null) {
            driver.setIsActive(updateDTO.getIsActive());
        }

        return convertToDTO(driverRepository.save(driver));
    }

    @Override
    public void deleteDriver(Long id) {
        if (!driverRepository.existsById(id)) {
            throw new ResourceNotFoundException("Driver not found");
        }
        driverRepository.deleteById(id);
    }

    @Override
    public List<DriverResponseDTO> searchDrivers(String licenseNo, Boolean active) {
        return driverRepository.findDriversWithFilters(licenseNo, active).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private DriverResponseDTO convertToDTO(Driver driver) {
        return DriverResponseDTO.builder()
                .id(driver.getId())
                .firstName(driver.getFirstName())
                .lastName(driver.getLastName())
                .licenseNo(driver.getLicenseNo())
                .licenseClass(driver.getLicenseClass())
                .passportExpiry(driver.getPassportExpiry())
                .visaExpiry(driver.getVisaExpiry())
                .residencePermitExpiry(driver.getResidencePermitExpiry())
                .phoneNumber(driver.getPhoneNumber())
                .email(driver.getEmail())
                .isActive(driver.getIsActive())
                .build();
    }
}
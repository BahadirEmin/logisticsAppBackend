package com.baem.logisticapp.service;

import com.baem.logisticapp.dto.DriverCreateDTO;
import com.baem.logisticapp.dto.DriverResponseDTO;
import com.baem.logisticapp.dto.DriverUpdateDTO;
import com.baem.logisticapp.entity.Driver;
import com.baem.logisticapp.repository.DriverRepository;
import com.baem.logisticapp.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class DriverServiceImpl implements DriverService {

    private final DriverRepository driverRepository;

    @Override
    public DriverResponseDTO createDriver(DriverCreateDTO createDTO) {
        Driver driver = Driver.builder()
                .firstName(createDTO.getFirstName())
                .lastName(createDTO.getLastName())
                .licenseNo(createDTO.getLicenseNo())
                .licenseClass(createDTO.getLicenseClass())
                .passportExpiry(createDTO.getPassportExpiry())
                .visaExpiry(createDTO.getVisaExpiry())
                .residencePermitExpiry(createDTO.getResidencePermitExpiry())
                .phoneNumber(createDTO.getPhoneNumber())
                .email(createDTO.getEmail())
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

        driver.setFirstName(updateDTO.getFirstName());
        driver.setLastName(updateDTO.getLastName());
        driver.setLicenseNo(updateDTO.getLicenseNo());
        driver.setLicenseClass(updateDTO.getLicenseClass());
        driver.setPassportExpiry(updateDTO.getPassportExpiry());
        driver.setVisaExpiry(updateDTO.getVisaExpiry());
        driver.setResidencePermitExpiry(updateDTO.getResidencePermitExpiry());
        driver.setPhoneNumber(updateDTO.getPhoneNumber());
        driver.setEmail(updateDTO.getEmail());
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
    public List<DriverResponseDTO> getActiveDrivers() {
        return driverRepository.findByIsActiveTrue().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public DriverResponseDTO getDriverByLicenseNo(String licenseNo) {
        return driverRepository.findByLicenseNo(licenseNo)
                .map(this::convertToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found"));
    }



    @Override
    public List<DriverResponseDTO> getDriversByLicenseClass(String licenseClass) {
        return driverRepository.findByLicenseClass(licenseClass).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<DriverResponseDTO> getDriversWithExpiringPassport(LocalDate expiryDate) {
        return driverRepository.findByPassportExpiryBefore(expiryDate).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<DriverResponseDTO> getDriversWithExpiringVisa(LocalDate expiryDate) {
        return driverRepository.findByVisaExpiryBefore(expiryDate).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<DriverResponseDTO> getDriversWithExpiringResidencePermit(LocalDate expiryDate) {
        return driverRepository.findByResidencePermitExpiryBefore(expiryDate).stream()
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
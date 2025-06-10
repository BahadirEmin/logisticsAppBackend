package com.company.logistics.logistics_backend.service;

import com.company.logistics.logistics_backend.domain.dto.DriverCreateDTO;
import com.company.logistics.logistics_backend.domain.dto.DriverResponseDTO;
import com.company.logistics.logistics_backend.domain.dto.DriverUpdateDTO;
import com.company.logistics.logistics_backend.domain.entity.Driver;
import com.company.logistics.logistics_backend.domain.repository.DriverRepository;
import com.company.logistics.logistics_backend.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class DriverService {

    private final DriverRepository driverRepository;

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

    @Transactional(readOnly = true)
    public DriverResponseDTO getDriverById(Long id) {
        return driverRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found"));
    }

    @Transactional(readOnly = true)
    public List<DriverResponseDTO> getAllDrivers() {
        return driverRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

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

    public void deleteDriver(Long id) {
        if (!driverRepository.existsById(id)) {
            throw new ResourceNotFoundException("Driver not found");
        }
        driverRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<DriverResponseDTO> getActiveDrivers() {
        return driverRepository.findByIsActiveTrue().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public DriverResponseDTO getDriverByLicenseNo(String licenseNo) {
        return driverRepository.findByLicenseNo(licenseNo)
                .map(this::convertToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found"));
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

    // Belirli bir lisans sınıfına sahip sürücüleri getir
    @Transactional(readOnly = true)
    public List<DriverResponseDTO> getDriversByLicenseClass(String licenseClass) {
        return driverRepository.findByLicenseClass(licenseClass).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Pasaport süresi yakında dolacak sürücüleri getir
    @Transactional(readOnly = true)
    public List<DriverResponseDTO> getDriversWithExpiringPassport(LocalDate expiryDate) {
        return driverRepository.findByPassportExpiryBefore(expiryDate).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Vize süresi yakında dolacak sürücüleri getir
    @Transactional(readOnly = true)
    public List<DriverResponseDTO> getDriversWithExpiringVisa(LocalDate expiryDate) {
        return driverRepository.findByVisaExpiryBefore(expiryDate).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // İkamet izni süresi yakında dolacak sürücüleri getir
    @Transactional(readOnly = true)
    public List<DriverResponseDTO> getDriversWithExpiringResidencePermit(LocalDate expiryDate) {
        return driverRepository.findByResidencePermitExpiryBefore(expiryDate).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
}
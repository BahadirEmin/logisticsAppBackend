package com.baem.logisticapp.service;

import com.baem.logisticapp.dto.SupplierCreateDTO;
import com.baem.logisticapp.dto.SupplierResponseDTO;
import com.baem.logisticapp.dto.SupplierUpdateDTO;
import com.baem.logisticapp.entity.Supplier;
import com.baem.logisticapp.exception.ResourceNotFoundException;
import com.baem.logisticapp.repository.SupplierRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository;

    public SupplierServiceImpl(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    @Override
    public SupplierResponseDTO createSupplier(SupplierCreateDTO createDTO) {
        // Vergi numarası benzersizlik kontrolü
        if (supplierRepository.findByTaxNumber(createDTO.getTaxNumber()).isPresent()) {
            throw new IllegalArgumentException("A supplier with this tax number already exists");
        }

        Supplier supplier = Supplier.builder()
                .companyName(createDTO.getCompanyName())
                .taxNumber(createDTO.getTaxNumber())
                .taxOffice(createDTO.getTaxOffice())
                .tradeRegistryNumber(createDTO.getTradeRegistryNumber())
                .phoneNumber(createDTO.getPhoneNumber())
                .email(createDTO.getEmail())
                .address(createDTO.getAddress())
                .city(createDTO.getCity())
                .country(createDTO.getCountry())
                .isActive(createDTO.getIsActive())
                .contractStartDate(createDTO.getContractStartDate())
                .contractEndDate(createDTO.getContractEndDate())
                .notes(createDTO.getNotes())
                .build();

        Supplier savedSupplier = supplierRepository.save(supplier);
        return convertToResponseDTO(savedSupplier);
    }

    @Override
    @Transactional(readOnly = true)
    public SupplierResponseDTO getSupplierById(Long id) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with id: " + id));
        return convertToResponseDTO(supplier);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SupplierResponseDTO> getAllSuppliers() {
        return supplierRepository.findAll()
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SupplierResponseDTO> getActiveSuppliers() {
        return supplierRepository.findByIsActiveTrueOrderByCompanyNameAsc()
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public SupplierResponseDTO updateSupplier(Long id, SupplierUpdateDTO updateDTO) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with id: " + id));

        if (updateDTO.getCompanyName() != null) {
            supplier.setCompanyName(updateDTO.getCompanyName());
        }
        if (updateDTO.getTaxOffice() != null) {
            supplier.setTaxOffice(updateDTO.getTaxOffice());
        }
        if (updateDTO.getTradeRegistryNumber() != null) {
            supplier.setTradeRegistryNumber(updateDTO.getTradeRegistryNumber());
        }
        if (updateDTO.getPhoneNumber() != null) {
            supplier.setPhoneNumber(updateDTO.getPhoneNumber());
        }
        if (updateDTO.getEmail() != null) {
            supplier.setEmail(updateDTO.getEmail());
        }
        if (updateDTO.getAddress() != null) {
            supplier.setAddress(updateDTO.getAddress());
        }
        if (updateDTO.getCity() != null) {
            supplier.setCity(updateDTO.getCity());
        }
        if (updateDTO.getCountry() != null) {
            supplier.setCountry(updateDTO.getCountry());
        }
        if (updateDTO.getIsActive() != null) {
            supplier.setIsActive(updateDTO.getIsActive());
        }
        if (updateDTO.getContractStartDate() != null) {
            supplier.setContractStartDate(updateDTO.getContractStartDate());
        }
        if (updateDTO.getContractEndDate() != null) {
            supplier.setContractEndDate(updateDTO.getContractEndDate());
        }
        if (updateDTO.getNotes() != null) {
            supplier.setNotes(updateDTO.getNotes());
        }

        Supplier updatedSupplier = supplierRepository.save(supplier);
        return convertToResponseDTO(updatedSupplier);
    }

    @Override
    public void deleteSupplier(Long id) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with id: " + id));
        
        // Soft delete - sadece isActive false yap
        supplier.setIsActive(false);
        supplierRepository.save(supplier);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SupplierResponseDTO> searchSuppliersByCompanyName(String companyName) {
        return supplierRepository.findByCompanyNameContainingIgnoreCaseAndIsActiveTrue(companyName)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SupplierResponseDTO> getSuppliersByCity(String city) {
        return supplierRepository.findByCityContainingIgnoreCaseAndIsActiveTrue(city)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SupplierResponseDTO> getSuppliersByCountry(String country) {
        return supplierRepository.findByCountryContainingIgnoreCaseAndIsActiveTrue(country)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SupplierResponseDTO> getSuppliersWithActiveVehicles() {
        return supplierRepository.findSuppliersWithActiveVehicles()
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SupplierResponseDTO> getSuppliersWithActiveTrailers() {
        return supplierRepository.findSuppliersWithActiveTrailers()
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object[]> getSupplierStatistics() {
        return supplierRepository.getSupplierStatistics();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isValidTaxNumber(String taxNumber, Long excludeId) {
        if (excludeId != null) {
            return supplierRepository.findByTaxNumberAndNotId(taxNumber, excludeId).isEmpty();
        } else {
            return supplierRepository.findByTaxNumber(taxNumber).isEmpty();
        }
    }

    private SupplierResponseDTO convertToResponseDTO(Supplier supplier) {
        SupplierResponseDTO dto = new SupplierResponseDTO();
        dto.setId(supplier.getId());
        dto.setCompanyName(supplier.getCompanyName());
        dto.setTaxNumber(supplier.getTaxNumber());
        dto.setTaxOffice(supplier.getTaxOffice());
        dto.setTradeRegistryNumber(supplier.getTradeRegistryNumber());
        dto.setPhoneNumber(supplier.getPhoneNumber());
        dto.setEmail(supplier.getEmail());
        dto.setAddress(supplier.getAddress());
        dto.setCity(supplier.getCity());
        dto.setCountry(supplier.getCountry());
        dto.setIsActive(supplier.getIsActive());
        dto.setContractStartDate(supplier.getContractStartDate());
        dto.setContractEndDate(supplier.getContractEndDate());
        dto.setNotes(supplier.getNotes());
        dto.setCreatedAt(supplier.getCreatedAt());
        dto.setUpdatedAt(supplier.getUpdatedAt());
        
        // Aktif araç ve trailer sayılarını hesapla
        if (supplier.getVehicles() != null) {
            dto.setActiveVehicleCount((int) supplier.getVehicles().stream()
                    .filter(v -> v.getIsActive() != null && v.getIsActive())
                    .count());
        }
        if (supplier.getTrailers() != null) {
            dto.setActiveTrailerCount((int) supplier.getTrailers().stream()
                    .filter(t -> t.getIsActive() != null && t.getIsActive())
                    .count());
        }
        
        return dto;
    }
}

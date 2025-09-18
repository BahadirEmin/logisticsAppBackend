package com.baem.logisticapp.service;

import com.baem.logisticapp.dto.SupplierCreateDTO;
import com.baem.logisticapp.dto.SupplierResponseDTO;
import com.baem.logisticapp.dto.SupplierUpdateDTO;

import java.util.List;

public interface SupplierService {

    SupplierResponseDTO createSupplier(SupplierCreateDTO createDTO);

    SupplierResponseDTO getSupplierById(Long id);

    List<SupplierResponseDTO> getAllSuppliers();

    List<SupplierResponseDTO> getActiveSuppliers();

    SupplierResponseDTO updateSupplier(Long id, SupplierUpdateDTO updateDTO);

    void deleteSupplier(Long id);

    List<SupplierResponseDTO> searchSuppliersByCompanyName(String companyName);

    List<SupplierResponseDTO> getSuppliersByCity(String city);

    List<SupplierResponseDTO> getSuppliersByCountry(String country);

    List<SupplierResponseDTO> getSuppliersWithActiveVehicles();

    List<SupplierResponseDTO> getSuppliersWithActiveTrailers();

    // Tedarikçi istatistikleri
    List<Object[]> getSupplierStatistics();

    // Vergi numarası benzersizlik kontrolü
    boolean isValidTaxNumber(String taxNumber, Long excludeId);
}

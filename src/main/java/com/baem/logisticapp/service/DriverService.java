package com.baem.logisticapp.service;

import com.baem.logisticapp.dto.DriverCreateDTO;
import com.baem.logisticapp.dto.DriverResponseDTO;
import com.baem.logisticapp.dto.DriverUpdateDTO;

import java.time.LocalDate;
import java.util.List;

public interface DriverService {
    DriverResponseDTO createDriver(DriverCreateDTO createDTO);
    DriverResponseDTO getDriverById(Long id);
    List<DriverResponseDTO> getAllDrivers();
    DriverResponseDTO updateDriver(Long id, DriverUpdateDTO updateDTO);
    void deleteDriver(Long id);
    List<DriverResponseDTO> searchDrivers(String licenseNo, Boolean active);

}

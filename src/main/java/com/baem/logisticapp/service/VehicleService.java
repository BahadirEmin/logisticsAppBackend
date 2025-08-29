package com.baem.logisticapp.service;

import com.baem.logisticapp.dto.VehicleCreateDTO;
import com.baem.logisticapp.dto.VehicleResponseDTO;
import com.baem.logisticapp.dto.VehicleUpdateDTO;

import java.time.LocalDate;
import java.util.List;


public interface VehicleService {

     VehicleResponseDTO createVehicle(VehicleCreateDTO createDTO);
     VehicleResponseDTO getVehicleById(Long id);
     VehicleResponseDTO updateVehicle(Long id, VehicleUpdateDTO updateDTO);
     void deleteVehicle(Long id);
     VehicleResponseDTO getVehicleByPlateNo(String plateNo);
     VehicleResponseDTO getVehicleByVin(String vin);
     List<VehicleResponseDTO> getActiveVehicles();
     List<VehicleResponseDTO> getAllVehicles();
     List<VehicleResponseDTO> searchVehicles(String plateNo, String vin, String make, String model, Short modelYear, Long ownershipTypeId, Boolean active, LocalDate purchasedAfter);
}
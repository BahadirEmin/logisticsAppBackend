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
     List<VehicleResponseDTO> getVehiclesByMakeAndModel(String make, String model);
     List<VehicleResponseDTO> getActiveVehicles();
     List<VehicleResponseDTO> getVehiclesByOwnershipType(Long ownershipTypeId);
     List<VehicleResponseDTO> getVehiclesByModelYear(Short modelYear);
     List<VehicleResponseDTO> getVehiclesPurchasedAfter(java.time.LocalDate date);
     List<VehicleResponseDTO> getVehiclesByMake(String make);
     List<VehicleResponseDTO> getVehiclesByModel(String model);
     List<VehicleResponseDTO> getActiveVehiclesByOwnershipType(Long ownershipTypeId);
     List<VehicleResponseDTO> getActiveVehiclesByMake(String make);
     List<VehicleResponseDTO> getActiveVehiclesByModel(String model);
     List<VehicleResponseDTO> getActiveVehiclesByModelYear(Short modelYear);
     List<VehicleResponseDTO> getActiveVehiclesPurchasedAfter(LocalDate date);
     List<VehicleResponseDTO> getVehiclesByMakeAndModelYear(String make, Short modelYear);
     List<VehicleResponseDTO> getVehiclesByModelAndModelYear(String model, Short modelYear);
     List<VehicleResponseDTO> getAllVehicles();
}
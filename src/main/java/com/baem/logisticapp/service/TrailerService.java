package com.baem.logisticapp.service;

import com.baem.logisticapp.dto.TrailerCreateDTO;
import com.baem.logisticapp.dto.TrailerResponseDTO;
import com.baem.logisticapp.dto.TrailerUpdateDTO;

import java.util.List;

public interface TrailerService {

     TrailerResponseDTO createTrailer(TrailerCreateDTO createDTO);
     TrailerResponseDTO getTrailerById(Long id);
     List<TrailerResponseDTO> getAllTrailers();
     TrailerResponseDTO updateTrailer(Long id, TrailerUpdateDTO updateDTO);
     void deleteTrailer(Long id);
     TrailerResponseDTO getTrailerByTrailerNo(String trailerNo);
     TrailerResponseDTO getTrailerByVin(String vin);
     List<TrailerResponseDTO> getTrailersByMakeAndModel(String make, String model);
     List<TrailerResponseDTO> getTrailersByType(String trailerType);
     List<TrailerResponseDTO> getActiveTrailers();
     List<TrailerResponseDTO> getTrailersByOwnershipType(Long ownershipTypeId);
     List<TrailerResponseDTO> getTrailersByModelYear(Short modelYear);
     List<TrailerResponseDTO> getTrailersByCapacityGreaterThan(Double capacity);
     List<TrailerResponseDTO> getTrailersByCapacityLessThan(Double capacity);
     List<TrailerResponseDTO> getTrailersByCapacityRange(Double minCapacity, Double maxCapacity);
     List<TrailerResponseDTO> getTrailersPurchasedAfter(java.time.LocalDate date);
     List<TrailerResponseDTO> getTrailersByMake(String make);
     List<TrailerResponseDTO> getTrailersByModel(String model);
     List<TrailerResponseDTO> getActiveTrailersByOwnershipType(Long ownershipTypeId);
     List<TrailerResponseDTO> getActiveTrailersByType(String trailerType);
     List<TrailerResponseDTO> getActiveTrailersByCapacityGreaterThan(Double capacity);

}
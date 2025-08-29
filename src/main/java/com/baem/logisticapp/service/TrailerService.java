package com.baem.logisticapp.service;

import com.baem.logisticapp.dto.TrailerCreateDTO;
import com.baem.logisticapp.dto.TrailerResponseDTO;
import com.baem.logisticapp.dto.TrailerUpdateDTO;

import java.time.LocalDate;
import java.util.List;

public interface TrailerService {

     TrailerResponseDTO createTrailer(TrailerCreateDTO createDTO);
     TrailerResponseDTO getTrailerById(Long id);
     List<TrailerResponseDTO> getAllTrailers();
     TrailerResponseDTO updateTrailer(Long id, TrailerUpdateDTO updateDTO);
     void deleteTrailer(Long id);
     List<TrailerResponseDTO> searchTrailers(String trailerNo, String vin, String trailerType, Long ownershipTypeId, Double minCapacity, Double maxCapacity, Boolean active, LocalDate purchasedAfter);

}
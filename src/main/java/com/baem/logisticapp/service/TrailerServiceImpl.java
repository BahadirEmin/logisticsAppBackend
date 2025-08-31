package com.baem.logisticapp.service;

import com.baem.logisticapp.dto.TrailerCreateDTO;
import com.baem.logisticapp.dto.TrailerResponseDTO;
import com.baem.logisticapp.dto.TrailerUpdateDTO;
import com.baem.logisticapp.entity.Trailer;
import com.baem.logisticapp.entity.VehicleOwnershipType;
import com.baem.logisticapp.exception.ResourceNotFoundException;
import com.baem.logisticapp.repository.TrailerRepository;
import com.baem.logisticapp.repository.VehicleOwnershipTypeRepository;
import com.baem.logisticapp.validator.TrailerValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TrailerServiceImpl implements TrailerService {

    private final TrailerRepository trailerRepository;
    private final VehicleOwnershipTypeRepository vehicleOwnershipTypeRepository;
    private final TrailerValidator trailerValidator;

    public TrailerResponseDTO createTrailer(TrailerCreateDTO createDTO) {
        trailerValidator.validateForCreate(createDTO);

        // Get ownership type
        VehicleOwnershipType ownershipType = vehicleOwnershipTypeRepository.findById(createDTO.getOwnershipTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("Ownership type not found"));

        Trailer trailer = Trailer.builder()
                .trailerNo(createDTO.getTrailerNo())
                .make(createDTO.getMake())
                .model(createDTO.getModel())
                .modelYear(createDTO.getModelYear())
                .ownershipType(ownershipType)
                .purchaseDate(createDTO.getPurchaseDate())
                .vin(createDTO.getVin())
                .trailerType(createDTO.getTrailerType())
                .capacity(createDTO.getCapacity())
                .length(createDTO.getLength())
                .width(createDTO.getWidth())
                .height(createDTO.getHeight())
                .isActive(true)
                .build();

        return convertToDTO(trailerRepository.save(trailer));
    }

    public TrailerResponseDTO getTrailerById(Long id) {
        return trailerRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Trailer not found"));
    }

    public List<TrailerResponseDTO> getAllTrailers() {
        return trailerRepository.findAll().stream()
                .map(this::convertToDTO)
                .toList();
    }

    public TrailerResponseDTO updateTrailer(Long id, TrailerUpdateDTO updateDTO) {
        Trailer trailer = trailerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Trailer not found"));

        trailerValidator.validateForUpdate(updateDTO, trailer.getTrailerNo());

        // Get ownership type
        VehicleOwnershipType ownershipType = vehicleOwnershipTypeRepository.findById(updateDTO.getOwnershipTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("Ownership type not found"));

        trailer.setTrailerNo(updateDTO.getTrailerNo());
        trailer.setMake(updateDTO.getMake());
        trailer.setModel(updateDTO.getModel());
        trailer.setModelYear(updateDTO.getModelYear());
        trailer.setOwnershipType(ownershipType);
        trailer.setPurchaseDate(updateDTO.getPurchaseDate());
        trailer.setVin(updateDTO.getVin());
        trailer.setTrailerType(updateDTO.getTrailerType());
        trailer.setCapacity(updateDTO.getCapacity());
        trailer.setLength(updateDTO.getLength());
        trailer.setWidth(updateDTO.getWidth());
        trailer.setHeight(updateDTO.getHeight());
        trailer.setIsActive(updateDTO.getIsActive() != null ? updateDTO.getIsActive() : true);

        return convertToDTO(trailerRepository.save(trailer));
    }

    public void deleteTrailer(Long id) {
        if (!trailerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Trailer not found");
        }
        trailerRepository.deleteById(id);
    }

    @Override
    public List<TrailerResponseDTO> searchTrailers(String trailerNo, String vin, String trailerType, 
                                                   Long ownershipTypeId, Double minCapacity, Double maxCapacity, 
                                                   Boolean active, LocalDate purchasedAfter) {
        return trailerRepository.findTrailersWithFilters(trailerNo, vin, trailerType, ownershipTypeId, minCapacity, maxCapacity, active, purchasedAfter).stream()
                .map(this::convertToDTO)
                .toList();
    }

    private TrailerResponseDTO convertToDTO(Trailer trailer) {
        return TrailerResponseDTO.builder()
                .id(trailer.getId())
                .trailerNo(trailer.getTrailerNo())
                .make(trailer.getMake())
                .model(trailer.getModel())
                .modelYear(trailer.getModelYear())
                .ownershipTypeId(trailer.getOwnershipType().getId())
                .ownershipTypeName(trailer.getOwnershipType().getOwnershipName())
                .purchaseDate(trailer.getPurchaseDate())
                .vin(trailer.getVin())
                .trailerType(trailer.getTrailerType())
                .capacity(trailer.getCapacity())
                .length(trailer.getLength())
                .width(trailer.getWidth())
                .height(trailer.getHeight())
                .isActive(trailer.getIsActive())
                .build();
    }
}

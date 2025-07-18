package com.company.logistics.logistics_backend.service;

import com.company.logistics.logistics_backend.domain.dto.TrailerCreateDTO;
import com.company.logistics.logistics_backend.domain.dto.TrailerResponseDTO;
import com.company.logistics.logistics_backend.domain.dto.TrailerUpdateDTO;
import com.company.logistics.logistics_backend.domain.entity.Trailer;
import com.company.logistics.logistics_backend.domain.entity.VehicleOwnershipType;
import com.company.logistics.logistics_backend.domain.repository.TrailerRepository;
import com.company.logistics.logistics_backend.domain.repository.VehicleOwnershipTypeRepository;
import com.company.logistics.logistics_backend.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TrailerService {

    private final TrailerRepository trailerRepository;
    private final VehicleOwnershipTypeRepository vehicleOwnershipTypeRepository;

    public TrailerResponseDTO createTrailer(TrailerCreateDTO createDTO) {
        // Check if trailer with same trailer number already exists
        if (trailerRepository.existsByTrailerNo(createDTO.getTrailerNo())) {
            throw new IllegalArgumentException("Trailer with number " + createDTO.getTrailerNo() + " already exists");
        }

        // Check if trailer with same VIN already exists
        if (trailerRepository.existsByVin(createDTO.getVin())) {
            throw new IllegalArgumentException("Trailer with VIN " + createDTO.getVin() + " already exists");
        }

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

        // Check if trailer number is being changed and if it already exists
        if (!trailer.getTrailerNo().equals(updateDTO.getTrailerNo()) &&
                trailerRepository.existsByTrailerNo(updateDTO.getTrailerNo())) {
            throw new IllegalArgumentException("Trailer with number " + updateDTO.getTrailerNo() + " already exists");
        }

        // Check if VIN is being changed and if it already exists
        if (!trailer.getVin().equals(updateDTO.getVin()) &&
                trailerRepository.existsByVin(updateDTO.getVin())) {
            throw new IllegalArgumentException("Trailer with VIN " + updateDTO.getVin() + " already exists");
        }

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

    public TrailerResponseDTO getTrailerByTrailerNo(String trailerNo) {
        return trailerRepository.findByTrailerNo(trailerNo)
                .map(this::convertToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Trailer not found"));
    }

    public TrailerResponseDTO getTrailerByVin(String vin) {
        return trailerRepository.findByVin(vin)
                .map(this::convertToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Trailer not found"));
    }

    public List<TrailerResponseDTO> getTrailersByMakeAndModel(String make, String model) {
        return trailerRepository.findByMakeAndModel(make, model).stream()
                .map(this::convertToDTO)
                .toList();
    }

    public List<TrailerResponseDTO> getTrailersByType(String trailerType) {
        return trailerRepository.findByTrailerType(trailerType).stream()
                .map(this::convertToDTO)
                .toList();
    }

    public List<TrailerResponseDTO> getActiveTrailers() {
        return trailerRepository.findByIsActiveTrue().stream()
                .map(this::convertToDTO)
                .toList();
    }

    public List<TrailerResponseDTO> getTrailersByOwnershipType(Long ownershipTypeId) {
        return trailerRepository.findByOwnershipTypeId(ownershipTypeId).stream()
                .map(this::convertToDTO)
                .toList();
    }

    public List<TrailerResponseDTO> getTrailersByModelYear(Short modelYear) {
        return trailerRepository.findByModelYear(modelYear).stream()
                .map(this::convertToDTO)
                .toList();
    }

    public List<TrailerResponseDTO> getTrailersByCapacityGreaterThan(Double capacity) {
        return trailerRepository.findByCapacityGreaterThan(capacity).stream()
                .map(this::convertToDTO)
                .toList();
    }

    public List<TrailerResponseDTO> getTrailersByCapacityLessThan(Double capacity) {
        return trailerRepository.findByCapacityLessThan(capacity).stream()
                .map(this::convertToDTO)
                .toList();
    }

    public List<TrailerResponseDTO> getTrailersByCapacityRange(Double minCapacity, Double maxCapacity) {
        return trailerRepository.findByCapacityBetween(minCapacity, maxCapacity).stream()
                .map(this::convertToDTO)
                .toList();
    }

    public List<TrailerResponseDTO> getTrailersPurchasedAfter(java.time.LocalDate date) {
        return trailerRepository.findByPurchaseDateAfter(date).stream()
                .map(this::convertToDTO)
                .toList();
    }

    public List<TrailerResponseDTO> getTrailersByMake(String make) {
        return trailerRepository.findByMake(make).stream()
                .map(this::convertToDTO)
                .toList();
    }

    public List<TrailerResponseDTO> getTrailersByModel(String model) {
        return trailerRepository.findByModel(model).stream()
                .map(this::convertToDTO)
                .toList();
    }

    public List<TrailerResponseDTO> getActiveTrailersByOwnershipType(Long ownershipTypeId) {
        return trailerRepository.findByOwnershipTypeIdAndIsActiveTrue(ownershipTypeId).stream()
                .map(this::convertToDTO)
                .toList();
    }

    public List<TrailerResponseDTO> getActiveTrailersByType(String trailerType) {
        return trailerRepository.findByTrailerTypeAndIsActiveTrue(trailerType).stream()
                .map(this::convertToDTO)
                .toList();
    }

    public List<TrailerResponseDTO> getActiveTrailersByCapacityGreaterThan(Double capacity) {
        return trailerRepository.findByCapacityGreaterThanAndIsActiveTrue(capacity).stream()
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
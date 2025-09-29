package com.baem.logisticapp.service;

import com.baem.logisticapp.dto.CountryCodeCreateDTO;
import com.baem.logisticapp.dto.CountryCodeResponseDTO;
import com.baem.logisticapp.dto.CountryCodeUpdateDTO;
import com.baem.logisticapp.entity.CountryCode;
import com.baem.logisticapp.exception.ResourceNotFoundException;
import com.baem.logisticapp.repository.CountryCodeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CountryCodeServiceImpl implements CountryCodeService {

    private final CountryCodeRepository countryCodeRepository;

    public CountryCodeServiceImpl(CountryCodeRepository countryCodeRepository) {
        this.countryCodeRepository = countryCodeRepository;
    }

    @Override
    public CountryCodeResponseDTO createCountryCode(CountryCodeCreateDTO createDTO) {
        // ISO kod benzersizlik kontrolü
        if (createDTO.getCountryCodeIso() != null && 
            countryCodeRepository.findByCountryCodeIsoIgnoreCaseAndIsActiveTrue(createDTO.getCountryCodeIso()).isPresent()) {
            throw new IllegalArgumentException("A country with this ISO code already exists");
        }

        // Numerik kod benzersizlik kontrolü
        if (countryCodeRepository.findByCountryCodeNumericAndIsActiveTrue(createDTO.getCountryCodeNumeric()).isPresent()) {
            throw new IllegalArgumentException("A country with this numeric code already exists");
        }

        CountryCode countryCode = CountryCode.builder()
                .countryName(createDTO.getCountryName())
                .countryNameTr(createDTO.getCountryNameTr())
                .countryCodeIso(createDTO.getCountryCodeIso())
                .countryCodeNumeric(createDTO.getCountryCodeNumeric())
                .isActive(createDTO.getIsActive())
                .build();

        CountryCode savedCountryCode = countryCodeRepository.save(countryCode);
        return convertToResponseDTO(savedCountryCode);
    }

    @Override
    @Transactional(readOnly = true)
    public CountryCodeResponseDTO getCountryCodeById(Long id) {
        CountryCode countryCode = countryCodeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Country code not found with id: " + id));
        return convertToResponseDTO(countryCode);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CountryCodeResponseDTO> getAllCountryCodes() {
        return countryCodeRepository.findAll()
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CountryCodeResponseDTO> getActiveCountryCodes() {
        return countryCodeRepository.findByIsActiveTrueOrderByCountryNameAsc()
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CountryCodeResponseDTO updateCountryCode(Long id, CountryCodeUpdateDTO updateDTO) {
        CountryCode countryCode = countryCodeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Country code not found with id: " + id));

        // ISO kod benzersizlik kontrolü (güncellenen kayıt hariç)
        if (updateDTO.getCountryCodeIso() != null && 
            !isIsoCodeUnique(updateDTO.getCountryCodeIso(), id)) {
            throw new IllegalArgumentException("A country with this ISO code already exists");
        }

        // Numerik kod benzersizlik kontrolü (güncellenen kayıt hariç)
        if (updateDTO.getCountryCodeNumeric() != null && 
            !isNumericCodeUnique(updateDTO.getCountryCodeNumeric(), id)) {
            throw new IllegalArgumentException("A country with this numeric code already exists");
        }

        // Güncellenebilir alanları kontrol et ve güncelle
        if (updateDTO.getCountryName() != null) {
            countryCode.setCountryName(updateDTO.getCountryName());
        }
        if (updateDTO.getCountryNameTr() != null) {
            countryCode.setCountryNameTr(updateDTO.getCountryNameTr());
        }
        if (updateDTO.getCountryCodeIso() != null) {
            countryCode.setCountryCodeIso(updateDTO.getCountryCodeIso());
        }
        if (updateDTO.getCountryCodeNumeric() != null) {
            countryCode.setCountryCodeNumeric(updateDTO.getCountryCodeNumeric());
        }
        if (updateDTO.getIsActive() != null) {
            countryCode.setIsActive(updateDTO.getIsActive());
        }

        CountryCode updatedCountryCode = countryCodeRepository.save(countryCode);
        return convertToResponseDTO(updatedCountryCode);
    }

    @Override
    public void deleteCountryCode(Long id) {
        CountryCode countryCode = countryCodeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Country code not found with id: " + id));
        
        // Soft delete - sadece isActive false yap
        countryCode.setIsActive(false);
        countryCodeRepository.save(countryCode);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CountryCodeResponseDTO> searchCountryCodesByName(String name) {
        return countryCodeRepository.findByCountryNameIgnoreCaseAndIsActiveTrue(name)
                .map(this::convertToResponseDTO)
                .map(List::of)
                .orElse(List.of());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CountryCodeResponseDTO> searchCountryCodesByTurkishName(String nameTr) {
        return countryCodeRepository.findByCountryNameTrIgnoreCaseAndIsActiveTrue(nameTr)
                .map(this::convertToResponseDTO)
                .map(List::of)
                .orElse(List.of());
    }

    @Override
    @Transactional(readOnly = true)
    public CountryCodeResponseDTO getCountryCodeByIsoCode(String isoCode) {
        CountryCode countryCode = countryCodeRepository.findByCountryCodeIsoIgnoreCaseAndIsActiveTrue(isoCode)
                .orElseThrow(() -> new ResourceNotFoundException("Country code not found with ISO code: " + isoCode));
        return convertToResponseDTO(countryCode);
    }

    @Override
    @Transactional(readOnly = true)
    public CountryCodeResponseDTO getCountryCodeByNumericCode(String numericCode) {
        CountryCode countryCode = countryCodeRepository.findByCountryCodeNumericAndIsActiveTrue(numericCode)
                .orElseThrow(() -> new ResourceNotFoundException("Country code not found with numeric code: " + numericCode));
        return convertToResponseDTO(countryCode);
    }

    @Override
    @Transactional(readOnly = true)
    public CountryCodeResponseDTO findByAnyCountryName(String countryName) {
        CountryCode countryCode = countryCodeRepository.findByAnyCountryName(countryName)
                .orElseThrow(() -> new ResourceNotFoundException("Country code not found with name: " + countryName));
        return convertToResponseDTO(countryCode);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isIsoCodeUnique(String isoCode, Long excludeId) {
        if (excludeId != null) {
            return countryCodeRepository.findByCountryCodeIsoIgnoreCaseAndIsActiveTrue(isoCode)
                    .map(country -> !country.getId().equals(excludeId))
                    .orElse(true);
        } else {
            return countryCodeRepository.findByCountryCodeIsoIgnoreCaseAndIsActiveTrue(isoCode).isEmpty();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isNumericCodeUnique(String numericCode, Long excludeId) {
        if (excludeId != null) {
            return countryCodeRepository.findByCountryCodeNumericAndIsActiveTrue(numericCode)
                    .map(country -> !country.getId().equals(excludeId))
                    .orElse(true);
        } else {
            return countryCodeRepository.findByCountryCodeNumericAndIsActiveTrue(numericCode).isEmpty();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isCountryNameUnique(String countryName, Long excludeId) {
        if (excludeId != null) {
            return countryCodeRepository.findByCountryNameIgnoreCaseAndIsActiveTrue(countryName)
                    .map(country -> !country.getId().equals(excludeId))
                    .orElse(true);
        } else {
            return countryCodeRepository.findByCountryNameIgnoreCaseAndIsActiveTrue(countryName).isEmpty();
        }
    }

    private CountryCodeResponseDTO convertToResponseDTO(CountryCode countryCode) {
        CountryCodeResponseDTO dto = new CountryCodeResponseDTO();
        dto.setId(countryCode.getId());
        dto.setCountryName(countryCode.getCountryName());
        dto.setCountryNameTr(countryCode.getCountryNameTr());
        dto.setCountryCodeIso(countryCode.getCountryCodeIso());
        dto.setCountryCodeNumeric(countryCode.getCountryCodeNumeric());
        dto.setIsActive(countryCode.getIsActive());
        
        return dto;
    }
}

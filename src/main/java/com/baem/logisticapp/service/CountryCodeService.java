package com.baem.logisticapp.service;

import com.baem.logisticapp.dto.CountryCodeCreateDTO;
import com.baem.logisticapp.dto.CountryCodeResponseDTO;
import com.baem.logisticapp.dto.CountryCodeUpdateDTO;

import java.util.List;

public interface CountryCodeService {

    CountryCodeResponseDTO createCountryCode(CountryCodeCreateDTO createDTO);

    CountryCodeResponseDTO getCountryCodeById(Long id);

    List<CountryCodeResponseDTO> getAllCountryCodes();

    List<CountryCodeResponseDTO> getActiveCountryCodes();

    CountryCodeResponseDTO updateCountryCode(Long id, CountryCodeUpdateDTO updateDTO);

    void deleteCountryCode(Long id);

    // Search operations
    List<CountryCodeResponseDTO> searchCountryCodesByName(String name);

    List<CountryCodeResponseDTO> searchCountryCodesByTurkishName(String nameTr);

    CountryCodeResponseDTO getCountryCodeByIsoCode(String isoCode);

    CountryCodeResponseDTO getCountryCodeByNumericCode(String numericCode);

    CountryCodeResponseDTO findByAnyCountryName(String countryName);

    // Validation methods
    boolean isIsoCodeUnique(String isoCode, Long excludeId);

    boolean isNumericCodeUnique(String numericCode, Long excludeId);

    boolean isCountryNameUnique(String countryName, Long excludeId);
}

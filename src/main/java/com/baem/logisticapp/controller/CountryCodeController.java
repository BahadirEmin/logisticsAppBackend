package com.baem.logisticapp.controller;

import com.baem.logisticapp.dto.CountryCodeCreateDTO;
import com.baem.logisticapp.dto.CountryCodeResponseDTO;
import com.baem.logisticapp.dto.CountryCodeUpdateDTO;
import com.baem.logisticapp.service.CountryCodeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/country-codes")
@RequiredArgsConstructor
public class CountryCodeController {

    private final CountryCodeService countryCodeService;

    @PostMapping
    public ResponseEntity<CountryCodeResponseDTO> createCountryCode(@Valid @RequestBody CountryCodeCreateDTO createDTO) {
        return new ResponseEntity<>(countryCodeService.createCountryCode(createDTO), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CountryCodeResponseDTO> getCountryCodeById(@PathVariable Long id) {
        return ResponseEntity.ok(countryCodeService.getCountryCodeById(id));
    }

    @GetMapping
    public ResponseEntity<List<CountryCodeResponseDTO>> getAllCountryCodes() {
        return ResponseEntity.ok(countryCodeService.getAllCountryCodes());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CountryCodeResponseDTO> updateCountryCode(
            @PathVariable Long id,
            @Valid @RequestBody CountryCodeUpdateDTO updateDTO) {
        return ResponseEntity.ok(countryCodeService.updateCountryCode(id, updateDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCountryCode(@PathVariable Long id) {
        countryCodeService.deleteCountryCode(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<CountryCodeResponseDTO>> searchCountryCodes(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String nameTr,
            @RequestParam(required = false) String isoCode,
            @RequestParam(required = false) String numericCode,
            @RequestParam(required = false) String anyName,
            @RequestParam(required = false) Boolean active) {
        
        if (anyName != null) {
            return ResponseEntity.ok(List.of(countryCodeService.findByAnyCountryName(anyName)));
        } else if (name != null) {
            return ResponseEntity.ok(countryCodeService.searchCountryCodesByName(name));
        } else if (nameTr != null) {
            return ResponseEntity.ok(countryCodeService.searchCountryCodesByTurkishName(nameTr));
        } else if (isoCode != null) {
            return ResponseEntity.ok(List.of(countryCodeService.getCountryCodeByIsoCode(isoCode)));
        } else if (numericCode != null) {
            return ResponseEntity.ok(List.of(countryCodeService.getCountryCodeByNumericCode(numericCode)));
        } else if (active != null && active) {
            return ResponseEntity.ok(countryCodeService.getActiveCountryCodes());
        } else {
            return ResponseEntity.ok(countryCodeService.getAllCountryCodes());
        }
    }

    @GetMapping("/active")
    public ResponseEntity<List<CountryCodeResponseDTO>> getActiveCountryCodes() {
        return ResponseEntity.ok(countryCodeService.getActiveCountryCodes());
    }

    @GetMapping("/iso/{isoCode}")
    public ResponseEntity<CountryCodeResponseDTO> getCountryCodeByIsoCode(@PathVariable String isoCode) {
        return ResponseEntity.ok(countryCodeService.getCountryCodeByIsoCode(isoCode));
    }

    @GetMapping("/numeric/{numericCode}")
    public ResponseEntity<CountryCodeResponseDTO> getCountryCodeByNumericCode(@PathVariable String numericCode) {
        return ResponseEntity.ok(countryCodeService.getCountryCodeByNumericCode(numericCode));
    }

    @GetMapping("/find/{countryName}")
    public ResponseEntity<CountryCodeResponseDTO> findByAnyCountryName(@PathVariable String countryName) {
        return ResponseEntity.ok(countryCodeService.findByAnyCountryName(countryName));
    }
}

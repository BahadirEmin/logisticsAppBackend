package com.baem.logisticapp.controller;

import com.baem.logisticapp.dto.SupplierCreateDTO;
import com.baem.logisticapp.dto.SupplierResponseDTO;
import com.baem.logisticapp.dto.SupplierUpdateDTO;
import com.baem.logisticapp.service.SupplierService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/suppliers")
@RequiredArgsConstructor
public class SupplierController {

    private final SupplierService supplierService;

    @PostMapping
    public ResponseEntity<SupplierResponseDTO> createSupplier(@Valid @RequestBody SupplierCreateDTO createDTO) {
        return new ResponseEntity<>(supplierService.createSupplier(createDTO), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SupplierResponseDTO> getSupplierById(@PathVariable Long id) {
        return ResponseEntity.ok(supplierService.getSupplierById(id));
    }

    @GetMapping
    public ResponseEntity<List<SupplierResponseDTO>> getAllSuppliers() {
        return ResponseEntity.ok(supplierService.getAllSuppliers());
    }

    @PutMapping("/{id}")
    public ResponseEntity<SupplierResponseDTO> updateSupplier(
            @PathVariable Long id,
            @Valid @RequestBody SupplierUpdateDTO updateDTO) {
        return ResponseEntity.ok(supplierService.updateSupplier(id, updateDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSupplier(@PathVariable Long id) {
        supplierService.deleteSupplier(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<SupplierResponseDTO>> searchSuppliers(
            @RequestParam(required = false) String companyName,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) Boolean active) {
        if (companyName != null) {
            return ResponseEntity.ok(supplierService.searchSuppliersByCompanyName(companyName));
        } else if (city != null) {
            return ResponseEntity.ok(supplierService.getSuppliersByCity(city));
        } else if (country != null) {
            return ResponseEntity.ok(supplierService.getSuppliersByCountry(country));
        } else if (active != null && active) {
            return ResponseEntity.ok(supplierService.getActiveSuppliers());
        } else {
            return ResponseEntity.ok(supplierService.getAllSuppliers());
        }
    }
}

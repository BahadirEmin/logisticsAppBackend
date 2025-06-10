package com.company.logistics.logistics_backend.controller;

import com.company.logistics.logistics_backend.domain.dto.DriverCreateDTO;
import com.company.logistics.logistics_backend.domain.dto.DriverResponseDTO;
import com.company.logistics.logistics_backend.domain.dto.DriverUpdateDTO;
import com.company.logistics.logistics_backend.service.DriverService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/drivers")
@RequiredArgsConstructor
public class DriverController {

    private final DriverService driverService;

    @PostMapping
    public ResponseEntity<DriverResponseDTO> createDriver(@Valid @RequestBody DriverCreateDTO driverCreateDTO) {
        return new ResponseEntity<>(driverService.createDriver(driverCreateDTO), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DriverResponseDTO> getDriverById(@PathVariable Long id) {
        return ResponseEntity.ok(driverService.getDriverById(id));
    }

    @GetMapping
    public ResponseEntity<List<DriverResponseDTO>> getAllDrivers() {
        return ResponseEntity.ok(driverService.getAllDrivers());
    }

    @PutMapping("/{id}")
    public ResponseEntity<DriverResponseDTO> updateDriver(
            @PathVariable Long id,
            @Valid @RequestBody DriverUpdateDTO driverUpdateDTO) {
        return ResponseEntity.ok(driverService.updateDriver(id, driverUpdateDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDriver(@PathVariable Long id) {
        driverService.deleteDriver(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/active")
    public ResponseEntity<List<DriverResponseDTO>> getActiveDrivers() {
        return ResponseEntity.ok(driverService.getActiveDrivers());
    }

    @GetMapping("/license/{licenseNo}")
    public ResponseEntity<DriverResponseDTO> getDriverByLicenseNo(@PathVariable String licenseNo) {
        return ResponseEntity.ok(driverService.getDriverByLicenseNo(licenseNo));
    }
}
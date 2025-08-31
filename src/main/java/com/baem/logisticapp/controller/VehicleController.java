package com.baem.logisticapp.controller;

import com.baem.logisticapp.dto.VehicleCreateDTO;
import com.baem.logisticapp.dto.VehicleResponseDTO;
import com.baem.logisticapp.dto.VehicleUpdateDTO;
import com.baem.logisticapp.service.VehicleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/vehicles")
@RequiredArgsConstructor
@Tag(name = "05 - Vehicles", description = "Vehicle Management")
public class VehicleController {

    private final VehicleService vehicleService;

    @PostMapping
    public ResponseEntity<VehicleResponseDTO> createVehicle(@Valid @RequestBody VehicleCreateDTO vehicleCreateDTO) {
        return new ResponseEntity<>(vehicleService.createVehicle(vehicleCreateDTO), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VehicleResponseDTO> getVehicleById(@PathVariable Long id) {
        return ResponseEntity.ok(vehicleService.getVehicleById(id));
    }

    @GetMapping
    public ResponseEntity<List<VehicleResponseDTO>> getAllVehicles() {
        return ResponseEntity.ok(vehicleService.getAllVehicles());
    }

    @PutMapping("/{id}")
    public ResponseEntity<VehicleResponseDTO> updateVehicle(
            @PathVariable Long id,
            @Valid @RequestBody VehicleUpdateDTO vehicleUpdateDTO) {
        return ResponseEntity.ok(vehicleService.updateVehicle(id, vehicleUpdateDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehicle(@PathVariable Long id) {
        vehicleService.deleteVehicle(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<VehicleResponseDTO>> searchVehicles(
            @RequestParam(required = false) String plateNo,
            @RequestParam(required = false) String vin,
            @RequestParam(required = false) String make,
            @RequestParam(required = false) String model,
            @RequestParam(required = false) Short modelYear,
            @RequestParam(required = false) Long ownershipTypeId,
            @RequestParam(required = false) Boolean active,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate purchasedAfter) {
        return ResponseEntity.ok(vehicleService.searchVehicles(plateNo, vin, make, model, modelYear, ownershipTypeId, active, purchasedAfter));
    }
}
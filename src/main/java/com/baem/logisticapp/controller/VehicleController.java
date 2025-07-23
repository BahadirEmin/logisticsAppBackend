package com.baem.logisticapp.controller;

import com.baem.logisticapp.dto.VehicleCreateDTO;
import com.baem.logisticapp.dto.VehicleResponseDTO;
import com.baem.logisticapp.dto.VehicleUpdateDTO;
import com.baem.logisticapp.service.VehicleService;
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

    @GetMapping("/active")
    public ResponseEntity<List<VehicleResponseDTO>> getActiveVehicles() {
        return ResponseEntity.ok(vehicleService.getActiveVehicles());
    }

    @GetMapping("/plate/{plateNo}")
    public ResponseEntity<VehicleResponseDTO> getVehicleByPlateNo(@PathVariable String plateNo) {
        return ResponseEntity.ok(vehicleService.getVehicleByPlateNo(plateNo));
    }

    @GetMapping("/vin/{vin}")
    public ResponseEntity<VehicleResponseDTO> getVehicleByVin(@PathVariable String vin) {
        return ResponseEntity.ok(vehicleService.getVehicleByVin(vin));
    }

    @GetMapping("/make/{make}/model/{model}")
    public ResponseEntity<List<VehicleResponseDTO>> getVehiclesByMakeAndModel(
            @PathVariable String make,
            @PathVariable String model) {
        return ResponseEntity.ok(vehicleService.getVehiclesByMakeAndModel(make, model));
    }

    @GetMapping("/ownership-type/{ownershipTypeId}")
    public ResponseEntity<List<VehicleResponseDTO>> getVehiclesByOwnershipType(@PathVariable Long ownershipTypeId) {
        return ResponseEntity.ok(vehicleService.getVehiclesByOwnershipType(ownershipTypeId));
    }

    @GetMapping("/model-year/{modelYear}")
    public ResponseEntity<List<VehicleResponseDTO>> getVehiclesByModelYear(@PathVariable Short modelYear) {
        return ResponseEntity.ok(vehicleService.getVehiclesByModelYear(modelYear));
    }

    @GetMapping("/purchased-after")
    public ResponseEntity<List<VehicleResponseDTO>> getVehiclesPurchasedAfter(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(vehicleService.getVehiclesPurchasedAfter(date));
    }

    @GetMapping("/make/{make}")
    public ResponseEntity<List<VehicleResponseDTO>> getVehiclesByMake(@PathVariable String make) {
        return ResponseEntity.ok(vehicleService.getVehiclesByMake(make));
    }

    @GetMapping("/model/{model}")
    public ResponseEntity<List<VehicleResponseDTO>> getVehiclesByModel(@PathVariable String model) {
        return ResponseEntity.ok(vehicleService.getVehiclesByModel(model));
    }

    @GetMapping("/active/ownership-type/{ownershipTypeId}")
    public ResponseEntity<List<VehicleResponseDTO>> getActiveVehiclesByOwnershipType(
            @PathVariable Long ownershipTypeId) {
        return ResponseEntity.ok(vehicleService.getActiveVehiclesByOwnershipType(ownershipTypeId));
    }

    @GetMapping("/active/make/{make}")
    public ResponseEntity<List<VehicleResponseDTO>> getActiveVehiclesByMake(@PathVariable String make) {
        return ResponseEntity.ok(vehicleService.getActiveVehiclesByMake(make));
    }

    @GetMapping("/active/model/{model}")
    public ResponseEntity<List<VehicleResponseDTO>> getActiveVehiclesByModel(@PathVariable String model) {
        return ResponseEntity.ok(vehicleService.getActiveVehiclesByModel(model));
    }

    @GetMapping("/active/model-year/{modelYear}")
    public ResponseEntity<List<VehicleResponseDTO>> getActiveVehiclesByModelYear(@PathVariable Short modelYear) {
        return ResponseEntity.ok(vehicleService.getActiveVehiclesByModelYear(modelYear));
    }

    @GetMapping("/active/purchased-after")
    public ResponseEntity<List<VehicleResponseDTO>> getActiveVehiclesPurchasedAfter(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(vehicleService.getActiveVehiclesPurchasedAfter(date));
    }

    @GetMapping("/make/{make}/model-year/{modelYear}")
    public ResponseEntity<List<VehicleResponseDTO>> getVehiclesByMakeAndModelYear(
            @PathVariable String make,
            @PathVariable Short modelYear) {
        return ResponseEntity.ok(vehicleService.getVehiclesByMakeAndModelYear(make, modelYear));
    }

    @GetMapping("/model/{model}/model-year/{modelYear}")
    public ResponseEntity<List<VehicleResponseDTO>> getVehiclesByModelAndModelYear(
            @PathVariable String model,
            @PathVariable Short modelYear) {
        return ResponseEntity.ok(vehicleService.getVehiclesByModelAndModelYear(model, modelYear));
    }
}
package com.company.logistics.logistics_backend.controller;

import com.company.logistics.logistics_backend.domain.dto.TrailerCreateDTO;
import com.company.logistics.logistics_backend.domain.dto.TrailerResponseDTO;
import com.company.logistics.logistics_backend.domain.dto.TrailerUpdateDTO;
import com.company.logistics.logistics_backend.service.TrailerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/trailers")
@RequiredArgsConstructor
public class TrailerController {

    private final TrailerService trailerService;

    @PostMapping
    public ResponseEntity<TrailerResponseDTO> createTrailer(@Valid @RequestBody TrailerCreateDTO trailerCreateDTO) {
        return new ResponseEntity<>(trailerService.createTrailer(trailerCreateDTO), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TrailerResponseDTO> getTrailerById(@PathVariable Long id) {
        return ResponseEntity.ok(trailerService.getTrailerById(id));
    }

    @GetMapping
    public ResponseEntity<List<TrailerResponseDTO>> getAllTrailers() {
        return ResponseEntity.ok(trailerService.getAllTrailers());
    }

    @PutMapping("/{id}")
    public ResponseEntity<TrailerResponseDTO> updateTrailer(
            @PathVariable Long id,
            @Valid @RequestBody TrailerUpdateDTO trailerUpdateDTO) {
        return ResponseEntity.ok(trailerService.updateTrailer(id, trailerUpdateDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrailer(@PathVariable Long id) {
        trailerService.deleteTrailer(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/active")
    public ResponseEntity<List<TrailerResponseDTO>> getActiveTrailers() {
        return ResponseEntity.ok(trailerService.getActiveTrailers());
    }

    @GetMapping("/trailer-no/{trailerNo}")
    public ResponseEntity<TrailerResponseDTO> getTrailerByTrailerNo(@PathVariable String trailerNo) {
        return ResponseEntity.ok(trailerService.getTrailerByTrailerNo(trailerNo));
    }

    @GetMapping("/vin/{vin}")
    public ResponseEntity<TrailerResponseDTO> getTrailerByVin(@PathVariable String vin) {
        return ResponseEntity.ok(trailerService.getTrailerByVin(vin));
    }

    @GetMapping("/type/{trailerType}")
    public ResponseEntity<List<TrailerResponseDTO>> getTrailersByType(@PathVariable String trailerType) {
        return ResponseEntity.ok(trailerService.getTrailersByType(trailerType));
    }

    @GetMapping("/ownership-type/{ownershipTypeId}")
    public ResponseEntity<List<TrailerResponseDTO>> getTrailersByOwnershipType(@PathVariable Long ownershipTypeId) {
        return ResponseEntity.ok(trailerService.getTrailersByOwnershipType(ownershipTypeId));
    }

    @GetMapping("/capacity/greater-than")
    public ResponseEntity<List<TrailerResponseDTO>> getTrailersByCapacityGreaterThan(@RequestParam Double capacity) {
        return ResponseEntity.ok(trailerService.getTrailersByCapacityGreaterThan(capacity));
    }

    @GetMapping("/capacity/range")
    public ResponseEntity<List<TrailerResponseDTO>> getTrailersByCapacityRange(
            @RequestParam Double minCapacity,
            @RequestParam Double maxCapacity) {
        return ResponseEntity.ok(trailerService.getTrailersByCapacityRange(minCapacity, maxCapacity));
    }

    @GetMapping("/purchased-after")
    public ResponseEntity<List<TrailerResponseDTO>> getTrailersPurchasedAfter(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(trailerService.getTrailersPurchasedAfter(date));
    }
}
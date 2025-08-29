package com.baem.logisticapp.controller;

import com.baem.logisticapp.dto.TrailerCreateDTO;
import com.baem.logisticapp.dto.TrailerResponseDTO;
import com.baem.logisticapp.dto.TrailerUpdateDTO;
import com.baem.logisticapp.service.TrailerService;
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

    @GetMapping("/search")
    public ResponseEntity<List<TrailerResponseDTO>> searchTrailers(
            @RequestParam(required = false) String trailerNo,
            @RequestParam(required = false) String vin,
            @RequestParam(required = false) String trailerType,
            @RequestParam(required = false) Long ownershipTypeId,
            @RequestParam(required = false) Double minCapacity,
            @RequestParam(required = false) Double maxCapacity,
            @RequestParam(required = false) Boolean active,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate purchasedAfter) {
        return ResponseEntity.ok(trailerService.searchTrailers(trailerNo, vin, trailerType, ownershipTypeId, minCapacity, maxCapacity, active, purchasedAfter));
    }
}
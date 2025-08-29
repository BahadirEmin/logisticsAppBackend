package com.baem.logisticapp.controller;

import com.baem.logisticapp.dto.CustomerRiskStatusCreateDTO;
import com.baem.logisticapp.dto.CustomerRiskStatusResponseDTO;
import com.baem.logisticapp.dto.CustomerRiskStatusUpdateDTO;
import com.baem.logisticapp.service.CustomerRiskStatusService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/risk-statuses")
@RequiredArgsConstructor
public class CustomerRiskStatusController {

    private final CustomerRiskStatusService customerRiskStatusService;

    @PostMapping
    public ResponseEntity<CustomerRiskStatusResponseDTO> createRiskStatus(
            @Valid @RequestBody CustomerRiskStatusCreateDTO createDTO) {
        return new ResponseEntity<>(customerRiskStatusService.createRiskStatus(createDTO), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerRiskStatusResponseDTO> getRiskStatusById(@PathVariable Long id) {
        return ResponseEntity.ok(customerRiskStatusService.getRiskStatusById(id));
    }

    @GetMapping
    public ResponseEntity<List<CustomerRiskStatusResponseDTO>> getAllRiskStatuses() {
        return ResponseEntity.ok(customerRiskStatusService.getAllRiskStatuses());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerRiskStatusResponseDTO> updateRiskStatus(
            @PathVariable Long id,
            @Valid @RequestBody CustomerRiskStatusUpdateDTO updateDTO) {
        return ResponseEntity.ok(customerRiskStatusService.updateRiskStatus(id, updateDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRiskStatus(@PathVariable Long id) {
        customerRiskStatusService.deleteRiskStatus(id);
        return ResponseEntity.noContent().build();
    }
}
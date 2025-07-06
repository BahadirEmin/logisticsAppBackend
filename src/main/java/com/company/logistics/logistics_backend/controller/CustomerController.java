package com.company.logistics.logistics_backend.controller;

import com.company.logistics.logistics_backend.domain.dto.CustomerCreateDTO;
import com.company.logistics.logistics_backend.domain.dto.CustomerResponseDTO;
import com.company.logistics.logistics_backend.domain.dto.CustomerUpdateDTO;
import com.company.logistics.logistics_backend.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<CustomerResponseDTO> createCustomer(@Valid @RequestBody CustomerCreateDTO customerCreateDTO) {
        return new ResponseEntity<>(customerService.createCustomer(customerCreateDTO), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> getCustomerById(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.getCustomerById(id));
    }

    @GetMapping
    public ResponseEntity<List<CustomerResponseDTO>> getAllCustomers() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> updateCustomer(
            @PathVariable Long id,
            @Valid @RequestBody CustomerUpdateDTO customerUpdateDTO) {
        return ResponseEntity.ok(customerService.updateCustomer(id, customerUpdateDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/risk-status/{riskStatusId}")
    public ResponseEntity<List<CustomerResponseDTO>> getCustomersByRiskStatus(@PathVariable Long riskStatusId) {
        return ResponseEntity.ok(customerService.getCustomersByRiskStatus(riskStatusId));
    }

    @GetMapping("/blacklisted")
    public ResponseEntity<List<CustomerResponseDTO>> getBlacklistedCustomers() {
        return ResponseEntity.ok(customerService.getBlacklistedCustomers());
    }

    @GetMapping("/in-lawsuit")
    public ResponseEntity<List<CustomerResponseDTO>> getCustomersInLawsuit() {
        return ResponseEntity.ok(customerService.getCustomersInLawsuit());
    }

    @GetMapping("/tax/{taxNo}")
    public ResponseEntity<CustomerResponseDTO> getCustomerByTaxNo(@PathVariable String taxNo) {
        return ResponseEntity.ok(customerService.getCustomerByTaxNo(taxNo));
    }

    @GetMapping("/search")
    public ResponseEntity<List<CustomerResponseDTO>> searchCustomersByName(@RequestParam String name) {
        return ResponseEntity.ok(customerService.searchCustomersByName(name));
    }

    @GetMapping("/risk-status/{riskStatusId}/blacklisted")
    public ResponseEntity<List<CustomerResponseDTO>> getBlacklistedCustomersByRiskStatus(
            @PathVariable Long riskStatusId) {
        return ResponseEntity.ok(customerService.getBlacklistedCustomersByRiskStatus(riskStatusId));
    }

    @GetMapping("/risk-status/{riskStatusId}/in-lawsuit")
    public ResponseEntity<List<CustomerResponseDTO>> getCustomersInLawsuitByRiskStatus(
            @PathVariable Long riskStatusId) {
        return ResponseEntity.ok(customerService.getCustomersInLawsuitByRiskStatus(riskStatusId));
    }
}
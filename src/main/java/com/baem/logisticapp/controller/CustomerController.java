package com.baem.logisticapp.controller;

import com.baem.logisticapp.dto.CustomerCreateDTO;
import com.baem.logisticapp.dto.CustomerResponseDTO;
import com.baem.logisticapp.dto.CustomerUpdateDTO;
import com.baem.logisticapp.service.CustomerService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
@Tag(name = "02 - Customers", description = "Customer Management")
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

    @GetMapping("/search")
    public ResponseEntity<List<CustomerResponseDTO>> searchCustomers(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long riskStatusId,
            @RequestParam(required = false) Boolean blacklisted,
            @RequestParam(required = false) Boolean inLawsuit,
            @RequestParam(required = false) String taxNo) {
        return ResponseEntity.ok(customerService.searchCustomers(name, riskStatusId, blacklisted, inLawsuit, taxNo));
    }
}
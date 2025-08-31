package com.baem.logisticapp.validator;

import com.baem.logisticapp.dto.CustomerCreateDTO;
import com.baem.logisticapp.dto.CustomerUpdateDTO;
import com.baem.logisticapp.repository.CustomerRepository;
import org.springframework.stereotype.Component;

@Component
public class CustomerValidator {
    
    private final CustomerRepository customerRepository;
    
    public CustomerValidator(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }
    
    public void validateForCreate(CustomerCreateDTO createDTO) {
        // Tax number validation
        if (createDTO.getTaxNo() != null && !createDTO.getTaxNo().trim().isEmpty()) {
            if (customerRepository.findByTaxNo(createDTO.getTaxNo()).isPresent()) {
                throw new IllegalArgumentException("Tax number is already in use");
            }
        }
        
        // Credit limit validation - must be positive if provided
        if (createDTO.getCreditLimit() != null && createDTO.getCreditLimit().signum() < 0) {
            throw new IllegalArgumentException("Credit limit cannot be negative");
        }
    }
    
    public void validateForUpdate(Long customerId, CustomerUpdateDTO updateDTO) {
        // Tax number validation
        if (updateDTO.getTaxNo() != null && !updateDTO.getTaxNo().trim().isEmpty()) {
            customerRepository.findByTaxNo(updateDTO.getTaxNo())
                    .ifPresent(customer -> {
                        if (!customer.getId().equals(customerId)) {
                            throw new IllegalArgumentException("Tax number is already in use by another customer");
                        }
                    });
        }
        
        // Credit limit validation - must be positive if provided
        if (updateDTO.getCreditLimit() != null && updateDTO.getCreditLimit().signum() < 0) {
            throw new IllegalArgumentException("Credit limit cannot be negative");
        }
    }
}

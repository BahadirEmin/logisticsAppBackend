package com.company.logistics.logistics_backend.service;

import com.company.logistics.logistics_backend.domain.dto.CustomerCreateDTO;
import com.company.logistics.logistics_backend.domain.dto.CustomerResponseDTO;
import com.company.logistics.logistics_backend.domain.dto.CustomerUpdateDTO;
import com.company.logistics.logistics_backend.domain.entity.Customer;
import com.company.logistics.logistics_backend.domain.entity.CustomerRiskStatus;
import com.company.logistics.logistics_backend.domain.repository.CustomerRepository;
import com.company.logistics.logistics_backend.domain.repository.CustomerRiskStatusRepository;
import com.company.logistics.logistics_backend.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerRiskStatusRepository customerRiskStatusRepository;

    public CustomerResponseDTO createCustomer(CustomerCreateDTO createDTO) {
        // Check if customer with same tax number already exists
        if (customerRepository.existsByTaxNo(createDTO.getTaxNo())) {
            throw new IllegalArgumentException("Customer with tax number " + createDTO.getTaxNo() + " already exists");
        }

        // Get risk status
        CustomerRiskStatus riskStatus = customerRiskStatusRepository.findById(createDTO.getRiskStatusId())
                .orElseThrow(() -> new ResourceNotFoundException("Risk status not found"));

        Customer customer = Customer.builder()
                .name(createDTO.getName())
                .taxNo(createDTO.getTaxNo())
                .riskStatus(riskStatus)
                .isBlacklisted(createDTO.getIsBlacklisted() != null ? createDTO.getIsBlacklisted() : false)
                .isInLawsuit(createDTO.getIsInLawsuit() != null ? createDTO.getIsInLawsuit() : false)
                .creditLimit(createDTO.getCreditLimit())
                .build();

        return convertToDTO(customerRepository.save(customer));
    }

    public CustomerResponseDTO getCustomerById(Long id) {
        return customerRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
    }

    public List<CustomerResponseDTO> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(this::convertToDTO)
                .toList();
    }

    public CustomerResponseDTO updateCustomer(Long id, CustomerUpdateDTO updateDTO) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        // Check if tax number is being changed and if it already exists
        if (!customer.getTaxNo().equals(updateDTO.getTaxNo()) &&
                customerRepository.existsByTaxNo(updateDTO.getTaxNo())) {
            throw new IllegalArgumentException("Customer with tax number " + updateDTO.getTaxNo() + " already exists");
        }

        // Get risk status
        CustomerRiskStatus riskStatus = customerRiskStatusRepository.findById(updateDTO.getRiskStatusId())
                .orElseThrow(() -> new ResourceNotFoundException("Risk status not found"));

        customer.setName(updateDTO.getName());
        customer.setTaxNo(updateDTO.getTaxNo());
        customer.setRiskStatus(riskStatus);
        customer.setIsBlacklisted(updateDTO.getIsBlacklisted() != null ? updateDTO.getIsBlacklisted() : false);
        customer.setIsInLawsuit(updateDTO.getIsInLawsuit() != null ? updateDTO.getIsInLawsuit() : false);
        customer.setCreditLimit(updateDTO.getCreditLimit());

        return convertToDTO(customerRepository.save(customer));
    }

    public void deleteCustomer(Long id) {
        if (!customerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Customer not found");
        }
        customerRepository.deleteById(id);
    }

    public List<CustomerResponseDTO> getCustomersByRiskStatus(Long riskStatusId) {
        return customerRepository.findByRiskStatusId(riskStatusId).stream()
                .map(this::convertToDTO)
                .toList();
    }

    public List<CustomerResponseDTO> getBlacklistedCustomers() {
        return customerRepository.findByIsBlacklistedTrue().stream()
                .map(this::convertToDTO)
                .toList();
    }

    public List<CustomerResponseDTO> getCustomersInLawsuit() {
        return customerRepository.findByIsInLawsuitTrue().stream()
                .map(this::convertToDTO)
                .toList();
    }

    public CustomerResponseDTO getCustomerByTaxNo(String taxNo) {
        return customerRepository.findByTaxNo(taxNo)
                .map(this::convertToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
    }

    public List<CustomerResponseDTO> searchCustomersByName(String name) {
        return customerRepository.findByNameContainingIgnoreCase(name).stream()
                .map(this::convertToDTO)
                .toList();
    }

    public List<CustomerResponseDTO> getBlacklistedCustomersByRiskStatus(Long riskStatusId) {
        return customerRepository.findByRiskStatusIdAndIsBlacklistedTrue(riskStatusId).stream()
                .map(this::convertToDTO)
                .toList();
    }

    public List<CustomerResponseDTO> getCustomersInLawsuitByRiskStatus(Long riskStatusId) {
        return customerRepository.findByRiskStatusIdAndIsInLawsuitTrue(riskStatusId).stream()
                .map(this::convertToDTO)
                .toList();
    }

    private CustomerResponseDTO convertToDTO(Customer customer) {
        return CustomerResponseDTO.builder()
                .id(customer.getId())
                .name(customer.getName())
                .taxNo(customer.getTaxNo())
                .riskStatusId(customer.getRiskStatus().getId())
                .riskStatusName(customer.getRiskStatus().getStatusName())
                .isBlacklisted(customer.getIsBlacklisted())
                .isInLawsuit(customer.getIsInLawsuit())
                .creditLimit(customer.getCreditLimit())
                .createdAt(customer.getCreatedAt())
                .build();
    }
}
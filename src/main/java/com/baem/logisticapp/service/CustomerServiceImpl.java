package com.baem.logisticapp.service;

import com.baem.logisticapp.dto.CustomerCreateDTO;
import com.baem.logisticapp.dto.CustomerResponseDTO;
import com.baem.logisticapp.dto.CustomerUpdateDTO;
import com.baem.logisticapp.entity.Customer;
import com.baem.logisticapp.entity.CustomerRiskStatus;
import com.baem.logisticapp.repository.CustomerRepository;
import com.baem.logisticapp.repository.CustomerRiskStatusRepository;
import com.baem.logisticapp.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerRiskStatusRepository customerRiskStatusRepository;

    @Override
    public CustomerResponseDTO createCustomer(CustomerCreateDTO createDTO) {
        // Check if customer with same tax number already exists (only if taxNo is
        // provided)
        if (customerRepository.existsByTaxNoSafe(createDTO.getTaxNo())) {
            throw new IllegalArgumentException("Customer with tax number " + createDTO.getTaxNo() + " already exists");
        }

        // Get risk status
        CustomerRiskStatus riskStatus = customerRiskStatusRepository.findById(createDTO.getRiskStatusId())
                .orElseThrow(() -> new ResourceNotFoundException("Risk status not found"));

        Customer customer = Customer.builder()
                .name(createDTO.getName())
                .taxNo(createDTO.getTaxNo())
                .contactName(createDTO.getContactName())
                .phoneNumber(createDTO.getPhoneNumber())
                .address(createDTO.getAddress())
                .riskStatus(riskStatus)
                .isBlacklisted(createDTO.getIsBlacklisted() != null ? createDTO.getIsBlacklisted() : false)
                .isInLawsuit(createDTO.getIsInLawsuit() != null ? createDTO.getIsInLawsuit() : false)
                .creditLimit(createDTO.getCreditLimit())
                .build();

        return convertToDTO(customerRepository.save(customer));
    }

    @Override
    public CustomerResponseDTO getCustomerById(Long id) {
        return customerRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
    }

    @Override
    public List<CustomerResponseDTO> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    public CustomerResponseDTO updateCustomer(Long id, CustomerUpdateDTO updateDTO) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        // Check if tax number is being changed and if it already exists (only if taxNo
        // is provided)
        String currentTaxNo = customer.getTaxNo();
        String newTaxNo = updateDTO.getTaxNo();

        if (newTaxNo != null && !newTaxNo.trim().isEmpty() &&
                !newTaxNo.equals(currentTaxNo) &&
                customerRepository.existsByTaxNoSafe(newTaxNo)) {
            throw new IllegalArgumentException("Customer with tax number " + newTaxNo + " already exists");
        }

        // Get risk status
        CustomerRiskStatus riskStatus = customerRiskStatusRepository.findById(updateDTO.getRiskStatusId())
                .orElseThrow(() -> new ResourceNotFoundException("Risk status not found"));

        customer.setName(updateDTO.getName());
        customer.setTaxNo(updateDTO.getTaxNo());
        customer.setContactName(updateDTO.getContactName());
        customer.setPhoneNumber(updateDTO.getPhoneNumber());
        customer.setAddress(updateDTO.getAddress());
        customer.setRiskStatus(riskStatus);
        customer.setIsBlacklisted(updateDTO.getIsBlacklisted() != null ? updateDTO.getIsBlacklisted() : false);
        customer.setIsInLawsuit(updateDTO.getIsInLawsuit() != null ? updateDTO.getIsInLawsuit() : false);
        customer.setCreditLimit(updateDTO.getCreditLimit());

        return convertToDTO(customerRepository.save(customer));
    }

    @Override
    public void deleteCustomer(Long id) {
        if (!customerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Customer not found");
        }
        customerRepository.deleteById(id);
    }

    @Override
    public CustomerResponseDTO getCustomerByTaxNo(String taxNo) {
        return customerRepository.findByTaxNo(taxNo)
                .map(this::convertToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
    }

    @Override
    public List<CustomerResponseDTO> searchCustomers(String name, Long riskStatusId, Boolean blacklisted, Boolean inLawsuit, String taxNo) {
        return customerRepository.findCustomersWithFilters(name, riskStatusId, blacklisted, inLawsuit, taxNo).stream()
                .map(this::convertToDTO)
                .toList();
    }

    private CustomerResponseDTO convertToDTO(Customer customer) {
        return CustomerResponseDTO.builder()
                .id(customer.getId())
                .name(customer.getName())
                .taxNo(customer.getTaxNo())
                .contactName(customer.getContactName())
                .phoneNumber(customer.getPhoneNumber())
                .address(customer.getAddress())
                .riskStatusId(customer.getRiskStatus().getId())
                .riskStatusName(customer.getRiskStatus().getStatusName())
                .isBlacklisted(customer.getIsBlacklisted())
                .isInLawsuit(customer.getIsInLawsuit())
                .creditLimit(customer.getCreditLimit())
                .createdAt(customer.getCreatedAt())
                .build();
    }
}
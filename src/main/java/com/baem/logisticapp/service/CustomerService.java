package com.baem.logisticapp.service;

import com.baem.logisticapp.dto.CustomerCreateDTO;
import com.baem.logisticapp.dto.CustomerResponseDTO;
import com.baem.logisticapp.dto.CustomerUpdateDTO;

import java.util.List;

public interface CustomerService {

     CustomerResponseDTO createCustomer(CustomerCreateDTO createDTO);
     CustomerResponseDTO getCustomerById(Long id);
     List<CustomerResponseDTO> getAllCustomers();
     CustomerResponseDTO updateCustomer(Long id, CustomerUpdateDTO updateDTO);
     void deleteCustomer(Long id);
     CustomerResponseDTO getCustomerByTaxNo(String taxNo);
     List<CustomerResponseDTO> searchCustomers(String name, Long riskStatusId, Boolean blacklisted, Boolean inLawsuit, String taxNo);

}
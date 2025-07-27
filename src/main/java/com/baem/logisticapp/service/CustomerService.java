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
     List<CustomerResponseDTO> getCustomersByRiskStatus(Long riskStatusId);
     List<CustomerResponseDTO> getBlacklistedCustomers();
     List<CustomerResponseDTO> getCustomersInLawsuit();
     CustomerResponseDTO getCustomerByTaxNo(String taxNo);
     List<CustomerResponseDTO> searchCustomersByName(String name);
     List<CustomerResponseDTO> getBlacklistedCustomersByRiskStatus(Long riskStatusId);
     List<CustomerResponseDTO> getCustomersInLawsuitByRiskStatus(Long riskStatusId);

}
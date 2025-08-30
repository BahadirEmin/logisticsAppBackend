package com.baem.logisticapp.service;

import com.baem.logisticapp.dto.CustomerRiskStatusCreateDTO;
import com.baem.logisticapp.dto.CustomerRiskStatusResponseDTO;
import com.baem.logisticapp.dto.CustomerRiskStatusUpdateDTO;

import java.util.List;

public interface CustomerRiskStatusService {

    CustomerRiskStatusResponseDTO createRiskStatus(CustomerRiskStatusCreateDTO createDTO);

    CustomerRiskStatusResponseDTO getRiskStatusById(Long id);

    List<CustomerRiskStatusResponseDTO> getAllRiskStatuses();

    CustomerRiskStatusResponseDTO updateRiskStatus(Long id, CustomerRiskStatusUpdateDTO updateDTO);

    void deleteRiskStatus(Long id);
}
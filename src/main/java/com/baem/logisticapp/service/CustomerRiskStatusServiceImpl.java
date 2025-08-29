package com.baem.logisticapp.service;

import com.baem.logisticapp.dto.CustomerRiskStatusCreateDTO;
import com.baem.logisticapp.dto.CustomerRiskStatusResponseDTO;
import com.baem.logisticapp.dto.CustomerRiskStatusUpdateDTO;
import com.baem.logisticapp.entity.CustomerRiskStatus;
import com.baem.logisticapp.exception.ResourceNotFoundException;
import com.baem.logisticapp.repository.CustomerRiskStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerRiskStatusServiceImpl implements CustomerRiskStatusService {

    private final CustomerRiskStatusRepository customerRiskStatusRepository;

    @Override
    public CustomerRiskStatusResponseDTO createRiskStatus(CustomerRiskStatusCreateDTO createDTO) {
        // Check if risk status with same name already exists
        if (customerRiskStatusRepository.existsByStatusName(createDTO.getStatusName())) {
            throw new IllegalArgumentException(
                    "Risk status with name " + createDTO.getStatusName() + " already exists");
        }

        CustomerRiskStatus riskStatus = CustomerRiskStatus.builder()
                .statusName(createDTO.getStatusName())
                .build();

        return convertToDTO(customerRiskStatusRepository.save(riskStatus));
    }

    @Override
    public CustomerRiskStatusResponseDTO getRiskStatusById(Long id) {
        return customerRiskStatusRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Risk status not found"));
    }

    @Override
    public List<CustomerRiskStatusResponseDTO> getAllRiskStatuses() {
        return customerRiskStatusRepository.findAll().stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    public CustomerRiskStatusResponseDTO updateRiskStatus(Long id, CustomerRiskStatusUpdateDTO updateDTO) {
        CustomerRiskStatus riskStatus = customerRiskStatusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Risk status not found"));

        // Check if status name is being changed and if it already exists
        if (!riskStatus.getStatusName().equals(updateDTO.getStatusName()) &&
                customerRiskStatusRepository.existsByStatusName(updateDTO.getStatusName())) {
            throw new IllegalArgumentException(
                    "Risk status with name " + updateDTO.getStatusName() + " already exists");
        }

        riskStatus.setStatusName(updateDTO.getStatusName());
        return convertToDTO(customerRiskStatusRepository.save(riskStatus));
    }

    @Override
    public void deleteRiskStatus(Long id) {
        if (!customerRiskStatusRepository.existsById(id)) {
            throw new ResourceNotFoundException("Risk status not found");
        }
        customerRiskStatusRepository.deleteById(id);
    }

    private CustomerRiskStatusResponseDTO convertToDTO(CustomerRiskStatus riskStatus) {
        return CustomerRiskStatusResponseDTO.builder()
                .id(riskStatus.getId())
                .statusName(riskStatus.getStatusName())
                .build();
    }
}
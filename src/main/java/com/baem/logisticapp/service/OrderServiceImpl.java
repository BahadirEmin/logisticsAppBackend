package com.baem.logisticapp.service;

import com.baem.logisticapp.dto.OrderCreateDTO;
import com.baem.logisticapp.dto.OrderResponseDTO;
import com.baem.logisticapp.dto.OrderUpdateDTO;
import com.baem.logisticapp.entity.*;
import com.baem.logisticapp.exception.ResourceNotFoundException;
import com.baem.logisticapp.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final VehicleRepository vehicleRepository;
    private final TrailerRepository trailerRepository;

    @Override
    public OrderResponseDTO createOrder(OrderCreateDTO createDTO) {
        // Customer'ı bul
        Customer customer = customerRepository.findById(createDTO.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        // Sales person'ı bul (eğer belirtilmişse)
        User salesPerson = null;
        if (createDTO.getSalesPersonId() != null) {
            salesPerson = userRepository.findById(createDTO.getSalesPersonId())
                    .orElseThrow(() -> new ResourceNotFoundException("Sales person not found"));
        }

        Order order = Order.builder()
                .customer(customer)
                .departureCountry(createDTO.getDepartureCountry())
                .departureCity(createDTO.getDepartureCity())
                .departureDistrict(createDTO.getDepartureDistrict())
                .departurePostalCode(createDTO.getDeparturePostalCode())
                .departureAddress(createDTO.getDepartureAddress())
                .departureContactName(createDTO.getDepartureContactName())
                .departureContactPhone(createDTO.getDepartureContactPhone())
                .departureContactEmail(createDTO.getDepartureContactEmail())
                .arrivalCountry(createDTO.getArrivalCountry())
                .arrivalCity(createDTO.getArrivalCity())
                .arrivalDistrict(createDTO.getArrivalDistrict())
                .arrivalPostalCode(createDTO.getArrivalPostalCode())
                .arrivalAddress(createDTO.getArrivalAddress())
                .arrivalContactName(createDTO.getArrivalContactName())
                .arrivalContactPhone(createDTO.getArrivalContactPhone())
                .arrivalContactEmail(createDTO.getArrivalContactEmail())
                .cargoWidth(createDTO.getCargoWidth())
                .cargoLength(createDTO.getCargoLength())
                .cargoHeight(createDTO.getCargoHeight())
                .cargoWeightKg(createDTO.getCargoWeightKg())
                .cargoType(createDTO.getCargoType())
                .canTransfer(createDTO.getCanTransfer())
                .salesPerson(salesPerson)
                .customsAddress(createDTO.getCustomsAddress())
                .loadingDate(createDTO.getLoadingDate())
                .deadlineDate(createDTO.getDeadlineDate())
                .estimatedArrivalDate(createDTO.getEstimatedArrivalDate())
                .tripStatus(createDTO.getTripStatus())
                .build();

        return convertToDTO(orderRepository.save(order));
    }

    @Override
    public OrderResponseDTO getOrderById(Long id) {
        return orderRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
    }

    @Override
    public List<OrderResponseDTO> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    public OrderResponseDTO updateOrder(Long id, OrderUpdateDTO updateDTO) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        // Customer'ı güncelle
        if (updateDTO.getCustomerId() != null) {
            Customer customer = customerRepository.findById(updateDTO.getCustomerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
            order.setCustomer(customer);
        }

        // Sales person'ı güncelle
        if (updateDTO.getSalesPersonId() != null) {
            User salesPerson = userRepository.findById(updateDTO.getSalesPersonId())
                    .orElseThrow(() -> new ResourceNotFoundException("Sales person not found"));
            order.setSalesPerson(salesPerson);
        }

        // Diğer alanları güncelle
        updateOrderFields(order, updateDTO);
        order.setUpdatedAt(OffsetDateTime.now());

        return convertToDTO(orderRepository.save(order));
    }

    @Override
    public void deleteOrder(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new ResourceNotFoundException("Order not found");
        }
        orderRepository.deleteById(id);
    }

    @Override
    public List<OrderResponseDTO> getOrdersByCustomerId(Long customerId) {
        return orderRepository.findByCustomerId(customerId).stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    public List<OrderResponseDTO> getOrdersBySalesPersonId(Long salesPersonId) {
        return orderRepository.findBySalesPersonId(salesPersonId).stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    public List<OrderResponseDTO> getOrdersByFleetPersonId(Long fleetPersonId) {
        return orderRepository.findByFleetPersonId(fleetPersonId).stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    public List<OrderResponseDTO> getOrdersByTripStatus(String tripStatus) {
        TripStatus status = TripStatus.valueOf(tripStatus.toUpperCase());
        return orderRepository.findByTripStatus(status).stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    public OrderResponseDTO assignToOperation(Long orderId, Long operationPersonId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        User operationPerson = userRepository.findById(operationPersonId)
                .orElseThrow(() -> new ResourceNotFoundException("Operation person not found"));

        order.setOperationPerson(operationPerson);
        order.setUpdatedAt(OffsetDateTime.now());

        return convertToDTO(orderRepository.save(order));
    }

    @Override
    public OrderResponseDTO assignToFleet(Long orderId, Long fleetPersonId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        User fleetPerson = userRepository.findById(fleetPersonId)
                .orElseThrow(() -> new ResourceNotFoundException("Fleet person not found"));

        order.setFleetPerson(fleetPerson);
        order.setUpdatedAt(OffsetDateTime.now());

        return convertToDTO(orderRepository.save(order));
    }

    private void updateOrderFields(Order order, OrderUpdateDTO updateDTO) {
        if (updateDTO.getDepartureCountry() != null)
            order.setDepartureCountry(updateDTO.getDepartureCountry());
        if (updateDTO.getDepartureCity() != null)
            order.setDepartureCity(updateDTO.getDepartureCity());
        if (updateDTO.getDepartureDistrict() != null)
            order.setDepartureDistrict(updateDTO.getDepartureDistrict());
        if (updateDTO.getDeparturePostalCode() != null)
            order.setDeparturePostalCode(updateDTO.getDeparturePostalCode());
        if (updateDTO.getDepartureAddress() != null)
            order.setDepartureAddress(updateDTO.getDepartureAddress());
        if (updateDTO.getDepartureContactName() != null)
            order.setDepartureContactName(updateDTO.getDepartureContactName());
        if (updateDTO.getDepartureContactPhone() != null)
            order.setDepartureContactPhone(updateDTO.getDepartureContactPhone());
        if (updateDTO.getDepartureContactEmail() != null)
            order.setDepartureContactEmail(updateDTO.getDepartureContactEmail());

        if (updateDTO.getArrivalCountry() != null)
            order.setArrivalCountry(updateDTO.getArrivalCountry());
        if (updateDTO.getArrivalCity() != null)
            order.setArrivalCity(updateDTO.getArrivalCity());
        if (updateDTO.getArrivalDistrict() != null)
            order.setArrivalDistrict(updateDTO.getArrivalDistrict());
        if (updateDTO.getArrivalPostalCode() != null)
            order.setArrivalPostalCode(updateDTO.getArrivalPostalCode());
        if (updateDTO.getArrivalAddress() != null)
            order.setArrivalAddress(updateDTO.getArrivalAddress());
        if (updateDTO.getArrivalContactName() != null)
            order.setArrivalContactName(updateDTO.getArrivalContactName());
        if (updateDTO.getArrivalContactPhone() != null)
            order.setArrivalContactPhone(updateDTO.getArrivalContactPhone());
        if (updateDTO.getArrivalContactEmail() != null)
            order.setArrivalContactEmail(updateDTO.getArrivalContactEmail());

        if (updateDTO.getCargoWidth() != null)
            order.setCargoWidth(updateDTO.getCargoWidth());
        if (updateDTO.getCargoLength() != null)
            order.setCargoLength(updateDTO.getCargoLength());
        if (updateDTO.getCargoHeight() != null)
            order.setCargoHeight(updateDTO.getCargoHeight());
        if (updateDTO.getCargoWeightKg() != null)
            order.setCargoWeightKg(updateDTO.getCargoWeightKg());
        if (updateDTO.getCargoType() != null)
            order.setCargoType(updateDTO.getCargoType());
        if (updateDTO.getCanTransfer() != null)
            order.setCanTransfer(updateDTO.getCanTransfer());

        if (updateDTO.getCustomsAddress() != null)
            order.setCustomsAddress(updateDTO.getCustomsAddress());
        if (updateDTO.getLoadingDate() != null)
            order.setLoadingDate(updateDTO.getLoadingDate());
        if (updateDTO.getDeadlineDate() != null)
            order.setDeadlineDate(updateDTO.getDeadlineDate());
        if (updateDTO.getEstimatedArrivalDate() != null)
            order.setEstimatedArrivalDate(updateDTO.getEstimatedArrivalDate());
        if (updateDTO.getTripStatus() != null)
            order.setTripStatus(updateDTO.getTripStatus());
    }

    private OrderResponseDTO convertToDTO(Order order) {
        return OrderResponseDTO.builder()
                .id(order.getId())
                .customerId(order.getCustomer().getId())
                .customerName(order.getCustomer().getName())
                .departureCountry(order.getDepartureCountry())
                .departureCity(order.getDepartureCity())
                .departureDistrict(order.getDepartureDistrict())
                .departurePostalCode(order.getDeparturePostalCode())
                .departureAddress(order.getDepartureAddress())
                .departureContactName(order.getDepartureContactName())
                .departureContactPhone(order.getDepartureContactPhone())
                .departureContactEmail(order.getDepartureContactEmail())
                .arrivalCountry(order.getArrivalCountry())
                .arrivalCity(order.getArrivalCity())
                .arrivalDistrict(order.getArrivalDistrict())
                .arrivalPostalCode(order.getArrivalPostalCode())
                .arrivalAddress(order.getArrivalAddress())
                .arrivalContactName(order.getArrivalContactName())
                .arrivalContactPhone(order.getArrivalContactPhone())
                .arrivalContactEmail(order.getArrivalContactEmail())
                .cargoWidth(order.getCargoWidth())
                .cargoLength(order.getCargoLength())
                .cargoHeight(order.getCargoHeight())
                .cargoWeightKg(order.getCargoWeightKg())
                .cargoType(order.getCargoType())
                .canTransfer(order.getCanTransfer())
                .salesPersonId(order.getSalesPerson() != null ? order.getSalesPerson().getId() : null)
                .salesPersonName(order.getSalesPerson() != null ? order.getSalesPerson().getFullName() : null)
                .operationPersonId(order.getOperationPerson() != null ? order.getOperationPerson().getId() : null)
                .operationPersonName(order.getOperationPerson() != null ? order.getOperationPerson().getFullName() : null)
                .fleetPersonId(order.getFleetPerson() != null ? order.getFleetPerson().getId() : null)
                .fleetPersonName(order.getFleetPerson() != null ? order.getFleetPerson().getFullName() : null)
                .assignedTruckId(order.getAssignedTruck() != null ? order.getAssignedTruck().getId() : null)
                .assignedTruckPlateNo(order.getAssignedTruck() != null ? order.getAssignedTruck().getPlateNo() : null)
                .assignedTrailerId(order.getAssignedTrailer() != null ? order.getAssignedTrailer().getId() : null)
                .assignedTrailerNo(order.getAssignedTrailer() != null ? order.getAssignedTrailer().getTrailerNo() : null)
                .customsAddress(order.getCustomsAddress())
                .customsPersonId(order.getCustomsPerson() != null ? order.getCustomsPerson().getId() : null)
                .customsPersonName(order.getCustomsPerson() != null ? order.getCustomsPerson().getFullName() : null)
                .loadingDate(order.getLoadingDate())
                .deadlineDate(order.getDeadlineDate())
                .estimatedArrivalDate(order.getEstimatedArrivalDate())
                .tripStatus(order.getTripStatus())
                .tripStatusDisplayName(order.getTripStatus() != null ? order.getTripStatus().getDisplayName() : null)
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .build();
    }
}


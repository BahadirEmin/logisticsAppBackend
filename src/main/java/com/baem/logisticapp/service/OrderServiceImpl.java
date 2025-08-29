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
    private final DriverRepository driverRepository;

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

        // Araç bilgilerini bul (eğer belirtilmişse)
        Vehicle assignedTruck = null;
        if (createDTO.getAssignedTruckId() != null) {
            assignedTruck = vehicleRepository.findById(createDTO.getAssignedTruckId())
                    .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found"));
        }

        Trailer assignedTrailer = null;
        if (createDTO.getAssignedTrailerId() != null) {
            assignedTrailer = trailerRepository.findById(createDTO.getAssignedTrailerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Trailer not found"));
        }

        Driver assignedDriver = null;
        if (createDTO.getAssignedDriverId() != null) {
            assignedDriver = driverRepository.findById(createDTO.getAssignedDriverId())
                    .orElseThrow(() -> new ResourceNotFoundException("Driver not found"));
        }

        // Sefer numarası oluştur
        String tripNumber = generateTripNumber();

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
                .assignedTruck(assignedTruck)
                .assignedTrailer(assignedTrailer)
                .assignedDriver(assignedDriver)
                .quotePrice(createDTO.getQuotePrice())
                .actualPrice(createDTO.getActualPrice())
                .supplyType(createDTO.getSupplyType())
                .tripNumber(tripNumber)
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

    // Sefer numarası oluşturma metodu
    private String generateTripNumber() {
        // Basit bir sefer numarası oluşturma: SF + timestamp
        return "SF" + System.currentTimeMillis();
    }

    // Teklif onaylama metodu
    @Override
    public OrderResponseDTO approveQuote(Long orderId, Long approverUserId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        User approver = userRepository.findById(approverUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!approver.getCanApproveQuotes()) {
            throw new IllegalArgumentException("User does not have quote approval authority");
        }

        if (order.getTripStatus() != TripStatus.TEKLIF_ASAMASI) {
            throw new IllegalArgumentException("Order is not in quote stage");
        }

        order.setTripStatus(TripStatus.ONAYLANAN_TEKLIF);
        order.setUpdatedAt(OffsetDateTime.now());

        return convertToDTO(orderRepository.save(order));
    }

    // Teklif iptal etme metodu
    @Override
    public OrderResponseDTO cancelQuote(Long orderId, Long cancelerUserId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        User canceler = userRepository.findById(cancelerUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!canceler.getCanApproveQuotes()) {
            throw new IllegalArgumentException("User does not have quote cancellation authority");
        }

        if (order.getTripStatus() != TripStatus.TEKLIF_ASAMASI) {
            throw new IllegalArgumentException("Order is not in quote stage");
        }

        order.setTripStatus(TripStatus.IPTAL_EDILDI);
        order.setUpdatedAt(OffsetDateTime.now());

        return convertToDTO(orderRepository.save(order));
    }

    // Operasyoncu atama metodu (operasyoncu kendi yerine başkasını atayabilir)
    @Override
    public OrderResponseDTO assignToOperationByOperation(Long orderId, Long newOperationPersonId, Long currentOperationPersonId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        User currentOperationPerson = userRepository.findById(currentOperationPersonId)
                .orElseThrow(() -> new ResourceNotFoundException("Current operation person not found"));

        User newOperationPerson = userRepository.findById(newOperationPersonId)
                .orElseThrow(() -> new ResourceNotFoundException("New operation person not found"));

        // Mevcut operasyoncunun bu görevi değiştirme yetkisi var mı kontrol et
        if (!currentOperationPerson.getRole().equals(Role.OPERATION)) {
            throw new IllegalArgumentException("Current user is not an operation person");
        }

        order.setOperationPerson(newOperationPerson);
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
                .assignedDriverId(order.getAssignedDriver() != null ? order.getAssignedDriver().getId() : null)
                .assignedDriverName(order.getAssignedDriver() != null ? order.getAssignedDriver().getFullName() : null)
                .quotePrice(order.getQuotePrice())
                .actualPrice(order.getActualPrice())
                .supplyType(order.getSupplyType())
                .tripNumber(order.getTripNumber())
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

    @Override
    public OrderResponseDTO assignFleet(Long orderId, Long vehicleId, Long trailerId, Long driverId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));

        // Vehicle assignment
        if (vehicleId != null) {
            Vehicle vehicle = vehicleRepository.findById(vehicleId)
                    .orElseThrow(() -> new RuntimeException("Vehicle not found with id: " + vehicleId));
            order.setAssignedTruck(vehicle);
        }

        // Trailer assignment
        if (trailerId != null) {
            Trailer trailer = trailerRepository.findById(trailerId)
                    .orElseThrow(() -> new RuntimeException("Trailer not found with id: " + trailerId));
            order.setAssignedTrailer(trailer);
        }

        // Driver assignment
        if (driverId != null) {
            Driver driver = driverRepository.findById(driverId)
                    .orElseThrow(() -> new RuntimeException("Driver not found with id: " + driverId));
            order.setAssignedDriver(driver);
        }

        return convertToDTO(orderRepository.save(order));
    }
}


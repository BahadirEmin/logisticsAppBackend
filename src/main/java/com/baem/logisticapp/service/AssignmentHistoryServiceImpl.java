package com.baem.logisticapp.service;

import com.baem.logisticapp.dto.AssignmentHistoryDTO;
import com.baem.logisticapp.entity.Order;
import com.baem.logisticapp.entity.OrderAssignmentHistory;
import com.baem.logisticapp.entity.User;
import com.baem.logisticapp.exception.ResourceNotFoundException;
import com.baem.logisticapp.repository.OrderAssignmentHistoryRepository;
import com.baem.logisticapp.repository.OrderRepository;
import com.baem.logisticapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AssignmentHistoryServiceImpl implements AssignmentHistoryService {

    private final OrderAssignmentHistoryRepository assignmentHistoryRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    @Override
    public List<AssignmentHistoryDTO> getAssignmentHistory(Long orderId) {
        log.info("Getting assignment history for order: {}", orderId);
        
        List<OrderAssignmentHistory> historyList = assignmentHistoryRepository.findByOrderIdOrderByAssignedAtDesc(orderId);
        
        return historyList.stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    public void logDriverAssignment(Long orderId, Long driverId, String driverName, String assignedBy, Long assignedById, String previousValue, String notes) {
        String action = previousValue == null ? "Şoför Atama" : "Şoför Değişimi";
        
        saveAssignmentHistory(orderId, action, OrderAssignmentHistory.ResourceType.DRIVER, 
                driverId, driverName, assignedBy, assignedById, previousValue, driverName, notes);
    }

    @Override
    public void logVehicleAssignment(Long orderId, Long vehicleId, String vehiclePlateNo, String assignedBy, Long assignedById, String previousValue, String notes) {
        String action = previousValue == null ? "Araç Atama" : "Araç Değişimi";
        
        saveAssignmentHistory(orderId, action, OrderAssignmentHistory.ResourceType.VEHICLE, 
                vehicleId, vehiclePlateNo, assignedBy, assignedById, previousValue, vehiclePlateNo, notes);
    }

    @Override
    public void logTrailerAssignment(Long orderId, Long trailerId, String trailerNo, String assignedBy, Long assignedById, String previousValue, String notes) {
        String action = previousValue == null ? "Römork Atama" : "Römork Değişimi";
        
        saveAssignmentHistory(orderId, action, OrderAssignmentHistory.ResourceType.TRAILER, 
                trailerId, trailerNo, assignedBy, assignedById, previousValue, trailerNo, notes);
    }

    @Override
    public void logPersonnelAssignment(Long orderId, OrderAssignmentHistory.ResourceType resourceType, Long personnelId, String personnelName, String assignedBy, Long assignedById, String previousValue, String notes) {
        String action = getPersonnelAction(resourceType, previousValue == null);
        
        saveAssignmentHistory(orderId, action, resourceType, 
                personnelId, personnelName, assignedBy, assignedById, previousValue, personnelName, notes);
    }

    private void saveAssignmentHistory(Long orderId, String action, OrderAssignmentHistory.ResourceType resourceType,
                                     Long resourceId, String resourceName, String assignedBy, Long assignedById,
                                     String previousValue, String newValue, String notes) {
        try {
            Order order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

            User assignedByUser = userRepository.findById(assignedById)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));

            OrderAssignmentHistory history = OrderAssignmentHistory.builder()
                    .order(order)
                    .action(action)
                    .resourceType(resourceType)
                    .resourceId(resourceId)
                    .resourceName(resourceName)
                    .assignedBy(assignedBy)
                    .assignedByUser(assignedByUser)
                    .assignedAt(LocalDateTime.now())
                    .previousValue(previousValue)
                    .newValue(newValue)
                    .notes(notes)
                    .build();

            assignmentHistoryRepository.save(history);
            log.info("Assignment history logged: {} for order {}", action, orderId);
            
        } catch (Exception e) {
            log.error("Error saving assignment history for order {}: {}", orderId, e.getMessage());
            // Hata durumunda exception fırlatmıyoruz, sadece log atıyoruz ki ana işlem etkilenmesin
        }
    }

    private String getPersonnelAction(OrderAssignmentHistory.ResourceType resourceType, boolean isNew) {
        return switch (resourceType) {
            case OPERATION_PERSON -> isNew ? "Operasyoncu Atama" : "Operasyoncu Değişimi";
            case FLEET_PERSON -> isNew ? "Filocu Atama" : "Filocu Değişimi";
            case CUSTOMS_PERSON -> isNew ? "Gümrükçü Atama" : "Gümrükçü Değişimi";
            default -> isNew ? "Personel Atama" : "Personel Değişimi";
        };
    }

    private AssignmentHistoryDTO convertToDTO(OrderAssignmentHistory history) {
        return AssignmentHistoryDTO.builder()
                .id(history.getId())
                .orderId(history.getOrder().getId())
                .action(history.getAction())
                .resourceType(history.getResourceType().name())
                .resourceId(history.getResourceId())
                .resourceName(history.getResourceName())
                .assignedBy(history.getAssignedBy())
                .assignedById(history.getAssignedByUser().getId())
                .assignedAt(history.getAssignedAt())
                .previousValue(history.getPreviousValue())
                .newValue(history.getNewValue())
                .notes(history.getNotes())
                .build();
    }
}

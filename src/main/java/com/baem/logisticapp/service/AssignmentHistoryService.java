package com.baem.logisticapp.service;

import com.baem.logisticapp.dto.AssignmentHistoryDTO;
import com.baem.logisticapp.entity.OrderAssignmentHistory;

import java.util.List;

public interface AssignmentHistoryService {
    
    List<AssignmentHistoryDTO> getAssignmentHistory(Long orderId);
    
    void logDriverAssignment(Long orderId, Long driverId, String driverName, String assignedBy, Long assignedById, String previousValue, String notes);
    
    void logVehicleAssignment(Long orderId, Long vehicleId, String vehiclePlateNo, String assignedBy, Long assignedById, String previousValue, String notes);
    
    void logTrailerAssignment(Long orderId, Long trailerId, String trailerNo, String assignedBy, Long assignedById, String previousValue, String notes);
    
    void logPersonnelAssignment(Long orderId, OrderAssignmentHistory.ResourceType resourceType, Long personnelId, String personnelName, String assignedBy, Long assignedById, String previousValue, String notes);
}

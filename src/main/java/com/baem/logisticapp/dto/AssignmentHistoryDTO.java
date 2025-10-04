package com.baem.logisticapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssignmentHistoryDTO {
    
    private Long id;
    private Long orderId;
    private String action;
    private String resourceType;
    private Long resourceId;
    private String resourceName;
    private String assignedBy;
    private Long assignedById;
    private LocalDateTime assignedAt;
    private String previousValue;
    private String newValue;
    private String notes;
}

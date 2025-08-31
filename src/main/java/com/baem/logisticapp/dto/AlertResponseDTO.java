package com.baem.logisticapp.dto;

import com.baem.logisticapp.entity.Alert;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class AlertResponseDTO {
    
    private Long id;
    private String alertType;
    private String priority;
    private String title;
    private String description;
    private String relatedEntityType;
    private Long relatedEntityId;
    private LocalDate expiryDate;
    private LocalDate alertDate;
    private Boolean isActive;
    private Boolean isRead;
    private LocalDateTime createdAt;
    private LocalDateTime readAt;
    private String assignedToUsername;
    private String deactivatedByUsername;
    private LocalDateTime deactivatedAt;
    private Integer daysUntilExpiry;

    // Constructors
    public AlertResponseDTO() {}

    public AlertResponseDTO(Long id, String alertType, String priority, String title, 
                           String description, String relatedEntityType, Long relatedEntityId,
                           LocalDate expiryDate, LocalDate alertDate, Boolean isActive, 
                           Boolean isRead, LocalDateTime createdAt, LocalDateTime readAt,
                           String assignedToUsername, String deactivatedByUsername, 
                           LocalDateTime deactivatedAt) {
        this.id = id;
        this.alertType = alertType;
        this.priority = priority;
        this.title = title;
        this.description = description;
        this.relatedEntityType = relatedEntityType;
        this.relatedEntityId = relatedEntityId;
        this.expiryDate = expiryDate;
        this.alertDate = alertDate;
        this.isActive = isActive;
        this.isRead = isRead;
        this.createdAt = createdAt;
        this.readAt = readAt;
        this.assignedToUsername = assignedToUsername;
        this.deactivatedByUsername = deactivatedByUsername;
        this.deactivatedAt = deactivatedAt;
        this.daysUntilExpiry = calculateDaysUntilExpiry(expiryDate);
    }

    private Integer calculateDaysUntilExpiry(LocalDate expiryDate) {
        if (expiryDate == null) return null;
        return (int) java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), expiryDate);
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getAlertType() { return alertType; }
    public void setAlertType(String alertType) { this.alertType = alertType; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getRelatedEntityType() { return relatedEntityType; }
    public void setRelatedEntityType(String relatedEntityType) { this.relatedEntityType = relatedEntityType; }

    public Long getRelatedEntityId() { return relatedEntityId; }
    public void setRelatedEntityId(Long relatedEntityId) { this.relatedEntityId = relatedEntityId; }

    public LocalDate getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDate expiryDate) { 
        this.expiryDate = expiryDate;
        this.daysUntilExpiry = calculateDaysUntilExpiry(expiryDate);
    }

    public LocalDate getAlertDate() { return alertDate; }
    public void setAlertDate(LocalDate alertDate) { this.alertDate = alertDate; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

    public Boolean getIsRead() { return isRead; }
    public void setIsRead(Boolean isRead) { this.isRead = isRead; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getReadAt() { return readAt; }
    public void setReadAt(LocalDateTime readAt) { this.readAt = readAt; }

    public String getAssignedToUsername() { return assignedToUsername; }
    public void setAssignedToUsername(String assignedToUsername) { this.assignedToUsername = assignedToUsername; }

    public String getDeactivatedByUsername() { return deactivatedByUsername; }
    public void setDeactivatedByUsername(String deactivatedByUsername) { this.deactivatedByUsername = deactivatedByUsername; }

    public LocalDateTime getDeactivatedAt() { return deactivatedAt; }
    public void setDeactivatedAt(LocalDateTime deactivatedAt) { this.deactivatedAt = deactivatedAt; }

    public Integer getDaysUntilExpiry() { return daysUntilExpiry; }
    public void setDaysUntilExpiry(Integer daysUntilExpiry) { this.daysUntilExpiry = daysUntilExpiry; }
}

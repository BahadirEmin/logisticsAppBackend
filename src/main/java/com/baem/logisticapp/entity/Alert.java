package com.baem.logisticapp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "alerts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AlertType alertType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AlertPriority priority;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "related_entity_type")
    private String relatedEntityType; // "DRIVER", "VEHICLE", "CUSTOMER" etc.

    @Column(name = "related_entity_id")
    private Long relatedEntityId;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    @Column(name = "alert_date")
    private LocalDate alertDate; // Ne zaman alert verilmeli

    @Builder.Default
    @Column(name = "is_active")
    private Boolean isActive = true;

    @Builder.Default
    @Column(name = "is_read")
    private Boolean isRead = false;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "read_at")
    private LocalDateTime readAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_to_user_id")
    private User assignedToUser; // Hangi kullanıcıya assign edildi

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deactivated_by_user_id")
    private User deactivatedBy; // Alert'i kim deactive etti

    @Column(name = "deactivated_at")
    private LocalDateTime deactivatedAt; // Ne zaman deactive edildi

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        if (this.alertDate == null) {
            // Default olarak 30 gün önce alert ver
            this.alertDate = this.expiryDate != null ? this.expiryDate.minusDays(30) : LocalDate.now();
        }
    }

    public enum AlertType {
        VISA_EXPIRING("Visa Expiring"),
        LICENSE_EXPIRING("License Expiring"),
        VEHICLE_INSPECTION_DUE("Vehicle Inspection Due"),
        INSURANCE_EXPIRING("Insurance Expiring"),
        CONTRACT_EXPIRING("Contract Expiring"),
        PAYMENT_DUE("Payment Due"),
        DOCUMENT_MISSING("Document Missing"),
        MAINTENANCE_DUE("Maintenance Due");

        private final String displayName;

        AlertType(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public enum AlertPriority {
        LOW("Low"),
        MEDIUM("Medium"),
        HIGH("High"),
        CRITICAL("Critical");

        private final String displayName;

        AlertPriority(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
}

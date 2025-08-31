package com.baem.logisticapp.controller;

import com.baem.logisticapp.dto.AlertResponseDTO;
import com.baem.logisticapp.service.AlertService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/alerts")
@Tag(name = "08 - Alerts", description = "Alert and Notification Management")
public class AlertController {

    private final AlertService alertService;

    public AlertController(AlertService alertService) {
        this.alertService = alertService;
    }

    @GetMapping
    @Operation(summary = "Get all active alerts")
    public ResponseEntity<List<AlertResponseDTO>> getAllAlerts() {
        List<AlertResponseDTO> alerts = alertService.getAllActiveAlerts();
        return ResponseEntity.ok(alerts);
    }

    @GetMapping("/unread")
    @Operation(summary = "Get all unread alerts")
    public ResponseEntity<List<AlertResponseDTO>> getUnreadAlerts() {
        List<AlertResponseDTO> alerts = alertService.getUnreadAlerts();
        return ResponseEntity.ok(alerts);
    }

    @GetMapping("/critical")
    @Operation(summary = "Get critical alerts (High and Critical priority)")
    public ResponseEntity<List<AlertResponseDTO>> getCriticalAlerts() {
        List<AlertResponseDTO> alerts = alertService.getCriticalAlerts();
        return ResponseEntity.ok(alerts);
    }

    @GetMapping("/count")
    @Operation(summary = "Get unread alert count for dashboard")
    public ResponseEntity<Long> getUnreadAlertCount() {
        Long count = alertService.getUnreadAlertCount();
        return ResponseEntity.ok(count);
    }

    @PutMapping("/{id}/read")
    @Operation(summary = "Mark alert as read")
    public ResponseEntity<Void> markAsRead(@PathVariable Long id) {
        alertService.markAsRead(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/deactivate/{userId}")
    @Operation(summary = "Deactivate alert")
    public ResponseEntity<String> deactivateAlert(@PathVariable Long id, @PathVariable Long userId) {
        alertService.deactivateAlert(id, userId);
        return ResponseEntity.ok("Alert deactivated successfully");
    }

    @PutMapping("/{id}/assign/{userId}")
    @Operation(summary = "Assign alert to user")
    public ResponseEntity<String> assignAlert(@PathVariable Long id, @PathVariable Long userId) {
        alertService.assignAlert(id, userId);
        return ResponseEntity.ok("Alert assigned successfully");
    }

    @PutMapping("/{id}/unassign")
    @Operation(summary = "Unassign alert from user")
    public ResponseEntity<String> unassignAlert(@PathVariable Long id) {
        alertService.unassignAlert(id);
        return ResponseEntity.ok("Alert unassigned successfully");
    }

    @GetMapping("/assigned/{userId}")
    @Operation(summary = "Get alerts assigned to specific user")
    public ResponseEntity<List<AlertResponseDTO>> getAlertsAssignedToUser(@PathVariable Long userId) {
        List<AlertResponseDTO> alerts = alertService.getAlertsAssignedToUser(userId);
        return ResponseEntity.ok(alerts);
    }

    @PostMapping("/manual-check")
    @Operation(summary = "Manually trigger alert check (for testing)")
    public ResponseEntity<String> manualAlertCheck() {
        alertService.checkAndCreateAlerts();
        return ResponseEntity.ok("Alert check triggered manually - check logs for details");
    }
}

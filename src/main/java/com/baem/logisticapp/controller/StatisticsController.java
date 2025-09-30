package com.baem.logisticapp.controller;

import com.baem.logisticapp.dto.statistics.*;
import com.baem.logisticapp.service.StatisticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/statistics")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Statistics Management", description = "Dashboard and reporting statistics operations")
public class StatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping("/fleet-dashboard")
    @Operation(summary = "Get fleet dashboard statistics", description = "Returns vehicle, driver, trailer and offer statistics for fleet dashboard")
    public ResponseEntity<FleetDashboardStatsDTO> getFleetDashboardStats(
            @Parameter(description = "Fleet person ID")
            @RequestParam(required = false) Long fleetPersonId) {
        return ResponseEntity.ok(statisticsService.getFleetDashboardStats(fleetPersonId));
    }

    @GetMapping("/sales-dashboard")
    @Operation(summary = "Get sales dashboard statistics", description = "Returns monthly stats, status breakdown and financial data for sales dashboard")
    public ResponseEntity<SalesDashboardStatsDTO> getSalesDashboardStats(
            @Parameter(description = "Sales person ID")
            @RequestParam(required = false) Long salesPersonId) {
        return ResponseEntity.ok(statisticsService.getSalesDashboardStats(salesPersonId));
    }

    @GetMapping("/operator-dashboard")
    @Operation(summary = "Get operator dashboard statistics", description = "Returns trip stats, resource stats and assignment stats for operator dashboard")
    public ResponseEntity<OperatorDashboardStatsDTO> getOperatorDashboardStats(
            @Parameter(description = "Operator ID")
            @RequestParam(required = false) Long operatorId) {
        return ResponseEntity.ok(statisticsService.getOperatorDashboardStats(operatorId));
    }

    @GetMapping("/recent-activities")
    @Operation(summary = "Get recent activities", description = "Returns recent activities for dashboard")
    public ResponseEntity<List<RecentActivityDTO>> getRecentActivities(
            @Parameter(description = "Maximum number of activities to return")
            @RequestParam(defaultValue = "10") Integer limit,
            @Parameter(description = "Activity type filter")
            @RequestParam(required = false) String type,
            @Parameter(description = "User ID")
            @RequestParam(required = false) Long userId) {
        return ResponseEntity.ok(statisticsService.getRecentActivities(limit, type, userId));
    }

    @GetMapping("/notifications/dashboard")
    @Operation(summary = "Get dashboard notifications", description = "Returns notifications for dashboard")
    public ResponseEntity<List<NotificationDTO>> getDashboardNotifications(
            @Parameter(description = "User ID")
            @RequestParam(required = false) Long userId,
            @Parameter(description = "Return only unread notifications")
            @RequestParam(defaultValue = "false") Boolean unreadOnly) {
        return ResponseEntity.ok(statisticsService.getDashboardNotifications(userId, unreadOnly));
    }
}

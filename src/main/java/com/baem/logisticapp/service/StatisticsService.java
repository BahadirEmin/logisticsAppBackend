package com.baem.logisticapp.service;

import com.baem.logisticapp.dto.statistics.*;
import com.baem.logisticapp.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatisticsService {

    private final VehicleRepository vehicleRepository;
    private final DriverRepository driverRepository;
    private final TrailerRepository trailerRepository;
    private final OrderRepository orderRepository;

    public FleetDashboardStatsDTO getFleetDashboardStats(Long fleetPersonId) {
        log.info("Getting fleet dashboard stats for fleet person: {}", fleetPersonId);
        
        // Vehicle stats
        var vehicleStats = FleetDashboardStatsDTO.VehicleStatsDTO.builder()
                .total(vehicleRepository.countAll())
                .active(vehicleRepository.countByActiveTrue())
                .maintenance(0) // Bu alanı ileriye erteleyebiliriz
                .available(vehicleRepository.countByActiveTrue())
                .build();

        // Driver stats  
        var driverStats = FleetDashboardStatsDTO.DriverStatsDTO.builder()
                .total(driverRepository.countAll())
                .active(driverRepository.countByActiveTrue())
                .onLeave(0) // Bu alanı ileriye erteleyebiliriz
                .build();

        // Trailer stats
        var trailerStats = FleetDashboardStatsDTO.TrailerStatsDTO.builder()
                .total(trailerRepository.countAll())
                .active(trailerRepository.countByActiveTrue())
                .maintenance(0) // Bu alanı ileriye erteleyebiliriz
                .build();

        // Offer stats (using orders as offers)
        var offerStats = FleetDashboardStatsDTO.OfferStatsDTO.builder()
                .total(orderRepository.countAll())
                .pending(orderRepository.countByStatus("PENDING"))
                .approved(orderRepository.countByStatus("APPROVED"))
                .completed(orderRepository.countByStatus("COMPLETED"))
                .build();

        return FleetDashboardStatsDTO.builder()
                .vehicleStats(vehicleStats)
                .driverStats(driverStats)
                .trailerStats(trailerStats)
                .offerStats(offerStats)
                .build();
    }

    public SalesDashboardStatsDTO getSalesDashboardStats(Long salesPersonId) {
        log.info("Getting sales dashboard stats for sales person: {}", salesPersonId);
        
        OffsetDateTime now = OffsetDateTime.now();
        OffsetDateTime startOfThisMonth = now.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        OffsetDateTime startOfLastMonth = startOfThisMonth.minusMonths(1);

        // Monthly orders count
        int thisMonthOrders = orderRepository.countByCreatedDateBetween(startOfThisMonth, now);

        // Total stats
        int totalOffers = orderRepository.countAll();
        
        var totalStats = SalesDashboardStatsDTO.TotalStatsDTO.builder()
                .totalOffers(totalOffers)
                .thisMonth(thisMonthOrders)
                .build();

        // Monthly stats
        int lastMonthOrders = orderRepository.countByCreatedDateBetween(startOfLastMonth, startOfThisMonth);
        double growth = lastMonthOrders > 0 ? (double) (thisMonthOrders - lastMonthOrders) / lastMonthOrders : 0.0;

        var monthlyStats = SalesDashboardStatsDTO.MonthlyStatsDTO.builder()
                .thisMonth(thisMonthOrders)
                .lastMonth(lastMonthOrders)
                .growth(growth)
                .build();

        // Status breakdown
        var statusBreakdown = SalesDashboardStatsDTO.StatusBreakdownDTO.builder()
                .approved(orderRepository.countByStatus("APPROVED"))
                .pending(orderRepository.countByStatus("PENDING"))
                .rejected(orderRepository.countByStatus("REJECTED"))
                .build();

        // Financial data
        var financial = SalesDashboardStatsDTO.FinancialDTO.builder()
                .totalValue(orderRepository.sumTotalAmount())
                .monthlyValue(orderRepository.sumTotalAmountByCreatedDateBetween(startOfThisMonth, now))
                .successRate(calculateSuccessRate())
                .build();

        return SalesDashboardStatsDTO.builder()
                .totalStats(totalStats)
                .monthlyStats(monthlyStats)
                .statusBreakdown(statusBreakdown)
                .financial(financial)
                .needsOperatorAssignment(orderRepository.countByStatusAndAssignedOperatorIsNull("APPROVED"))
                .build();
    }

    public OperatorDashboardStatsDTO getOperatorDashboardStats(Long operatorId) {
        log.info("Getting operator dashboard stats for operator: {}", operatorId);
        
        OffsetDateTime today = OffsetDateTime.now().withHour(0).withMinute(0).withSecond(0);
        OffsetDateTime tomorrow = today.plusDays(1);

        // Trip stats (using orders as trips)
        var tripStats = OperatorDashboardStatsDTO.TripStatsDTO.builder()
                .active(orderRepository.countByStatus("IN_PROGRESS"))
                .completedToday(orderRepository.countByStatusAndCompletedDateBetween("COMPLETED", today, tomorrow))
                .delayed(orderRepository.countDelayed())
                .build();

        // Resource stats
        var resourceStats = OperatorDashboardStatsDTO.ResourceStatsDTO.builder()
                .activeVehicles(vehicleRepository.countByActiveTrue())
                .activeDrivers(driverRepository.countByActiveTrue())
                .approvedOffers(orderRepository.countByStatus("APPROVED"))
                .build();

        // Assignment stats
        var assignmentStats = OperatorDashboardStatsDTO.AssignmentStatsDTO.builder()
                .pendingAssignments(orderRepository.countByStatusAndAssignedOperatorIsNull("APPROVED"))
                .todayAssignments(orderRepository.countByAssignedDateBetween(today, tomorrow))
                .build();

        return OperatorDashboardStatsDTO.builder()
                .tripStats(tripStats)
                .resourceStats(resourceStats)
                .assignmentStats(assignmentStats)
                .build();
    }

    public List<RecentActivityDTO> getRecentActivities(Integer limit, String type, Long userId) {
        log.info("Getting recent activities: limit={}, type={}, userId={}", limit, type, userId);
        
        // Mock data for now - in real implementation, you would have an activity log table
        List<RecentActivityDTO> activities = new ArrayList<>();
        
        activities.add(RecentActivityDTO.builder()
                .id(1L)
                .type("truck")
                .title("Araç Durumu Değişti")
                .message("Tır #TRK-001 bakımdan döndü")
                .timestamp(LocalDateTime.now().minusHours(2))
                .relativeTime("2 saat önce")
                .status("success")
                .entityId(123L)
                .build());
                
        activities.add(RecentActivityDTO.builder()
                .id(2L)
                .type("order")
                .title("Yeni Sipariş")
                .message("ABC Şirketi'nden yeni sipariş alındı")
                .timestamp(LocalDateTime.now().minusHours(4))
                .relativeTime("4 saat önce")
                .status("info")
                .entityId(456L)
                .build());

        return activities.stream().limit(limit != null ? limit : 10).toList();
    }

    public List<NotificationDTO> getDashboardNotifications(Long userId, Boolean unreadOnly) {
        log.info("Getting dashboard notifications for user: {}, unreadOnly: {}", userId, unreadOnly);
        
        // Mock data for now - in real implementation, you would have a notifications table
        List<NotificationDTO> notifications = new ArrayList<>();
        
        notifications.add(NotificationDTO.builder()
                .id(1L)
                .type("maintenance_due")
                .title("Bakım Zamanı")
                .message("34 ABC 123 plakalı araç bakım zamanı geldi")
                .priority("high")
                .timestamp(LocalDateTime.now().minusHours(8))
                .read(false)
                .actionUrl("/admin/vehicles/1")
                .build());

        return notifications.stream()
                .filter(notification -> !Boolean.TRUE.equals(unreadOnly) || !notification.getRead())
                .toList();
    }

    public RecentOffersDTO getRecentOffers(Integer limit) {
        log.info("Getting recent offers with limit: {}", limit);
        
        // Mock data for now - in real implementation, you would get from orders
        List<RecentOffersDTO.OfferDTO> offers = new ArrayList<>();
        
        offers.add(RecentOffersDTO.OfferDTO.builder()
                .id(1L)
                .customerName("ABC Ltd.")
                .departureCity("İstanbul")
                .arrivalCity("Ankara")
                .estimatedPrice(new java.math.BigDecimal("15000"))
                .currency("TRY")
                .status("approved")
                .createdAt(OffsetDateTime.now().minusHours(2))
                .build());
                
        offers.add(RecentOffersDTO.OfferDTO.builder()
                .id(2L)
                .customerName("XYZ Şirketi")
                .departureCity("Ankara")
                .arrivalCity("İzmir")
                .estimatedPrice(new java.math.BigDecimal("12000"))
                .currency("TRY")
                .status("pending")
                .createdAt(OffsetDateTime.now().minusHours(4))
                .build());

        List<RecentOffersDTO.OfferDTO> limitedOffers = offers.stream()
                .limit(limit != null ? limit : 10)
                .toList();

        return RecentOffersDTO.builder()
                .offers(limitedOffers)
                .build();
    }

    private double calculateSuccessRate() {
        int totalOrders = orderRepository.countAll();
        int successfulOrders = orderRepository.countByStatus("COMPLETED");
        return totalOrders > 0 ? (double) successfulOrders / totalOrders : 0.0;
    }
}

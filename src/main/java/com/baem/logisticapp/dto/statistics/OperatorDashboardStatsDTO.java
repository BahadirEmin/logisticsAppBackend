package com.baem.logisticapp.dto.statistics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OperatorDashboardStatsDTO {
    
    private TripStatsDTO tripStats;
    private ResourceStatsDTO resourceStats;
    private AssignmentStatsDTO assignmentStats;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TripStatsDTO {
        private Integer active;
        private Integer completedToday;
        private Integer delayed;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResourceStatsDTO {
        private Integer activeVehicles;
        private Integer activeDrivers;
        private Integer approvedOffers;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AssignmentStatsDTO {
        private Integer pendingAssignments;
        private Integer todayAssignments;
    }
}

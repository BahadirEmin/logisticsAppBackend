package com.baem.logisticapp.dto.statistics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FleetDashboardStatsDTO {
    
    private VehicleStatsDTO vehicleStats;
    private DriverStatsDTO driverStats;
    private TrailerStatsDTO trailerStats;
    private OfferStatsDTO offerStats;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VehicleStatsDTO {
        private Integer total;
        private Integer active;
        private Integer maintenance;
        private Integer available;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DriverStatsDTO {
        private Integer total;
        private Integer active;
        private Integer onLeave;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TrailerStatsDTO {
        private Integer total;
        private Integer active;
        private Integer maintenance;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OfferStatsDTO {
        private Integer total;
        private Integer pending;
        private Integer approved;
        private Integer completed;
    }
}

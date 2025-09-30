package com.baem.logisticapp.dto.statistics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalesDashboardStatsDTO {
    
    private MonthlyStatsDTO monthlyStats;
    private StatusBreakdownDTO statusBreakdown;
    private FinancialDTO financial;
    private Integer needsOperatorAssignment;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MonthlyStatsDTO {
        private Integer thisMonth;
        private Integer lastMonth;
        private Double growth;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StatusBreakdownDTO {
        private Integer approved;
        private Integer pending;
        private Integer rejected;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FinancialDTO {
        private Double totalValue;
        private Double monthlyValue;
        private Double successRate;
    }
}

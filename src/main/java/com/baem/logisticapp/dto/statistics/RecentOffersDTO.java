package com.baem.logisticapp.dto.statistics;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecentOffersDTO {
    
    private List<OfferDTO> offers;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OfferDTO {
        private Long id;
        private String customerName;
        private String departureCity;
        private String arrivalCity;
        private BigDecimal estimatedPrice;
        private String currency;
        private String status; // approved, pending, rejected
        
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
        private OffsetDateTime createdAt;
    }
}

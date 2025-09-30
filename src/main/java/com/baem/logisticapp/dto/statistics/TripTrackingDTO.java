package com.baem.logisticapp.dto.statistics;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TripTrackingDTO {
    
    private TripSummaryDTO summary;
    private List<TripDetailsDTO> trips;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TripSummaryDTO {
        private Integer total;
        private Integer inProgress;
        private Integer completed;
        private Integer delayed;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TripDetailsDTO {
        private Long id;
        private String tripNumber;
        private TripDriverDTO driver;
        private TripVehicleDTO vehicle;
        private TripRouteDTO route;
        private String status;
        private Integer progress;
        private TripDatesDTO dates;
        private TripCargoDTO cargo;
        private TripCustomerDTO customer;
        private TripLocationDTO location;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TripDriverDTO {
        private Long id;
        private String name;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TripVehicleDTO {
        private Long id;
        private String plate;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TripRouteDTO {
        private String from;
        private String to;
        private String currentLocation;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TripDatesDTO {
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
        private LocalDateTime startDate;
        
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
        private LocalDateTime estimatedArrival;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TripCargoDTO {
        private String type;
        private String weight;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TripCustomerDTO {
        private Long id;
        private String name;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TripLocationDTO {
        private Double lat;
        private Double lng;
        
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
        private LocalDateTime lastUpdate;
    }
}

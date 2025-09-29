package com.baem.logisticapp.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CountryCodeUpdateDTO {

    @Size(max = 100, message = "Country name cannot exceed 100 characters")
    private String countryName;

    @Size(max = 100, message = "Turkish country name cannot exceed 100 characters")
    private String countryNameTr;

    @Size(max = 3, message = "ISO country code cannot exceed 3 characters")
    private String countryCodeIso; // TR, DE, FR, etc.

    @Size(max = 2, message = "Numeric country code cannot exceed 2 characters")
    private String countryCodeNumeric; // 90, 49, 33, etc.

    private Boolean isActive;
}

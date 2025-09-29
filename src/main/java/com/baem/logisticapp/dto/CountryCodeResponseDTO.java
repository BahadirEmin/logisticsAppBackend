package com.baem.logisticapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CountryCodeResponseDTO {

    private Long id;
    private String countryName;
    private String countryNameTr;
    private String countryCodeIso; // TR, DE, FR, etc.
    private String countryCodeNumeric; // 90, 49, 33, etc.
    private Boolean isActive;
}

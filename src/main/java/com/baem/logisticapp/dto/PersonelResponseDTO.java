package com.baem.logisticapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonelResponseDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String role;
    private String department;
    private LocalDate hireDate;
    private String email;
    private String phone;
    private Boolean isActive;
}
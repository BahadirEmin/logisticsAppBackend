package com.baem.logisticapp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "driver")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "driver_id")
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(unique = true, nullable = false)
    private String licenseNo;

    @Column(nullable = false)
    private String licenseClass;

    @Column(nullable = false)
    private LocalDate passportExpiry;

    @Column(nullable = false)
    private LocalDate visaExpiry;

    @Column(nullable = false)
    private LocalDate residencePermitExpiry;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String email;

    @Builder.Default
    private Boolean isActive = true;
    
    // Helper method to get full name
    public String getFullName() {
        return firstName + " " + lastName;
    }
}

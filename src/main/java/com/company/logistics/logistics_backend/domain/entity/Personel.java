package com.company.logistics.logistics_backend.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "personel")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Personel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "personel_id")
    private Long id;

    private String firstName;
    private String lastName;
    private String role; // Görev / Unvan
    private String department;
    private LocalDate hireDate;

    private String email;
    private String phone;

    @Builder.Default
    private Boolean isActive = true;
}

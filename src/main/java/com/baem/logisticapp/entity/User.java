package com.baem.logisticapp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    // Personel bilgileri
    private String firstName;
    private String lastName;
    private String department; // Departman (Satış, Operasyon, Filo, Gümrük vb.)
    private String phone; // Telefon numarası

    @Column(name = "hire_date")
    private LocalDate hireDate; // İşe başlama tarihi

    @Builder.Default
    @Column(name = "is_active")
    private Boolean isActive = true; // Aktif/pasif durumu

    @Builder.Default
    @Column(name = "can_approve_quotes")
    private Boolean canApproveQuotes = false; // Teklif onaylama yetkisi

    private String email; // Email adresi

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    // Helper method to get full name
    public String getFullName() {
        if (firstName != null && lastName != null) {
            return firstName + " " + lastName;
        } else if (firstName != null) {
            return firstName;
        } else if (lastName != null) {
            return lastName;
        }
        return username; // fallback to username
    }
}
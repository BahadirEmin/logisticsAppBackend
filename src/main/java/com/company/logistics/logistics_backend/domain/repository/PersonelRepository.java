package com.company.logistics.logistics_backend.domain.repository;

import com.company.logistics.logistics_backend.domain.entity.Personel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PersonelRepository extends JpaRepository<Personel, Long> {

    // Email ile personel bulma
    Optional<Personel> findByEmail(String email);

    // Telefon numarası ile personel bulma
    Optional<Personel> findByPhone(String phone);

    // Departmana göre personel bulma
    List<Personel> findByDepartment(String department);

    // Role göre personel bulma
    List<Personel> findByRole(String role);

    // Aktif personel bulma
    List<Personel> findByIsActiveTrue();

    // İsme göre personel arama (içerir)
    List<Personel> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String firstName, String lastName);

    // Email ile personel var mı kontrol etme
    boolean existsByEmail(String email);

    // Telefon numarası ile personel var mı kontrol etme
    boolean existsByPhone(String phone);

    // Departman ve role göre personel bulma
    List<Personel> findByDepartmentAndRole(String department, String role);

    // İşe başlama tarihine göre personel bulma
    List<Personel> findByHireDateAfter(LocalDate date);

    // Belirli bir tarihten sonra işe başlayan aktif personel
    List<Personel> findByHireDateAfterAndIsActiveTrue(LocalDate date);

    // Departman ve aktiflik durumuna göre personel bulma
    List<Personel> findByDepartmentAndIsActiveTrue(String department);
}
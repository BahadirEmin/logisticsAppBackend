package com.baem.logisticapp.repository;

import com.baem.logisticapp.entity.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {

    // Lisans numarasına göre sürücü bulma
    Optional<Driver> findByLicenseNo(String licenseNo);

    // Belirli bir lisans sınıfına sahip sürücüleri bulma
    List<Driver> findByLicenseClass(String licenseClass);

    // Pasaport süresi yakında dolacak sürücüleri bulma
    List<Driver> findByPassportExpiryBefore(LocalDate date);

    // Vize süresi yakında dolacak sürücüleri bulma
    List<Driver> findByVisaExpiryBefore(LocalDate date);

    // İkamet izni süresi yakında dolacak sürücüleri bulma
    List<Driver> findByResidencePermitExpiryBefore(LocalDate date);

    List<Driver> findByIsActiveTrue();

    Optional<Driver> findByEmail(String email);

    Optional<Driver> findByPhoneNumber(String phoneNumber);
}
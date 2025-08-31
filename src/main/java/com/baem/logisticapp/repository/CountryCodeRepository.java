package com.baem.logisticapp.repository;

import com.baem.logisticapp.entity.CountryCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CountryCodeRepository extends JpaRepository<CountryCode, Long> {

    // İngilizce ülke ismine göre arama
    Optional<CountryCode> findByCountryNameIgnoreCaseAndIsActiveTrue(String countryName);

    // Türkçe ülke ismine göre arama
    Optional<CountryCode> findByCountryNameTrIgnoreCaseAndIsActiveTrue(String countryNameTr);

    // ISO koduna göre arama
    Optional<CountryCode> findByCountryCodeIsoIgnoreCaseAndIsActiveTrue(String countryCodeIso);

    // Numerik koda göre arama
    Optional<CountryCode> findByCountryCodeNumericAndIsActiveTrue(String countryCodeNumeric);

    // Ülke ismi (herhangi bir dilde) ile arama
    @Query("SELECT cc FROM CountryCode cc WHERE cc.isActive = true AND " +
           "(UPPER(cc.countryName) = UPPER(:countryName) OR " +
           "UPPER(cc.countryNameTr) = UPPER(:countryName) OR " +
           "UPPER(cc.countryCodeIso) = UPPER(:countryName))")
    Optional<CountryCode> findByAnyCountryName(@Param("countryName") String countryName);
}

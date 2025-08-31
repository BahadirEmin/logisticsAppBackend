package com.baem.logisticapp.config;

import com.baem.logisticapp.entity.CountryCode;
import com.baem.logisticapp.entity.CustomerRiskStatus;
import com.baem.logisticapp.entity.Role;
import com.baem.logisticapp.entity.User;
import com.baem.logisticapp.entity.VehicleOwnershipType;
import com.baem.logisticapp.repository.CountryCodeRepository;
import com.baem.logisticapp.repository.CustomerRiskStatusRepository;
import com.baem.logisticapp.repository.UserRepository;
import com.baem.logisticapp.repository.VehicleOwnershipTypeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

@Configuration
public class DataInitializer {
    private final UserRepository userRepository;
    private final CustomerRiskStatusRepository customerRiskStatusRepository;
    private final VehicleOwnershipTypeRepository vehicleOwnershipTypeRepository;
    private final CountryCodeRepository countryCodeRepository;
    private final PasswordEncoder passwordEncoder;
    
    public DataInitializer(UserRepository userRepository,
                          CustomerRiskStatusRepository customerRiskStatusRepository,
                          VehicleOwnershipTypeRepository vehicleOwnershipTypeRepository,
                          CountryCodeRepository countryCodeRepository,
                          PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.customerRiskStatusRepository = customerRiskStatusRepository;
        this.vehicleOwnershipTypeRepository = vehicleOwnershipTypeRepository;
        this.countryCodeRepository = countryCodeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public CommandLineRunner initData() {
        return args -> {
            // Delay initialization to allow Spring context to fully load first
            try {
                Thread.sleep(500); // Short delay to let startup messages appear first
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            
            // Only initialize if database is empty
            if (userRepository.count() == 0) {
                initUsers();
            }
            if (customerRiskStatusRepository.count() == 0) {
                initRiskStatuses();
            }
            if (vehicleOwnershipTypeRepository.count() == 0) {
                initVehicleOwnershipTypes();
            }
            if (countryCodeRepository.count() == 0) {
                initCountryCodes();
            }
        };
    }

    private void initUsers() {
        userRepository.save(User.builder()
                .username("sales")
                .firstName("Ahmet")
                .lastName("Satış")
                .department("Satış")
                .phone("0532-111-1111")
                .hireDate(LocalDate.of(2023, 1, 15))
                .email("sales@logistics.com")
                .password(passwordEncoder.encode("sales123"))
                .role(Role.SALES)
                .isActive(true)
                .build());
        
        userRepository.save(User.builder()
                .username("operation")
                .firstName("Mehmet")
                .lastName("Operasyon")
                .department("Operasyon")
                .phone("0532-222-2222")
                .hireDate(LocalDate.of(2022, 6, 10))
                .email("operation@logistics.com")
                .password(passwordEncoder.encode("operation123"))
                .role(Role.OPERATION)
                .isActive(true)
                .build());
        
        userRepository.save(User.builder()
                .username("fleet")
                .firstName("Ali")
                .lastName("Filo")
                .department("Filo Yönetimi")
                .phone("0532-333-3333")
                .hireDate(LocalDate.of(2021, 9, 5))
                .email("fleet@logistics.com")
                .password(passwordEncoder.encode("fleet123"))
                .role(Role.FLEET)
                .isActive(true)
                .build());
        
        userRepository.save(User.builder()
                .username("admin")
                .firstName("Fatma")
                .lastName("Admin")
                .department("Yönetim")
                .phone("0532-444-4444")
                .hireDate(LocalDate.of(2020, 3, 1))
                .email("admin@logistics.com")
                .password(passwordEncoder.encode("admin123"))
                .role(Role.ADMIN)
                .isActive(true)
                .build());
    }

    private void initRiskStatuses() {
        customerRiskStatusRepository.save(CustomerRiskStatus.builder()
                .statusName("Düşük Risk")
                .build());
        customerRiskStatusRepository.save(CustomerRiskStatus.builder()
                .statusName("Orta Risk")
                .build());
        customerRiskStatusRepository.save(CustomerRiskStatus.builder()
                .statusName("Yüksek Risk")
                .build());
        customerRiskStatusRepository.save(CustomerRiskStatus.builder()
                .statusName("Kara Liste")
                .build());
    }

    private void initVehicleOwnershipTypes() {
        vehicleOwnershipTypeRepository.save(VehicleOwnershipType.builder()
                .ownershipName("Özmal")
                .build());
        vehicleOwnershipTypeRepository.save(VehicleOwnershipType.builder()
                .ownershipName("Kiralık")
                .build());
        vehicleOwnershipTypeRepository.save(VehicleOwnershipType.builder()
                .ownershipName("Spedisyon")
                .build());
    }

    private void initCountryCodes() {
        // Temel Avrupa ülkeleri
        countryCodeRepository.save(CountryCode.builder()
                .countryName("TURKEY")
                .countryNameTr("TÜRKİYE")
                .countryCodeIso("TR")
                .countryCodeNumeric("90")
                .isActive(true)
                .build());

        countryCodeRepository.save(CountryCode.builder()
                .countryName("GERMANY")
                .countryNameTr("ALMANYA")
                .countryCodeIso("DE")
                .countryCodeNumeric("49")
                .isActive(true)
                .build());

        countryCodeRepository.save(CountryCode.builder()
                .countryName("FRANCE")
                .countryNameTr("FRANSA")
                .countryCodeIso("FR")
                .countryCodeNumeric("33")
                .isActive(true)
                .build());

        countryCodeRepository.save(CountryCode.builder()
                .countryName("ITALY")
                .countryNameTr("İTALYA")
                .countryCodeIso("IT")
                .countryCodeNumeric("39")
                .isActive(true)
                .build());

        countryCodeRepository.save(CountryCode.builder()
                .countryName("SPAIN")
                .countryNameTr("İSPANYA")
                .countryCodeIso("ES")
                .countryCodeNumeric("34")
                .isActive(true)
                .build());

        countryCodeRepository.save(CountryCode.builder()
                .countryName("NETHERLANDS")
                .countryNameTr("HOLLANDA")
                .countryCodeIso("NL")
                .countryCodeNumeric("31")
                .isActive(true)
                .build());

        countryCodeRepository.save(CountryCode.builder()
                .countryName("BELGIUM")
                .countryNameTr("BELÇİKA")
                .countryCodeIso("BE")
                .countryCodeNumeric("32")
                .isActive(true)
                .build());

        countryCodeRepository.save(CountryCode.builder()
                .countryName("AUSTRIA")
                .countryNameTr("AVUSTURYA")
                .countryCodeIso("AT")
                .countryCodeNumeric("43")
                .isActive(true)
                .build());

        countryCodeRepository.save(CountryCode.builder()
                .countryName("SWITZERLAND")
                .countryNameTr("İSVİÇRE")
                .countryCodeIso("CH")
                .countryCodeNumeric("41")
                .isActive(true)
                .build());

        countryCodeRepository.save(CountryCode.builder()
                .countryName("POLAND")
                .countryNameTr("POLONYA")
                .countryCodeIso("PL")
                .countryCodeNumeric("48")
                .isActive(true)
                .build());

        countryCodeRepository.save(CountryCode.builder()
                .countryName("CZECH REPUBLIC")
                .countryNameTr("ÇEK CUMHURİYETİ")
                .countryCodeIso("CZ")
                .countryCodeNumeric("42")
                .isActive(true)
                .build());

        countryCodeRepository.save(CountryCode.builder()
                .countryName("HUNGARY")
                .countryNameTr("MACARİSTAN")
                .countryCodeIso("HU")
                .countryCodeNumeric("36")
                .isActive(true)
                .build());

        countryCodeRepository.save(CountryCode.builder()
                .countryName("ROMANIA")
                .countryNameTr("ROMANYA")
                .countryCodeIso("RO")
                .countryCodeNumeric("40")
                .isActive(true)
                .build());

        countryCodeRepository.save(CountryCode.builder()
                .countryName("BULGARIA")
                .countryNameTr("BULGARİSTAN")
                .countryCodeIso("BG")
                .countryCodeNumeric("35")
                .isActive(true)
                .build());

        countryCodeRepository.save(CountryCode.builder()
                .countryName("GREECE")
                .countryNameTr("YUNANİSTAN")
                .countryCodeIso("GR")
                .countryCodeNumeric("30")
                .isActive(true)
                .build());

        // Varsayılan ülke kodu
        countryCodeRepository.save(CountryCode.builder()
                .countryName("OTHER")
                .countryNameTr("DİĞER")
                .countryCodeIso("XX")
                .countryCodeNumeric("00")
                .isActive(true)
                .build());
    }
}
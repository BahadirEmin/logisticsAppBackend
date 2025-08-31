package com.baem.logisticapp.config;

import com.baem.logisticapp.entity.*;
import com.baem.logisticapp.repository.*;
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
    private final PasswordEncoder passwordEncoder;
    // Alert test verileri için yeni repository'ler
    private final DriverRepository driverRepository;
    private final VehicleRepository vehicleRepository;
    
    public DataInitializer(UserRepository userRepository,
                          CustomerRiskStatusRepository customerRiskStatusRepository,
                          VehicleOwnershipTypeRepository vehicleOwnershipTypeRepository,
                          PasswordEncoder passwordEncoder,
                          DriverRepository driverRepository,
                          VehicleRepository vehicleRepository) {
        this.userRepository = userRepository;
        this.customerRiskStatusRepository = customerRiskStatusRepository;
        this.vehicleOwnershipTypeRepository = vehicleOwnershipTypeRepository;
        this.passwordEncoder = passwordEncoder;
        this.driverRepository = driverRepository;
        this.vehicleRepository = vehicleRepository;
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
            if (driverRepository.count() == 0) {
                initTestDrivers();
            }
            if (vehicleRepository.count() == 0) {
                initTestVehicles();
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
    


    private void initTestDrivers() {
        // Alert test için visa'sı yakında bitecek sürücü
        driverRepository.save(Driver.builder()
                .firstName("Ahmet")
                .lastName("Yılmaz")
                .licenseNo("34-123456")
                .licenseClass("E")
                .passportExpiry(LocalDate.of(2026, 3, 15)) // License proxy
                .visaExpiry(LocalDate.of(2025, 10, 15)) // Yakında bitecek visa
                .residencePermitExpiry(LocalDate.of(2026, 6, 20))
                .phoneNumber("0543-123-4567")
                .email("ahmet.yilmaz@logistics.com")
                .isActive(true)
                .build());

        // License'ı yakında bitecek sürücü
        driverRepository.save(Driver.builder()
                .firstName("Mehmet")
                .lastName("Demir")
                .licenseNo("34-789012")
                .licenseClass("E")
                .passportExpiry(LocalDate.of(2025, 11, 30)) // Yakında bitecek license
                .visaExpiry(LocalDate.of(2026, 5, 10))
                .residencePermitExpiry(LocalDate.of(2026, 8, 15))
                .phoneNumber("0543-765-4321")
                .email("mehmet.demir@logistics.com")
                .isActive(true)
                .build());

        // Normal sürücü
        driverRepository.save(Driver.builder()
                .firstName("Ali")
                .lastName("Kaya")
                .licenseNo("34-345678")
                .licenseClass("E")
                .passportExpiry(LocalDate.of(2027, 2, 10))
                .visaExpiry(LocalDate.of(2027, 1, 20))
                .residencePermitExpiry(LocalDate.of(2027, 4, 30))
                .phoneNumber("0543-999-8888")
                .email("ali.kaya@logistics.com")
                .isActive(true)
                .build());
    }

    private void initTestVehicles() {
        // Muayenesi yakında bitecek araç
        vehicleRepository.save(Vehicle.builder()
                .plateNo("34ABC34")
                .make("Mercedes")
                .model("Actros")
                .modelYear((short) 2020)
                .purchaseDate(LocalDate.of(2020, 6, 15))
                .vin("WDAH1234567890123")
                .inspectionExpiryDate(LocalDate.of(2025, 9, 20)) // Yakında bitecek muayene
                .insuranceExpiryDate(LocalDate.of(2026, 1, 15))
                .isActive(true)
                .build());

        // Sigortası yakında bitecek araç
        vehicleRepository.save(Vehicle.builder()
                .plateNo("34XYZ34")
                .make("Volvo")
                .model("FH16")
                .modelYear((short) 2019)
                .purchaseDate(LocalDate.of(2019, 8, 20))
                .vin("YV2A1234567890123")
                .inspectionExpiryDate(LocalDate.of(2026, 3, 10))
                .insuranceExpiryDate(LocalDate.of(2025, 12, 1)) // Yakında bitecek sigorta
                .isActive(true)
                .build());

        // Normal araç
        vehicleRepository.save(Vehicle.builder()
                .plateNo("34DEF34")
                .make("Scania")
                .model("R450")
                .modelYear((short) 2021)
                .purchaseDate(LocalDate.of(2021, 4, 10))
                .vin("YS2P1234567890123")
                .inspectionExpiryDate(LocalDate.of(2027, 2, 15))
                .insuranceExpiryDate(LocalDate.of(2027, 3, 20))
                .isActive(true)
                .build());
    }
}
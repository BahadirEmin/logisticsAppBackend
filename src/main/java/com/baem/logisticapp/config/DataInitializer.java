package com.baem.logisticapp.config;

import com.baem.logisticapp.entity.CustomerRiskStatus;
import com.baem.logisticapp.entity.Role;
import com.baem.logisticapp.entity.User;
import com.baem.logisticapp.entity.VehicleOwnershipType;
import com.baem.logisticapp.repository.CustomerRiskStatusRepository;
import com.baem.logisticapp.repository.UserRepository;
import com.baem.logisticapp.repository.VehicleOwnershipTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {
    private final UserRepository userRepository;
    private final CustomerRiskStatusRepository customerRiskStatusRepository;
    private final VehicleOwnershipTypeRepository vehicleOwnershipTypeRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner initData() {
        return args -> {
            initUsers();
            initRiskStatuses();
            initVehicleOwnershipTypes();
        };
    }

    private void initUsers() {
        if (userRepository.findByUsername("sales").isEmpty()) {
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
        }
        if (userRepository.findByUsername("operation").isEmpty()) {
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
        }
        if (userRepository.findByUsername("fleet").isEmpty()) {
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
        }
        if (userRepository.findByUsername("admin").isEmpty()) {
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
    }

    private void initRiskStatuses() {
        if (customerRiskStatusRepository.count() == 0) {
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
    }

    private void initVehicleOwnershipTypes() {
        if (vehicleOwnershipTypeRepository.count() == 0) {
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
    }
}
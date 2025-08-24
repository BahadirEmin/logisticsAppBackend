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
                    .name("Satışçı User")
                    .email("sales@logistics.com")
                    .password(passwordEncoder.encode("sales123"))
                    .role(Role.SALES)
                    .build());
        }
        if (userRepository.findByUsername("operation").isEmpty()) {
            userRepository.save(User.builder()
                    .username("operation")
                    .name("Operasyon User")
                    .email("operation@logistics.com")
                    .password(passwordEncoder.encode("operation123"))
                    .role(Role.OPERATION)
                    .build());
        }
        if (userRepository.findByUsername("fleet").isEmpty()) {
            userRepository.save(User.builder()
                    .username("fleet")
                    .name("Filo Yöneticisi")
                    .email("fleet@logistics.com")
                    .password(passwordEncoder.encode("fleet123"))
                    .role(Role.FLEET)
                    .build());
        }
        if (userRepository.findByUsername("admin").isEmpty()) {
            userRepository.save(User.builder()
                    .username("admin")
                    .name("Admin User")
                    .email("admin@logistics.com")
                    .password(passwordEncoder.encode("admin123"))
                    .role(Role.ADMIN)
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
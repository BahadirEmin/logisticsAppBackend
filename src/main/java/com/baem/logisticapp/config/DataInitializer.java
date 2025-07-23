package com.baem.logisticapp.config;

import com.baem.logisticapp.entity.Role;
import com.baem.logisticapp.entity.User;
import com.baem.logisticapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner initUsers() {
        return args -> {
            if (userRepository.findByUsername("sales").isEmpty()) {
                userRepository.save(User.builder()
                        .username("sales")
                        .password(passwordEncoder.encode("sales123"))
                        .role(Role.SALES)
                        .build());
            }
            if (userRepository.findByUsername("operation").isEmpty()) {
                userRepository.save(User.builder()
                        .username("operation")
                        .password(passwordEncoder.encode("operation123"))
                        .role(Role.OPERATION)
                        .build());
            }
            if (userRepository.findByUsername("admin").isEmpty()) {
                userRepository.save(User.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin123"))
                        .role(Role.ADMIN)
                        .build());
            }
        };
    }
}
package com.baem.logisticapp.controller;

import com.baem.logisticapp.dto.AuthRequest;
import com.baem.logisticapp.dto.AuthResponse;
import com.baem.logisticapp.dto.UserResponseDTO;
import com.baem.logisticapp.entity.User;
import com.baem.logisticapp.repository.UserRepository;
import com.baem.logisticapp.security.JwtUtil;
import com.baem.logisticapp.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;
    private final UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        try {
            System.out.println("Login attempt for username: " + request.getUsername());

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

            System.out.println("Authentication successful for: " + request.getUsername());

            UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
            String role = userDetails.getAuthorities().stream().findFirst().get().getAuthority().replace("ROLE_", "");
            String token = jwtUtil.generateToken(userDetails.getUsername(), role);

            System.out.println("JWT token generated for: " + request.getUsername());

            // User bilgilerini al
            User user = userRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            System.out.println("User found in database: " + user.getUsername());

            UserResponseDTO userResponse = UserResponseDTO.builder()
                    .id(user.getId())
                    .username(user.getUsername())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .department(user.getDepartment())
                    .phone(user.getPhone())
                    .hireDate(user.getHireDate())
                    .isActive(user.getIsActive())
                    .canApproveQuotes(user.getCanApproveQuotes())
                    .email(user.getEmail())
                    .role(user.getRole())
                    .build();

            AuthResponse authResponse = AuthResponse.builder()
                    .token(token)
                    .user(userResponse)
                    .build();

            System.out.println("Login successful for: " + request.getUsername());
            return ResponseEntity.ok(authResponse);
        } catch (BadCredentialsException e) {
            System.out.println("Bad credentials for: " + request.getUsername());
            return ResponseEntity.status(401).build();
        } catch (Exception e) {
            System.out.println("Login error for: " + request.getUsername() + " - " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
}
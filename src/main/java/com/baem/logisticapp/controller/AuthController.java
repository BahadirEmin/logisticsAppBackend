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
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
            UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
            String role = userDetails.getAuthorities().stream().findFirst().get().getAuthority().replace("ROLE_", "");
            String token = jwtUtil.generateToken(userDetails.getUsername(), role);

            // User bilgilerini al
            User user = userRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            UserResponseDTO userResponse = UserResponseDTO.builder()
                    .id(user.getId())
                    .username(user.getUsername())
                    .name(user.getFullName())
                    .email(user.getEmail())
                    .role(user.getRole())
                    .build();

            AuthResponse authResponse = AuthResponse.builder()
                    .token(token)
                    .user(userResponse)
                    .build();

            return ResponseEntity.ok(authResponse);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).build();
        }
    }
}
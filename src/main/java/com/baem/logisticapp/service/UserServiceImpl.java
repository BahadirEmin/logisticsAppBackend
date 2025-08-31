package com.baem.logisticapp.service;

import com.baem.logisticapp.dto.UserCreateDTO;
import com.baem.logisticapp.dto.UserResponseDTO;
import com.baem.logisticapp.dto.UserUpdateDTO;
import com.baem.logisticapp.entity.Role;
import com.baem.logisticapp.entity.User;
import com.baem.logisticapp.exception.ResourceNotFoundException;
import com.baem.logisticapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponseDTO createUser(UserCreateDTO userCreateDTO) {
        log.info("Creating new user with username: {}", userCreateDTO.getUsername());

        // Check if username already exists
        if (userRepository.findByUsername(userCreateDTO.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already exists: " + userCreateDTO.getUsername());
        }

        // Check if email already exists (if provided)
        if (userCreateDTO.getEmail() != null && !userCreateDTO.getEmail().isEmpty()) {
            if (userRepository.findByEmail(userCreateDTO.getEmail()).isPresent()) {
                throw new IllegalArgumentException("Email already exists: " + userCreateDTO.getEmail());
            }
        }

        User user = User.builder()
                .username(userCreateDTO.getUsername())
                .firstName(userCreateDTO.getFirstName())
                .lastName(userCreateDTO.getLastName())
                .department(userCreateDTO.getDepartment())
                .phone(userCreateDTO.getPhone())
                .hireDate(userCreateDTO.getHireDate())
                .isActive(userCreateDTO.getIsActive())
                .canApproveQuotes(userCreateDTO.getCanApproveQuotes())
                .email(userCreateDTO.getEmail())
                .password(passwordEncoder.encode(userCreateDTO.getPassword()))
                .role(userCreateDTO.getRole())
                .build();

        User savedUser = userRepository.save(user);
        log.info("User created successfully with ID: {}", savedUser.getId());

        return mapToResponseDTO(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDTO getUserById(Long id) {
        log.info("Fetching user by ID: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return mapToResponseDTO(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDTO> getAllUsers() {
        log.info("Fetching all users");
        return userRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserResponseDTO> getAllUsersPaginated(Pageable pageable) {
        log.info("Fetching users with pagination: page={}, size={}", pageable.getPageNumber(), pageable.getPageSize());
        return userRepository.findAll(pageable)
                .map(this::mapToResponseDTO);
    }

    @Override
    public UserResponseDTO updateUser(Long id, UserUpdateDTO userUpdateDTO) {
        log.info("Updating user with ID: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        // Check if username is being changed and if it already exists
        if (userUpdateDTO.getUsername() != null && !userUpdateDTO.getUsername().equals(user.getUsername())) {
            if (userRepository.findByUsername(userUpdateDTO.getUsername()).isPresent()) {
                throw new IllegalArgumentException("Username already exists: " + userUpdateDTO.getUsername());
            }
        }

        // Check if email is being changed and if it already exists
        if (userUpdateDTO.getEmail() != null && !userUpdateDTO.getEmail().equals(user.getEmail())) {
            if (userRepository.findByEmail(userUpdateDTO.getEmail()).isPresent()) {
                throw new IllegalArgumentException("Email already exists: " + userUpdateDTO.getEmail());
            }
        }

        // Update fields if provided
        if (userUpdateDTO.getUsername() != null) {
            user.setUsername(userUpdateDTO.getUsername());
        }
        if (userUpdateDTO.getFirstName() != null) {
            user.setFirstName(userUpdateDTO.getFirstName());
        }
        if (userUpdateDTO.getLastName() != null) {
            user.setLastName(userUpdateDTO.getLastName());
        }
        if (userUpdateDTO.getDepartment() != null) {
            user.setDepartment(userUpdateDTO.getDepartment());
        }
        if (userUpdateDTO.getPhone() != null) {
            user.setPhone(userUpdateDTO.getPhone());
        }
        if (userUpdateDTO.getHireDate() != null) {
            user.setHireDate(userUpdateDTO.getHireDate());
        }
        if (userUpdateDTO.getEmail() != null) {
            user.setEmail(userUpdateDTO.getEmail());
        }
        if (userUpdateDTO.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(userUpdateDTO.getPassword()));
        }
        if (userUpdateDTO.getRole() != null) {
            user.setRole(userUpdateDTO.getRole());
        }
        if (userUpdateDTO.getIsActive() != null) {
            user.setIsActive(userUpdateDTO.getIsActive());
        }
        if (userUpdateDTO.getCanApproveQuotes() != null) {
            user.setCanApproveQuotes(userUpdateDTO.getCanApproveQuotes());
        }

        User updatedUser = userRepository.save(user);
        log.info("User updated successfully with ID: {}", updatedUser.getId());

        return mapToResponseDTO(updatedUser);
    }

    @Override
    public void deleteUser(Long id) {
        log.info("Deleting user with ID: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        // Soft delete by setting isActive to false
        user.setIsActive(false);
        userRepository.save(user);

        log.info("User soft deleted successfully with ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDTO> searchUsers(String department, Role role, Boolean isActive, String name) {
        log.info("Searching users with filters - department: {}, role: {}, isActive: {}, name: {}",
                department, role, isActive, name);

        List<User> users;

        if (department != null && role != null && isActive != null) {
            users = userRepository.findByDepartmentAndIsActive(department, isActive);
            users = users.stream()
                    .filter(user -> user.getRole() == role)
                    .collect(Collectors.toList());
        } else if (department != null && isActive != null) {
            users = userRepository.findByDepartmentAndIsActive(department, isActive);
        } else if (role != null && isActive != null) {
            users = userRepository.findByRoleAndIsActive(role, isActive);
        } else if (department != null) {
            users = userRepository.findByDepartment(department);
        } else if (role != null) {
            users = userRepository.findByRole(role);
        } else if (isActive != null) {
            users = userRepository.findByIsActive(isActive);
        } else if (name != null && !name.trim().isEmpty()) {
            users = userRepository.findByNameContaining(name.trim());
        } else {
            users = userRepository.findAll();
        }

        return users.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDTO> getUsersByDepartment(String department) {
        log.info("Fetching users by department: {}", department);
        return userRepository.findByDepartment(department).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDTO> getUsersByRole(Role role) {
        log.info("Fetching users by role: {}", role);
        return userRepository.findByRole(role).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDTO> getActiveUsers() {
        log.info("Fetching active users");
        return userRepository.findByIsActive(true).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByUsername(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    private UserResponseDTO mapToResponseDTO(User user) {
        return UserResponseDTO.builder()
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
    }
}

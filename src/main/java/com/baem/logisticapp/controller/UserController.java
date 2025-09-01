package com.baem.logisticapp.controller;

import com.baem.logisticapp.dto.UserCreateDTO;
import com.baem.logisticapp.dto.UserResponseDTO;
import com.baem.logisticapp.dto.UserUpdateDTO;
import com.baem.logisticapp.entity.Role;
import com.baem.logisticapp.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "User Management", description = "APIs for managing users")
public class UserController {

        private final UserService userService;

        @PostMapping
        @Operation(summary = "Create a new user", description = "Creates a new user with the provided information")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "User created successfully", content = @Content(schema = @Schema(implementation = UserResponseDTO.class))),
                        @ApiResponse(responseCode = "400", description = "Invalid input data"),
                        @ApiResponse(responseCode = "409", description = "Username or email already exists")
        })
        public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserCreateDTO userCreateDTO) {
                log.info("Creating new user with username: {}", userCreateDTO.getUsername());
                UserResponseDTO createdUser = userService.createUser(userCreateDTO);
                return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
        }

        @GetMapping("/{id}")
        @Operation(summary = "Get user by ID", description = "Retrieves a user by their ID")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "User found", content = @Content(schema = @Schema(implementation = UserResponseDTO.class))),
                        @ApiResponse(responseCode = "404", description = "User not found")
        })
        public ResponseEntity<UserResponseDTO> getUserById(@Parameter(description = "User ID") @PathVariable Long id) {
                log.info("Fetching user by ID: {}", id);
                UserResponseDTO user = userService.getUserById(id);
                return ResponseEntity.ok(user);
        }

        @GetMapping
        @Operation(summary = "Get all users", description = "Retrieves all users in the system")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Users retrieved successfully", content = @Content(schema = @Schema(implementation = UserResponseDTO.class)))
        })
        public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
                log.info("Fetching all users");
                List<UserResponseDTO> users = userService.getAllUsers();
                return ResponseEntity.ok(users);
        }

        @GetMapping("/paginated")
        @Operation(summary = "Get all users with pagination", description = "Retrieves users with pagination support")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Users retrieved successfully", content = @Content(schema = @Schema(implementation = UserResponseDTO.class)))
        })
        public ResponseEntity<Page<UserResponseDTO>> getAllUsersPaginated(
                        @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
                        @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
                        @Parameter(description = "Sort field") @RequestParam(defaultValue = "id") String sortBy,
                        @Parameter(description = "Sort direction (asc/desc)") @RequestParam(defaultValue = "asc") String sortDir) {

                log.info("Fetching users with pagination - page: {}, size: {}, sortBy: {}, sortDir: {}",
                                page, size, sortBy, sortDir);

                Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending()
                                : Sort.by(sortBy).ascending();

                Pageable pageable = PageRequest.of(page, size, sort);
                Page<UserResponseDTO> users = userService.getAllUsersPaginated(pageable);

                return ResponseEntity.ok(users);
        }

        @PutMapping("/{id}")
        @Operation(summary = "Update user", description = "Updates an existing user's information")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "User updated successfully", content = @Content(schema = @Schema(implementation = UserResponseDTO.class))),
                        @ApiResponse(responseCode = "404", description = "User not found"),
                        @ApiResponse(responseCode = "400", description = "Invalid input data"),
                        @ApiResponse(responseCode = "409", description = "Username or email already exists")
        })
        public ResponseEntity<UserResponseDTO> updateUser(
                        @Parameter(description = "User ID") @PathVariable Long id,
                        @Valid @RequestBody UserUpdateDTO userUpdateDTO) {
                log.info("Updating user with ID: {}", id);
                UserResponseDTO updatedUser = userService.updateUser(id, userUpdateDTO);
                return ResponseEntity.ok(updatedUser);
        }

        @DeleteMapping("/{id}")
        @Operation(summary = "Delete user", description = "Soft deletes a user by setting isActive to false")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "204", description = "User deleted successfully"),
                        @ApiResponse(responseCode = "404", description = "User not found")
        })
        public ResponseEntity<Void> deleteUser(@Parameter(description = "User ID") @PathVariable Long id) {
                log.info("Deleting user with ID: {}", id);
                userService.deleteUser(id);
                return ResponseEntity.noContent().build();
        }

        @GetMapping("/search")
        @Operation(summary = "Search users", description = "Search users with various filters")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Users found", content = @Content(schema = @Schema(implementation = UserResponseDTO.class)))
        })
        public ResponseEntity<List<UserResponseDTO>> searchUsers(
                        @Parameter(description = "Department filter") @RequestParam(required = false) String department,
                        @Parameter(description = "Role filter") @RequestParam(required = false) Role role,
                        @Parameter(description = "Active status filter") @RequestParam(required = false) Boolean isActive,
                        @Parameter(description = "Name search (searches first and last name)") @RequestParam(required = false) String name) {

                log.info("Searching users with filters - department: {}, role: {}, isActive: {}, name: {}",
                                department, role, isActive, name);

                List<UserResponseDTO> users = userService.searchUsers(department, role, isActive, name);
                return ResponseEntity.ok(users);
        }

        @GetMapping("/department/{department}")
        @Operation(summary = "Get users by department", description = "Retrieves all users in a specific department")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Users found", content = @Content(schema = @Schema(implementation = UserResponseDTO.class)))
        })
        public ResponseEntity<List<UserResponseDTO>> getUsersByDepartment(
                        @Parameter(description = "Department name") @PathVariable String department) {
                log.info("Fetching users by department: {}", department);
                List<UserResponseDTO> users = userService.getUsersByDepartment(department);
                return ResponseEntity.ok(users);
        }

        @GetMapping("/role/{role}")
        @Operation(summary = "Get users by role", description = "Retrieves all users with a specific role")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Users found", content = @Content(schema = @Schema(implementation = UserResponseDTO.class)))
        })
        public ResponseEntity<List<UserResponseDTO>> getUsersByRole(
                        @Parameter(description = "User role") @PathVariable Role role) {
                log.info("Fetching users by role: {}", role);
                List<UserResponseDTO> users = userService.getUsersByRole(role);
                return ResponseEntity.ok(users);
        }

        @GetMapping("/active")
        @Operation(summary = "Get active users", description = "Retrieves all active users")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Active users found", content = @Content(schema = @Schema(implementation = UserResponseDTO.class)))
        })
        public ResponseEntity<List<UserResponseDTO>> getActiveUsers() {
                log.info("Fetching active users");
                List<UserResponseDTO> users = userService.getActiveUsers();
                return ResponseEntity.ok(users);
        }

        @GetMapping("/check-username")
        @Operation(summary = "Check username exists", description = "Checks if a username already exists in the system")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Check completed", content = @Content(schema = @Schema(implementation = Boolean.class)))
        })
        public ResponseEntity<Boolean> checkUsernameExists(
                        @Parameter(description = "Username to check") @RequestParam String username) {
                log.info("Checking if username exists: {}", username);
                boolean exists = userService.existsByUsername(username);
                return ResponseEntity.ok(exists);
        }

        @GetMapping("/check-email")
        @Operation(summary = "Check email exists", description = "Checks if an email already exists in the system")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Check completed", content = @Content(schema = @Schema(implementation = Boolean.class)))
        })
        public ResponseEntity<Boolean> checkEmailExists(
                        @Parameter(description = "Email to check") @RequestParam String email) {
                log.info("Checking if email exists: {}", email);
                boolean exists = userService.existsByEmail(email);
                return ResponseEntity.ok(exists);
        }
}

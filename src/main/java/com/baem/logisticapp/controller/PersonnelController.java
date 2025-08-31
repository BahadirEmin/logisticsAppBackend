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
@RequestMapping("/api/v1/personnel")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Personnel Management", description = "APIs for managing personnel")
public class PersonnelController {

    private final UserService userService;

    @PostMapping
    @Operation(summary = "Create a new personnel", description = "Creates a new personnel with the provided information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Personnel created successfully", content = @Content(schema = @Schema(implementation = UserResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "409", description = "Username or email already exists")
    })
    public ResponseEntity<UserResponseDTO> createPersonnel(@Valid @RequestBody UserCreateDTO userCreateDTO) {
        log.info("Creating new personnel with username: {}", userCreateDTO.getUsername());
        UserResponseDTO createdPersonnel = userService.createUser(userCreateDTO);
        return new ResponseEntity<>(createdPersonnel, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get personnel by ID", description = "Retrieves a personnel by their ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Personnel found", content = @Content(schema = @Schema(implementation = UserResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Personnel not found")
    })
    public ResponseEntity<UserResponseDTO> getPersonnelById(
            @Parameter(description = "Personnel ID") @PathVariable Long id) {
        log.info("Fetching personnel by ID: {}", id);
        UserResponseDTO personnel = userService.getUserById(id);
        return ResponseEntity.ok(personnel);
    }

    @GetMapping
    @Operation(summary = "Get all personnel", description = "Retrieves all personnel in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Personnel retrieved successfully", content = @Content(schema = @Schema(implementation = UserResponseDTO.class)))
    })
    public ResponseEntity<List<UserResponseDTO>> getAllPersonnel() {
        log.info("Fetching all personnel");
        List<UserResponseDTO> personnel = userService.getAllUsers();
        return ResponseEntity.ok(personnel);
    }

    @GetMapping("/paginated")
    @Operation(summary = "Get all personnel with pagination", description = "Retrieves personnel with pagination support")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Personnel retrieved successfully", content = @Content(schema = @Schema(implementation = UserResponseDTO.class)))
    })
    public ResponseEntity<Page<UserResponseDTO>> getAllPersonnelPaginated(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort field") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction (asc/desc)") @RequestParam(defaultValue = "asc") String sortDir) {

        log.info("Fetching personnel with pagination - page: {}, size: {}, sortBy: {}, sortDir: {}",
                page, size, sortBy, sortDir);

        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<UserResponseDTO> personnel = userService.getAllUsersPaginated(pageable);

        return ResponseEntity.ok(personnel);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update personnel", description = "Updates an existing personnel's information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Personnel updated successfully", content = @Content(schema = @Schema(implementation = UserResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Personnel not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "409", description = "Username or email already exists")
    })
    public ResponseEntity<UserResponseDTO> updatePersonnel(
            @Parameter(description = "Personnel ID") @PathVariable Long id,
            @Valid @RequestBody UserUpdateDTO userUpdateDTO) {
        log.info("Updating personnel with ID: {}", id);
        UserResponseDTO updatedPersonnel = userService.updateUser(id, userUpdateDTO);
        return ResponseEntity.ok(updatedPersonnel);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete personnel", description = "Soft deletes a personnel by setting isActive to false")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Personnel deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Personnel not found")
    })
    public ResponseEntity<Void> deletePersonnel(@Parameter(description = "Personnel ID") @PathVariable Long id) {
        log.info("Deleting personnel with ID: {}", id);
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    @Operation(summary = "Search personnel", description = "Search personnel with various filters")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Personnel found", content = @Content(schema = @Schema(implementation = UserResponseDTO.class)))
    })
    public ResponseEntity<List<UserResponseDTO>> searchPersonnel(
            @Parameter(description = "Department filter") @RequestParam(required = false) String department,
            @Parameter(description = "Role filter") @RequestParam(required = false) Role role,
            @Parameter(description = "Active status filter") @RequestParam(required = false) Boolean isActive,
            @Parameter(description = "Name search (searches first and last name)") @RequestParam(required = false) String name) {

        log.info("Searching personnel with filters - department: {}, role: {}, isActive: {}, name: {}",
                department, role, isActive, name);

        List<UserResponseDTO> personnel = userService.searchUsers(department, role, isActive, name);
        return ResponseEntity.ok(personnel);
    }

    @GetMapping("/department/{department}")
    @Operation(summary = "Get personnel by department", description = "Retrieves all personnel in a specific department")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Personnel found", content = @Content(schema = @Schema(implementation = UserResponseDTO.class)))
    })
    public ResponseEntity<List<UserResponseDTO>> getPersonnelByDepartment(
            @Parameter(description = "Department name") @PathVariable String department) {
        log.info("Fetching personnel by department: {}", department);
        List<UserResponseDTO> personnel = userService.getUsersByDepartment(department);
        return ResponseEntity.ok(personnel);
    }

    @GetMapping("/role/{role}")
    @Operation(summary = "Get personnel by role", description = "Retrieves all personnel with a specific role")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Personnel found", content = @Content(schema = @Schema(implementation = UserResponseDTO.class)))
    })
    public ResponseEntity<List<UserResponseDTO>> getPersonnelByRole(
            @Parameter(description = "Personnel role") @PathVariable Role role) {
        log.info("Fetching personnel by role: {}", role);
        List<UserResponseDTO> personnel = userService.getUsersByRole(role);
        return ResponseEntity.ok(personnel);
    }

    @GetMapping("/active")
    @Operation(summary = "Get active personnel", description = "Retrieves all active personnel")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Active personnel found", content = @Content(schema = @Schema(implementation = UserResponseDTO.class)))
    })
    public ResponseEntity<List<UserResponseDTO>> getActivePersonnel() {
        log.info("Fetching active personnel");
        List<UserResponseDTO> personnel = userService.getActiveUsers();
        return ResponseEntity.ok(personnel);
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

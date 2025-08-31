package com.baem.logisticapp.service;

import com.baem.logisticapp.dto.UserCreateDTO;
import com.baem.logisticapp.dto.UserResponseDTO;
import com.baem.logisticapp.dto.UserUpdateDTO;
import com.baem.logisticapp.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {

    UserResponseDTO createUser(UserCreateDTO userCreateDTO);

    UserResponseDTO getUserById(Long id);

    List<UserResponseDTO> getAllUsers();

    Page<UserResponseDTO> getAllUsersPaginated(Pageable pageable);

    UserResponseDTO updateUser(Long id, UserUpdateDTO userUpdateDTO);

    void deleteUser(Long id);

    List<UserResponseDTO> searchUsers(String department, Role role, Boolean isActive, String name);

    List<UserResponseDTO> getUsersByDepartment(String department);

    List<UserResponseDTO> getUsersByRole(Role role);

    List<UserResponseDTO> getActiveUsers();

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}

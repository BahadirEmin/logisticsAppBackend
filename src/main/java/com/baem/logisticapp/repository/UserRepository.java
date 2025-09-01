package com.baem.logisticapp.repository;

import com.baem.logisticapp.entity.Role;
import com.baem.logisticapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    // Yeni eklenen alanlarla arama metodları
    List<User> findByDepartment(String department);

    List<User> findByRole(Role role);

    List<User> findByIsActive(Boolean isActive);

    List<User> findByDepartmentAndIsActive(String department, Boolean isActive);

    List<User> findByRoleAndIsActive(Role role, Boolean isActive);

    Optional<User> findByPhone(String phone);

    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.firstName LIKE %:name% OR u.lastName LIKE %:name%")
    List<User> findByNameContaining(@Param("name") String name);

    @Query("SELECT u FROM User u WHERE u.isActive = true ORDER BY u.hireDate DESC")
    List<User> findActiveUsersOrderByHireDate();
}
package com.company.logistics.logistics_backend.repository;

import com.company.logistics.logistics_backend.entity.TestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestRepository extends JpaRepository<TestEntity, Long> {
}
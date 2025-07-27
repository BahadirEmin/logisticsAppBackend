package com.baem.logisticapp.repository;

import com.baem.logisticapp.entity.TestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestRepository extends JpaRepository<TestEntity, Long> {
}
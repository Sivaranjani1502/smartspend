package com.smartspend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.smartspend.model.TestEntity;

public interface TestRepository extends JpaRepository<TestEntity, Long> {
}

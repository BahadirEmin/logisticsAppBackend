package com.baem.logisticapp.controller;

import com.baem.logisticapp.entity.TestEntity;
import com.baem.logisticapp.repository.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @Autowired
    private TestRepository testRepository;

    @PostMapping
    public ResponseEntity<TestEntity> createTest(@RequestBody TestEntity testEntity) {
        return ResponseEntity.ok(testRepository.save(testEntity));
    }

    @GetMapping
    public ResponseEntity<List<TestEntity>> getAllTests() {
        return ResponseEntity.ok(testRepository.findAll());
    }
}
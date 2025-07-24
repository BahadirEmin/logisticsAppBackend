package com.baem.logisticapp.controller;

import com.baem.logisticapp.dto.PersonelCreateDTO;
import com.baem.logisticapp.dto.PersonelResponseDTO;
import com.baem.logisticapp.dto.PersonelUpdateDTO;
import com.baem.logisticapp.service.PersonalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/personel")
@RequiredArgsConstructor
public class PersonelController {

    private final PersonalService personelService;

    @PostMapping
    public ResponseEntity<PersonelResponseDTO> createPersonel(@Valid @RequestBody PersonelCreateDTO personelCreateDTO) {
        return new ResponseEntity<>(personelService.createPersonel(personelCreateDTO), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonelResponseDTO> getPersonelById(@PathVariable Long id) {
        return ResponseEntity.ok(personelService.getPersonelById(id));
    }

    @GetMapping
    public ResponseEntity<List<PersonelResponseDTO>> getAllPersonel() {
        return ResponseEntity.ok(personelService.getAllPersonel());
    }

    @PutMapping("/{id}")
    public ResponseEntity<PersonelResponseDTO> updatePersonel(
            @PathVariable Long id,
            @Valid @RequestBody PersonelUpdateDTO personelUpdateDTO) {
        return ResponseEntity.ok(personelService.updatePersonel(id, personelUpdateDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePersonel(@PathVariable Long id) {
        personelService.deletePersonel(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/department/{department}")
    public ResponseEntity<List<PersonelResponseDTO>> getPersonelByDepartment(@PathVariable String department) {
        return ResponseEntity.ok(personelService.getPersonelByDepartment(department));
    }

    @GetMapping("/role/{role}")
    public ResponseEntity<List<PersonelResponseDTO>> getPersonelByRole(@PathVariable String role) {
        return ResponseEntity.ok(personelService.getPersonelByRole(role));
    }

    @GetMapping("/active")
    public ResponseEntity<List<PersonelResponseDTO>> getActivePersonel() {
        return ResponseEntity.ok(personelService.getActivePersonel());
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<PersonelResponseDTO> getPersonelByEmail(@PathVariable String email) {
        return ResponseEntity.ok(personelService.getPersonelByEmail(email));
    }

    @GetMapping("/phone/{phone}")
    public ResponseEntity<PersonelResponseDTO> getPersonelByPhone(@PathVariable String phone) {
        return ResponseEntity.ok(personelService.getPersonelByPhone(phone));
    }

    @GetMapping("/search")
    public ResponseEntity<List<PersonelResponseDTO>> searchPersonelByName(@RequestParam String name) {
        return ResponseEntity.ok(personelService.searchPersonelByName(name));
    }

    @GetMapping("/department/{department}/role/{role}")
    public ResponseEntity<List<PersonelResponseDTO>> getPersonelByDepartmentAndRole(
            @PathVariable String department,
            @PathVariable String role) {
        return ResponseEntity.ok(personelService.getPersonelByDepartmentAndRole(department, role));
    }

    @GetMapping("/hired-after")
    public ResponseEntity<List<PersonelResponseDTO>> getPersonelHiredAfter(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(personelService.getPersonelHiredAfter(date));
    }

    @GetMapping("/active/hired-after")
    public ResponseEntity<List<PersonelResponseDTO>> getActivePersonelHiredAfter(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(personelService.getActivePersonelHiredAfter(date));
    }

    @GetMapping("/active/department/{department}")
    public ResponseEntity<List<PersonelResponseDTO>> getActivePersonelByDepartment(@PathVariable String department) {
        return ResponseEntity.ok(personelService.getActivePersonelByDepartment(department));
    }
}
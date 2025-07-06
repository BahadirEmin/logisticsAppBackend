package com.company.logistics.logistics_backend.service;

import com.company.logistics.logistics_backend.domain.dto.PersonelCreateDTO;
import com.company.logistics.logistics_backend.domain.dto.PersonelResponseDTO;
import com.company.logistics.logistics_backend.domain.dto.PersonelUpdateDTO;
import com.company.logistics.logistics_backend.domain.entity.Personel;
import com.company.logistics.logistics_backend.domain.repository.PersonelRepository;
import com.company.logistics.logistics_backend.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PersonelService {

    private final PersonelRepository personelRepository;

    public PersonelResponseDTO createPersonel(PersonelCreateDTO createDTO) {
        // Check if personnel with same email already exists
        if (personelRepository.existsByEmail(createDTO.getEmail())) {
            throw new IllegalArgumentException("Personnel with email " + createDTO.getEmail() + " already exists");
        }

        // Check if personnel with same phone number already exists
        if (personelRepository.existsByPhone(createDTO.getPhone())) {
            throw new IllegalArgumentException(
                    "Personnel with phone number " + createDTO.getPhone() + " already exists");
        }

        Personel personel = Personel.builder()
                .firstName(createDTO.getFirstName())
                .lastName(createDTO.getLastName())
                .role(createDTO.getRole())
                .department(createDTO.getDepartment())
                .hireDate(createDTO.getHireDate())
                .email(createDTO.getEmail())
                .phone(createDTO.getPhone())
                .isActive(true)
                .build();

        return convertToDTO(personelRepository.save(personel));
    }

    public PersonelResponseDTO getPersonelById(Long id) {
        return personelRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Personnel not found"));
    }

    public List<PersonelResponseDTO> getAllPersonel() {
        return personelRepository.findAll().stream()
                .map(this::convertToDTO)
                .toList();
    }

    public PersonelResponseDTO updatePersonel(Long id, PersonelUpdateDTO updateDTO) {
        Personel personel = personelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Personnel not found"));

        // Check if email is being changed and if it already exists
        if (!personel.getEmail().equals(updateDTO.getEmail()) &&
                personelRepository.existsByEmail(updateDTO.getEmail())) {
            throw new IllegalArgumentException("Personnel with email " + updateDTO.getEmail() + " already exists");
        }

        // Check if phone number is being changed and if it already exists
        if (!personel.getPhone().equals(updateDTO.getPhone()) &&
                personelRepository.existsByPhone(updateDTO.getPhone())) {
            throw new IllegalArgumentException(
                    "Personnel with phone number " + updateDTO.getPhone() + " already exists");
        }

        personel.setFirstName(updateDTO.getFirstName());
        personel.setLastName(updateDTO.getLastName());
        personel.setRole(updateDTO.getRole());
        personel.setDepartment(updateDTO.getDepartment());
        personel.setHireDate(updateDTO.getHireDate());
        personel.setEmail(updateDTO.getEmail());
        personel.setPhone(updateDTO.getPhone());
        personel.setIsActive(updateDTO.getIsActive() != null ? updateDTO.getIsActive() : true);

        return convertToDTO(personelRepository.save(personel));
    }

    public void deletePersonel(Long id) {
        if (!personelRepository.existsById(id)) {
            throw new ResourceNotFoundException("Personnel not found");
        }
        personelRepository.deleteById(id);
    }

    public List<PersonelResponseDTO> getPersonelByDepartment(String department) {
        return personelRepository.findByDepartment(department).stream()
                .map(this::convertToDTO)
                .toList();
    }

    public List<PersonelResponseDTO> getPersonelByRole(String role) {
        return personelRepository.findByRole(role).stream()
                .map(this::convertToDTO)
                .toList();
    }

    public List<PersonelResponseDTO> getActivePersonel() {
        return personelRepository.findByIsActiveTrue().stream()
                .map(this::convertToDTO)
                .toList();
    }

    public PersonelResponseDTO getPersonelByEmail(String email) {
        return personelRepository.findByEmail(email)
                .map(this::convertToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Personnel not found"));
    }

    public PersonelResponseDTO getPersonelByPhone(String phone) {
        return personelRepository.findByPhone(phone)
                .map(this::convertToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Personnel not found"));
    }

    public List<PersonelResponseDTO> searchPersonelByName(String name) {
        return personelRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(name, name).stream()
                .map(this::convertToDTO)
                .toList();
    }

    public List<PersonelResponseDTO> getPersonelByDepartmentAndRole(String department, String role) {
        return personelRepository.findByDepartmentAndRole(department, role).stream()
                .map(this::convertToDTO)
                .toList();
    }

    public List<PersonelResponseDTO> getPersonelHiredAfter(java.time.LocalDate date) {
        return personelRepository.findByHireDateAfter(date).stream()
                .map(this::convertToDTO)
                .toList();
    }

    public List<PersonelResponseDTO> getActivePersonelHiredAfter(java.time.LocalDate date) {
        return personelRepository.findByHireDateAfterAndIsActiveTrue(date).stream()
                .map(this::convertToDTO)
                .toList();
    }

    public List<PersonelResponseDTO> getActivePersonelByDepartment(String department) {
        return personelRepository.findByDepartmentAndIsActiveTrue(department).stream()
                .map(this::convertToDTO)
                .toList();
    }

    private PersonelResponseDTO convertToDTO(Personel personel) {
        return PersonelResponseDTO.builder()
                .id(personel.getId())
                .firstName(personel.getFirstName())
                .lastName(personel.getLastName())
                .role(personel.getRole())
                .department(personel.getDepartment())
                .hireDate(personel.getHireDate())
                .email(personel.getEmail())
                .phone(personel.getPhone())
                .isActive(personel.getIsActive())
                .build();
    }
}
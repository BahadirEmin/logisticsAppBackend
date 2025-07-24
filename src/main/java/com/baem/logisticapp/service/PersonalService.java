package com.baem.logisticapp.service;

import com.baem.logisticapp.dto.PersonelCreateDTO;
import com.baem.logisticapp.dto.PersonelResponseDTO;
import com.baem.logisticapp.dto.PersonelUpdateDTO;

import java.util.List;


public interface PersonalService {

     PersonelResponseDTO createPersonel(PersonelCreateDTO createDTO);
     PersonelResponseDTO getPersonelById(Long id);
     List<PersonelResponseDTO> getAllPersonel();
     PersonelResponseDTO updatePersonel(Long id, PersonelUpdateDTO updateDTO);
     void deletePersonel(Long id);
     List<PersonelResponseDTO> getPersonelByDepartment(String department);
     List<PersonelResponseDTO> getPersonelByRole(String role);
     List<PersonelResponseDTO> getActivePersonel();
     PersonelResponseDTO getPersonelByEmail(String email);
     PersonelResponseDTO getPersonelByPhone(String phone);
     List<PersonelResponseDTO> searchPersonelByName(String name);
     List<PersonelResponseDTO> getPersonelByDepartmentAndRole(String department, String role);
     List<PersonelResponseDTO> getPersonelHiredAfter(java.time.LocalDate date);
     List<PersonelResponseDTO> getActivePersonelHiredAfter(java.time.LocalDate date);
     List<PersonelResponseDTO> getActivePersonelByDepartment(String department);

}
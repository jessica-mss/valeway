package com.example.demo.repository;

import com.example.demo.domain.dto.EmployeeResponseDTO;
import com.example.demo.domain.representation.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<EmployeeResponseDTO> findByCpf(String cpf);
}

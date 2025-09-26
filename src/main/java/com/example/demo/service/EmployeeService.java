package com.example.demo.service;

import com.example.demo.domain.dto.EmployeeRequestDTO;
import com.example.demo.domain.dto.EmployeeResponseDTO;
import com.example.demo.domain.representation.Company;
import com.example.demo.domain.representation.Employee;
import com.example.demo.exception.CompanyNotFoundException;
import com.example.demo.exception.EmployeeNotFoundException;
import com.example.demo.repository.CompanyRepository;
import com.example.demo.repository.EmployeeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository repository;
    private final CompanyRepository companyRepository;

    public EmployeeResponseDTO create(EmployeeRequestDTO dto) {
        Company company = companyRepository.findById(dto.getCompanyId())
                .orElseThrow(() -> new EntityNotFoundException("Empresa  não encontrada"));

        Employee employee = Employee.builder()
                .name(dto.getName())
                .cpf(dto.getCpf())
                .salary(dto.getSalary())
                .address(dto.getAddress())
                .registration(dto.getRegistration())
                .company(company)
                .build();

        Employee post = repository.save(employee);

        return EmployeeResponseDTO.fromEntity(post);
    }

    public EmployeeResponseDTO update(Long id, EmployeeRequestDTO requestBody) {
        Employee employee = repository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("Funcionário não encontrado"));

        Company company = companyRepository.findById(requestBody.getCompanyId())
                .orElseThrow(() -> new CompanyNotFoundException("Empresa não encontrada"));

        employee.setName(requestBody.getName());
        employee.setCpf(requestBody.getCpf());
        employee.setSalary(requestBody.getSalary());
        employee.setAddress(requestBody.getAddress());
        employee.setRegistration(requestBody.getRegistration());
        employee.setCompany(company);

        Employee updated = repository.save(employee);

        return EmployeeResponseDTO.fromEntity(updated);
    }

    public void delete(Long id) {
        Employee employee = repository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("Funcionário não encontrado"));
        repository.delete(employee);
    }

    public EmployeeResponseDTO findById(Long id) {
        Employee employee = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Funcionário não encontrado")); // pode trocar por exception customizada
        return EmployeeResponseDTO.fromEntity(employee);
    }

    public List<EmployeeResponseDTO> findAll() {
        return repository.findAll()
                .stream()
                .map(EmployeeResponseDTO::fromEntity)
                .toList();
    }
}


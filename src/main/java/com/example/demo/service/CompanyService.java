package com.example.demo.service;

import com.example.demo.domain.dto.CompanyRequestDTO;
import com.example.demo.domain.dto.CompanyResponseDTO;
import com.example.demo.domain.dto.EmployeeResponseDTO;
import com.example.demo.domain.dto.EmployeesByCompanyDTO;
import com.example.demo.domain.representation.Company;
import com.example.demo.exception.CompanyNotFoundException;
import com.example.demo.repository.CompanyRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CompanyService {

    private final CompanyRepository repository;

    public CompanyService(CompanyRepository repository) {
        this.repository = repository;
    }

    public CompanyResponseDTO create(CompanyRequestDTO dto) {
        Company company = Company.builder()
                .companyName(dto.getCompanyName())
                .cnpj(dto.getCnpj())
                .address(dto.getAddress())
                .contactEmail(dto.getContactEmail())
                .contactPhone(dto.getContactPhone())
                .contactPersonName(dto.getContactPersonName())
                .status(dto.getStatus())
                .build();

        return CompanyResponseDTO.fromEntity(repository.save(company));
    }

    public List<CompanyResponseDTO> getAll() {
        return repository.findAll()
                .stream()
                .map(CompanyResponseDTO::fromEntity)
                .toList();
    }

    public CompanyResponseDTO getById(Long id) {
        Company company = repository.findById(id)
                .orElseThrow(() -> new CompanyNotFoundException("Company not found"));

        return CompanyResponseDTO.fromEntity(company);
    }

    public CompanyResponseDTO update(Long id, CompanyRequestDTO dto) {
        Company company = repository.findById(id)
                .orElseThrow(() -> new CompanyNotFoundException("Company not found"));

        company.setCompanyName(dto.getCompanyName());
        company.setCnpj(dto.getCnpj());
        company.setAddress(dto.getAddress());
        company.setContactEmail(dto.getContactEmail());
        company.setContactPhone(dto.getContactPhone());
        company.setContactPersonName(dto.getContactPersonName());
        company.setStatus(dto.getStatus());

        return CompanyResponseDTO.fromEntity(repository.save(company));
    }

    public void delete(Long id) {
        Company company = repository.findById(id)
                .orElseThrow(() -> new CompanyNotFoundException("Company not found"));
        repository.delete(company);
    }

    public EmployeesByCompanyDTO buscarEmpresaComFuncionarios(Long idEmpresa) {
        Company empresa = repository.findById(idEmpresa)
                .orElseThrow(() -> new EntityNotFoundException("Empresa não encontrada"));

        return EmployeesByCompanyDTO.builder()
                .companyId(empresa.getId())
                .companyName(empresa.getCompanyName())
                .cnpj(empresa.getCnpj())
                .funcionarios(
                        empresa.getEmployees().stream()
                                .map(emp -> EmployeeResponseDTO.builder()
                                        .id(emp.getId())
                                        .name(emp.getName())
                                        .cpf(emp.getCpf())
                                        .salary(emp.getSalary())
                                        .address(emp.getAddress())
                                        .registration(emp.getRegistration())
                                        .build())
                                .collect(Collectors.toList())
                )
                .build();
    }
}

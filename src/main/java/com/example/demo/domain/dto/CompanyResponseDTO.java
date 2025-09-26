package com.example.demo.domain.dto;

import com.example.demo.domain.representation.Company;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CompanyResponseDTO {

    private Long id;
    private String companyName;
    private String cnpj;
    private String address;
    private String contactEmail;
    private String contactPhone;
    private String contactPersonName;
    private String status;

//    private List<EmployeeResponseDTO> employees;

    public static CompanyResponseDTO fromEntity(Company company) {
        return CompanyResponseDTO.builder()
                .id(company.getId())
                .companyName(company.getCompanyName())
                .cnpj(company.getCnpj())
                .address(company.getAddress())
                .contactEmail(company.getContactEmail())
                .contactPhone(company.getContactPhone())
                .contactPersonName(company.getContactPersonName())
                .status(company.getStatus())
//                .employees(
//                        company.getEmployees() != null
//                                ? company.getEmployees().stream()
//                                .map(EmployeeResponseDTO::fromEntity)
//                                .toList()
//                                : null
//                )
                .build();
    }
}


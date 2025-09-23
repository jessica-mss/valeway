package com.example.demo.domain.dto;

import com.example.demo.domain.representation.Employee;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class EmployeeResponseDTO {
    private Long id;
    private String name;
    private String cpf;
    private BigDecimal salary;
    private String address;
    private String registration;
    private String companyCnpj;

    public static EmployeeResponseDTO fromEntity(Employee employee) {
        return EmployeeResponseDTO.builder()
                .id(employee.getId())
                .name(employee.getName())
                .cpf(employee.getCpf())
                .salary(employee.getSalary())
                .address(employee.getAddress())
                .registration(employee.getRegistration())
                .companyCnpj(employee.getCompany().getCnpj())
                .build();
    }
}


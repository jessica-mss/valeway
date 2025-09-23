package com.example.demo.domain.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyRequestDTO {

    @NotBlank
    private String companyName;

    @NotBlank
    private String cnpj;

    @NotBlank
    private String address;

    @Email
    @NotBlank
    private String contactEmail;

    @NotBlank
    private String contactPhone;

    @NotBlank
    private String contactPersonName;

    @NotBlank
    private String status;
}


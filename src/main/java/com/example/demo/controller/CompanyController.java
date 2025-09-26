package com.example.demo.controller;

import com.example.demo.domain.dto.CompanyRequestDTO;
import com.example.demo.domain.dto.CompanyResponseDTO;
import com.example.demo.domain.dto.EmployeesByCompanyDTO;
import com.example.demo.domain.representation.Company;
import com.example.demo.service.CompanyService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/companies")
//@PreAuthorize("hasRole('COMPANY')")
public class CompanyController {

    private final CompanyService service;

    public CompanyController(CompanyService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<CompanyResponseDTO> criar(@RequestBody @Valid CompanyRequestDTO dto) {
        CompanyResponseDTO createdCompany = service.create(dto);

        URI location = URI.create("/company/" + createdCompany.getId());

        return ResponseEntity
                .created(location)
                .body(createdCompany);
    }

    @GetMapping
    public ResponseEntity<List<CompanyResponseDTO>> listar() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompanyResponseDTO> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CompanyResponseDTO> atualizar(@PathVariable Long id, @RequestBody @Valid CompanyRequestDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/employees")
    public ResponseEntity<EmployeesByCompanyDTO> getEmpresaComFuncionarios(@PathVariable Long id) {
        EmployeesByCompanyDTO representation = service.buscarEmpresaComFuncionarios(id);
        return ResponseEntity.ok(representation);
    }
}


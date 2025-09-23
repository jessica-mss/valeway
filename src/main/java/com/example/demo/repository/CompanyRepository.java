package com.example.demo.repository;


import com.example.demo.domain.representation.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> {
}

package com.example.demo.domain.dto;

import jakarta.validation.constraints.NotNull;

public record LoginRequestDTO(@NotNull String email, String password) {
}

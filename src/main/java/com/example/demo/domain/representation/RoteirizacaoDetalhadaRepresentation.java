package com.example.demo.domain.representation;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class RoteirizacaoDetalhadaRepresentation {
    private String sentido;
    private String origem;
    private List<StepRepresentation> percurso;
    private String destino;
    private BigDecimal valor;
}
package com.example.demo.domain.representation;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class RoteirizacaoRepresentation {
    private List<RoteirizacaoDetalhadaRepresentation> detalhesRoteirizacao;
}

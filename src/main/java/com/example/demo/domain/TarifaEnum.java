package com.example.demo.domain;

import java.math.BigDecimal;
import java.util.List;

public enum TarifaEnum {
    ONIBUS(new BigDecimal("5.49")),
    METROFERROVIARIO(new BigDecimal("5.70")),
    ONIBUS_METROFERROVIARIO(new BigDecimal("10.71"));

    private final BigDecimal valor;

    TarifaEnum(BigDecimal valor) {
        this.valor = valor;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public static TarifaEnum defineValorTarifa(List<String> tiposTransporte) {
        boolean temOnibus = tiposTransporte.stream()
                .anyMatch(tipo -> "BUS".equalsIgnoreCase(tipo));
        boolean temSubway = tiposTransporte.stream()
                .anyMatch(tipo -> "SUBWAY".equalsIgnoreCase(tipo));

        if (temOnibus && temSubway) {
            return ONIBUS_METROFERROVIARIO;
        } else if (temOnibus) {
            return ONIBUS;
        } else if (temSubway) {
            return METROFERROVIARIO;
        } else {
            throw new IllegalArgumentException("Nenhum tipo de transporte válido encontrado.");
        }
    }
}
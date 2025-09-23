package com.example.demo.domain.representation;

import lombok.Data;

@Data
public class StepRepresentation {
    private String embarque;
    private TransporteRepresentation transporte;
    private String desembarque;
    private String travel_mode;
}
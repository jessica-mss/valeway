package com.example.demo.domain;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "roteirizacao")
public class Roteirizacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String sentido;
    private String origem;
    private String destino;
    private BigDecimal valor;

    @OneToMany(mappedBy = "roteirizacao", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EtapaRoteirizacao> etapasTrajetoRoteirizacao = new ArrayList<>();
}

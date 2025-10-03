package com.example.demo.domain;

import jakarta.persistence.*;


@Entity
@Table(name = "transporte_roteirizacao")
public class Transporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String codigo;
    private String tipo;
}

package com.example.demo.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "etapa_roteirizacao")
public class EtapaRoteirizacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String embarque;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "transporte_id", referencedColumnName = "id")
    private Transporte transporte;

    private String desembarque;
    private String travel_mode;

    @ManyToOne
    @JoinColumn(name = "roteirizacao_id")
    private Roteirizacao roteirizacao;
}

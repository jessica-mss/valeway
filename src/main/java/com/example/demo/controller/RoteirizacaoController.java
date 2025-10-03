package com.example.demo.controller;

import com.example.demo.domain.*;
import com.example.demo.domain.representation.RoteirizacaoDetalhadaRepresentation;
import com.example.demo.domain.representation.RoteirizacaoRepresentation;
import com.example.demo.service.RoteirizacaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class RoteirizacaoController {

    private final RoteirizacaoService roteirizacaoService;

    public RoteirizacaoController(RoteirizacaoService roteirizacaoService) {
        this.roteirizacaoService = roteirizacaoService;
    }

    @PostMapping("/roteirizacao")
    public RoteirizacaoRepresentation solicitarRoteirizacao(@RequestBody CoordenadasRoteirizacaoRequest request) throws Exception {
        return roteirizacaoService.calcularRoteirizacao(request);
    }

//    @PostMapping("/salvar")
//    public ResponseEntity<?> salvar(@RequestBody RoteirizacaoDetalhadaRepresentation dto) {
//        roteirizacaoService.salvarRoteirizacao(dto);
//        return ResponseEntity.ok().build();
//    }
}



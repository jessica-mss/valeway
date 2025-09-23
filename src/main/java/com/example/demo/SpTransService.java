//package com.example.demo;
//
//import jakarta.annotation.PostConstruct;
//import org.springframework.web.client.RestTemplate;
//
//@Service
//public class SpTransService {
//
//    @Value("${sptrans.token}")
//    private String token;
//
//    private final RestTemplate restTemplate = new RestTemplate();
//
//    @PostConstruct
//    public void autenticar() {
//        restTemplate.postForEntity(
//                "http://api.olhovivo.sptrans.com.br/v2.1/Login/Autenticar?token=" + token,
//                null, String.class
//        );
//    }
//
//    public SpTransLinha buscarLinha(String codigo) {
//        String url = "http://api.olhovivo.sptrans.com.br/v2.1/Linha/Buscar?termosBusca=" + codigo;
//        ResponseEntity<SpTransLinha[]> response = restTemplate.getForEntity(url, SpTransLinha[].class);
//        return response.getBody()[0];
//    }
//}
//

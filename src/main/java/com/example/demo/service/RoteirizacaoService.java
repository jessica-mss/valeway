package com.example.demo.service;

import com.example.demo.domain.CoordenadasRoteirizacaoRequest;
import com.example.demo.domain.TarifaEnum;
import com.example.demo.domain.representation.RoteirizacaoDetalhadaRepresentation;
import com.example.demo.domain.representation.RoteirizacaoRepresentation;
import com.example.demo.domain.representation.StepRepresentation;
import com.example.demo.domain.representation.TransporteRepresentation;
import com.example.demo.repository.RoteirizacaoRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class RoteirizacaoService {

    private final RestTemplate restTemplate;
    private final ObjectMapper mapper;
    private final RoteirizacaoRepository repository;

    @Value("${google.api.key}")
    private String apiKey;

    public RoteirizacaoService(RoteirizacaoRepository repository, RestTemplate restTemplate, ObjectMapper mapper) {
        this.repository = repository;
        this.restTemplate = restTemplate;
        this.mapper = mapper;
    }

    public RoteirizacaoRepresentation calcularRoteirizacao(CoordenadasRoteirizacaoRequest request) throws Exception {

        List<RoteirizacaoDetalhadaRepresentation> detalhes = new ArrayList<>();

        // ==== SENTIDO IDA ====
        RoteirizacaoDetalhadaRepresentation ida = criarDetalheRoteiro(
                request.getEnderecoOrigem(),
                request.getEnderecoDestino(),
                "ida"
        );
        detalhes.add(ida);

        // ==== SENTIDO VOLTA ====
        RoteirizacaoDetalhadaRepresentation volta = criarDetalheRoteiro(
                request.getEnderecoDestino(),
                request.getEnderecoOrigem(),
                "volta"
        );
        detalhes.add(volta);

        RoteirizacaoRepresentation resultado = new RoteirizacaoRepresentation();
        resultado.setDetalhesRoteirizacao(detalhes);

        return resultado;
    }

    private RoteirizacaoDetalhadaRepresentation criarDetalheRoteiro(String origem, String destino, String sentido) throws Exception {

//        String url = UriComponentsBuilder
//                .fromHttpUrl("https://maps.googleapis.com/maps/api/directions/json")
//                .queryParam("origin", origem)
//                .queryParam("destination", destino)
//                .queryParam("mode", "transit")
//                .queryParam("key", apiKey)
//                .toUriString();

        String url = "https://maps.googleapis.com/maps/api/directions/json"
//                + "?origin=" + URLEncoder.encode(origem, StandardCharsets.UTF_8)
//                + "&destination=" + URLEncoder.encode(destino, StandardCharsets.UTF_8)
                + "?origin=" + origem
                + "&destination=" + destino
                + "&mode=transit"
                + "&key=" + apiKey;

        String response = restTemplate.getForObject(url, String.class);
        JsonNode root = mapper.readTree(response);

        if (!root.has("routes") || root.path("routes").isEmpty()) {
            throw new RuntimeException("Nenhuma rota encontrada para os endereços informados.");
        }

        JsonNode leg = root.path("routes").get(0).path("legs").get(0);

        RoteirizacaoDetalhadaRepresentation detalhe = new RoteirizacaoDetalhadaRepresentation();
        detalhe.setOrigem(leg.path("start_address").asText());
        detalhe.setDestino(leg.path("end_address").asText());
        detalhe.setSentido(sentido);

        List<StepRepresentation> percurso = new ArrayList<>();

        for (JsonNode stepNode : leg.path("steps")) {
            if (!"TRANSIT".equalsIgnoreCase(stepNode.path("travel_mode").asText())) continue;

            StepRepresentation step = new StepRepresentation();
            step.setTravel_mode("TRANSIT");
            step.setEmbarque(stepNode.path("transit_details").path("departure_stop").path("name").asText());
            step.setDesembarque(stepNode.path("transit_details").path("arrival_stop").path("name").asText());

            TransporteRepresentation transporte = new TransporteRepresentation();
            transporte.setNome(stepNode.path("transit_details").path("line").path("name").asText());
            transporte.setCodigo(stepNode.path("transit_details").path("line").path("short_name").asText());
            transporte.setTipo(stepNode.path("transit_details").path("line").path("vehicle").path("type").asText());

            step.setTransporte(transporte);
            percurso.add(step);
        }

        detalhe.setPercurso(percurso);

        // Calcula automaticamente o valor da tarifa
        List<String> tiposTransporte = detalhe.getPercurso().stream()
                .map(step -> step.getTransporte().getTipo())
                .toList();

        BigDecimal valorTarifa = TarifaEnum.defineValorTarifa(tiposTransporte).getValor();
        detalhe.setValor(valorTarifa);

        return detalhe;
    }
}
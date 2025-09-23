package com.example.demo.service;

import com.example.demo.domain.CoordenadasRoteirizacaoRequest;
import com.example.demo.domain.representation.RoteirizacaoDetalhadaRepresentation;
import com.example.demo.domain.representation.RoteirizacaoRepresentation;
import com.example.demo.domain.representation.StepRepresentation;
import com.example.demo.domain.representation.TransporteRepresentation;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class RoteirizacaoService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper mapper = new ObjectMapper();

    @Value("${google.api.key}")
    private String apiKey;

    public RoteirizacaoRepresentation calcularRoteirizacao(CoordenadasRoteirizacaoRequest request) throws Exception {

        List<RoteirizacaoDetalhadaRepresentation> detalhes = new ArrayList<>();

        // ==== SENTIDO IDA ====
        RoteirizacaoDetalhadaRepresentation ida = criarDetalheRoteiro(
                request.getEnderecoOrigem(),
                request.getEnderecoDestino(),
                "ida"
        );
        ida.setValor(BigDecimal.valueOf(8));
        detalhes.add(ida);

        // ==== SENTIDO VOLTA ====
        RoteirizacaoDetalhadaRepresentation volta = criarDetalheRoteiro(
                request.getEnderecoDestino(),
                request.getEnderecoOrigem(),
                "volta"
        );

        volta.setValor(BigDecimal.valueOf(8));
        detalhes.add(volta);

        RoteirizacaoRepresentation resultado = new RoteirizacaoRepresentation();
        resultado.setDetalhesRoteirizacao(detalhes);

        return resultado;
    }

    private RoteirizacaoDetalhadaRepresentation criarDetalheRoteiro(String origem, String destino, String sentido) throws Exception {
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
            if (!"TRANSIT".equals(stepNode.path("travel_mode").asText())) continue;

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

        return detalhe;
    }
}
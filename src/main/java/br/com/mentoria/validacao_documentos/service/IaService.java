package br.com.mentoria.validacao_documentos.service;

import br.com.mentoria.validacao_documentos.dto.DadosDocumento;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class IaService {

    @Value("${app.ai.openai.api-key:}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public DadosDocumento extrairDadosEstruturados(String textoBruto) {
        // Fallback local para testar sem gastar API (se não tiver chave)
        if (apiKey == null || apiKey.isEmpty()) {
            System.out.println("AVISO: Chave da OpenAI não configurada. Usando fallback.");
            return new DadosDocumento("Joao da Silva", "123.456.789-00", "Rua das Flores, 150", "Guanambi - BA", "2026-08-10");
        }

        try {
            String url = "https://api.openai.com/v1/chat/completions";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);

            // Monta o prompt para a IA
            String prompt = """
                    Extraia os dados do texto abaixo e retorne APENAS um JSON válido no formato:
                    {"nome": "", "cpf": "", "endereco": "", "cidade": "", "dataEmissao": ""}
                    Se algum dado não for encontrado, deixe como null.
                    Texto: 
                    """ + textoBruto;

            // Corpo da requisição (usando Map para simplificar)
            Map<String, Object> body = new HashMap<>();
            body.put("model", "gpt-3.5-turbo"); // Ou gpt-4o-mini para ser mais barato/rápido
            body.put("messages", List.of(Map.of("role", "user", "content", prompt)));
            
            // Força a IA a retornar um JSON
            body.put("response_format", Map.of("type", "json_object"));

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

            // Lê a resposta da OpenAI
            JsonNode rootNode = objectMapper.readTree(response.getBody());
            String jsonResposta = rootNode.path("choices").get(0).path("message").path("content").asText();

            // Transforma o JSON da resposta no nosso Record DadosDocumento
            return objectMapper.readValue(jsonResposta, DadosDocumento.class);

        } catch (Exception e) {
            System.err.println("Erro na chamada da OpenAI: " + e.getMessage());
            throw new RuntimeException("Falha ao interpretar os dados com IA.", e);
        }
    }
}
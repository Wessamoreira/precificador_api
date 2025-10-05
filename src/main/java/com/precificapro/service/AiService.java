package com.precificapro.service;

import com.precificapro.domain.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AiService {

    private final RestTemplate restTemplate;
    private final String geminiApiKey;

    @Autowired
    private DashboardService dashboardService;

    public AiService(RestTemplateBuilder restTemplateBuilder, @Value("${gemini.api.key}") String geminiApiKey) {
        this.restTemplate = restTemplateBuilder.build();
        this.geminiApiKey = geminiApiKey;
    }

    public String askGemini(String question, User owner) {
        var metrics = dashboardService.getMetrics(owner);
        
        String context = String.format(
            "Dados atuais do negócio: Faturamento Total: R$%.2f, Lucro Líquido Total: R$%.2f, Produtos Cadastrados: %d, Clientes: %d.",
            metrics.totalRevenue(), metrics.totalNetProfit(), metrics.productCount(), metrics.customerCount()
        );

        String prompt = "Você é o PrecificaPro, um assistente financeiro especialista em pequenos negócios. " +
                        "Seja conciso, amigável e direto. Use os dados fornecidos para basear sua resposta. " +
                        "Contexto do negócio: " + context + " Pergunta do usuário: '" + question + "'";

        Map<String, Object> requestBody = new HashMap<>();
        Map<String, String> part = new HashMap<>();
        part.put("text", prompt);
        Map<String, Object> content = new HashMap<>();
        content.put("parts", List.of(part));
        requestBody.put("contents", List.of(content));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        // Usando a URL e o modelo que sabemos que funciona para a API Key simples
        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-pro-latest:generateContent?key=" + geminiApiKey;
        
        try {
            // Usando o RestTemplate, como no seu exemplo validado
            Map responseMap = restTemplate.postForObject(url, requestEntity, Map.class);
            
            List<Map> candidates = (List<Map>) responseMap.get("candidates");
            if (candidates == null || candidates.isEmpty()) {
                return "A IA não conseguiu gerar uma resposta, possivelmente devido a filtros de segurança.";
            }
            Map contentMap = (Map) candidates.get(0).get("content");
            List<Map> parts = (List<Map>) contentMap.get("parts");
            return (String) parts.get(0).get("text");
        } catch (Exception e) {
            System.err.println("Erro ao comunicar com a API do Gemini: " + e.getMessage());
            e.printStackTrace();
            return "Desculpe, estou com dificuldades técnicas. Verifique sua chave de API e a configuração do projeto no Google Cloud.";
        }
    }
}
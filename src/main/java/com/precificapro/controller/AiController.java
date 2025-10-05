package com.precificapro.controller;

import com.precificapro.domain.model.User;
import com.precificapro.service.AiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
// Remova 'import reactor.core.publisher.Mono;'

import java.util.Map;

record AiRequest(String question) {}

@RestController
@RequestMapping("/ai")
public class AiController {

    @Autowired
    private AiService aiService;

    // MUDANÇA AQUI: O método agora retorna ResponseEntity diretamente
    @PostMapping("/ask")
    public ResponseEntity<Map<String, String>> ask(
            @RequestBody AiRequest request,
            @AuthenticationPrincipal User owner
    ) {
        String response = aiService.askGemini(request.question(), owner);
        return ResponseEntity.ok(Map.of("answer", response));
    }
}
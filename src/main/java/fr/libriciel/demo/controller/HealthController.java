package fr.libriciel.demo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Controller pour vérifier la santé de l'API
 */
@RestController
@RequestMapping("/api/v1")
@Tag(name = "Santé", description = "Endpoints de vérification de l'état de l'API")
public class HealthController {
    
    @GetMapping("/health")
    @Operation(
        summary = "Vérifier l'état de l'API",
        description = "Endpoint public pour vérifier que l'API fonctionne correctement"
    )
    public ResponseEntity<Map<String, Object>> health() {
        return ResponseEntity.ok(Map.of(
            "status", "UP",
            "timestamp", LocalDateTime.now(),
            "service", "Libriciel Partner API Demo",
            "version", "1.0.0"
        ));
    }
}

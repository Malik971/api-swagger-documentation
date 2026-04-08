package fr.libriciel.demo.controller;

import fr.libriciel.demo.model.AuthRequest;
import fr.libriciel.demo.model.AuthResponse;
import fr.libriciel.demo.security.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Controller d'authentification pour les partenaires.
 * Simule le endpoint d'authentification OAuth2 de Libriciel.
 */
@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentification", description = "Endpoints d'authentification pour les partenaires")
public class AuthController {
    
    @Autowired
    private JwtService jwtService;
    
    /**
     * Endpoint d'authentification - Obtenir un token d'accès
     * 
     * Pour la démo, les credentials suivants sont acceptés :
     * - client_id: "partnerDemo"
     * - client_secret: "secret123"
     */
    @PostMapping("/token")
    @Operation(
        summary = "Obtenir un token d'accès",
        description = "Authentifie un partenaire et retourne un token JWT pour accéder aux API. " +
                     "Credentials de démo: client_id='partnerDemo', client_secret='secret123'"
    )
    public ResponseEntity<AuthResponse> authenticate(@Valid @RequestBody AuthRequest request) {
        // Pour la démo, validation simple (en production: base de données)
        if ("partnerDemo".equals(request.getClientId()) &&
            "secret123".equals(request.getClientSecret())) {
            
            String token = jwtService.generateToken(request.getClientId());
            AuthResponse response = new AuthResponse(token, jwtService.getExpirationTime());
            
            return ResponseEntity.ok(response);
        }
        
        return ResponseEntity.status(401).build();
    }
    
    /**
     * Endpoint de vérification du token (utile pour les partenaires)
     */
    @GetMapping("/verify")
    @Operation(
            summary = "Vérifier un token",
            description = "Vérifie si un token JWT est valide et non expiré"
    )
    public ResponseEntity<?> verifyToken(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body(
                    Map.of("valid", false, "message", "Token invalide ou absent")
            );
        }

        return ResponseEntity.ok(
                Map.of(
                        "valid", true,
                        "client_id", authentication.getName()
                )
        );
    }
}

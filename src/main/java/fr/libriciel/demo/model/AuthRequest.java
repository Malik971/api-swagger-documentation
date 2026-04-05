package fr.libriciel.demo.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Requête d'authentification pour les partenaires.
 * Simule le flow d'authentification pour accéder aux API Libriciel.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthRequest {
    
    @NotBlank(message = "Le client_id est obligatoire")
    private String clientId;
    
    @NotBlank(message = "Le client_secret est obligatoire")
    private String clientSecret;
}

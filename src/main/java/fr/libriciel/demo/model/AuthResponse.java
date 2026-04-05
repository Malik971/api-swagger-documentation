package fr.libriciel.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Réponse d'authentification contenant le token JWT.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private String accessToken;
    private String tokenType = "Bearer";
    private Long expiresIn; // en secondes
    
    public AuthResponse(String accessToken, Long expiresIn) {
        this.accessToken = accessToken;
        this.expiresIn = expiresIn;
    }
}

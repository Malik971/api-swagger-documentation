package fr.libriciel.demo.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Service de gestion des JWT pour l'authentification des partenaires.
 * Simule le système d'authentification OAuth2/JWT de Libriciel.
 */
@Service
public class JwtService {
    
    @Value("${jwt.secret}")
    private String secret;
    
    @Value("${jwt.expiration}")
    private Long expiration;
    
    /**
     * Génère un token JWT pour un partenaire authentifié
     */
    public String generateToken(String clientId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("client_id", clientId);
        claims.put("scope", "document:read document:write");
        
        return createToken(claims, clientId);
    }
    
    /**
     * Crée le token JWT avec les claims
     */
    private String createToken(Map<String, Object> claims, String subject) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);
        
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    
    /**
     * Valide le token JWT
     */
    public Boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Extrait le client_id du token
     */
    public String extractClientId(String token) {
        return extractAllClaims(token).getSubject();
    }
    
    /**
     * Vérifie si le token est expiré
     */
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
    
    /**
     * Extrait la date d'expiration
     */
    private Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }
    
    /**
     * Extrait tous les claims du token
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
    
    /**
     * Récupère la clé de signature
     */
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }
    
    /**
     * Retourne la durée de validité du token en secondes
     */
    public Long getExpirationTime() {
        return expiration / 1000;
    }
}

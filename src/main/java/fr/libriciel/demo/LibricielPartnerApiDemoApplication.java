package fr.libriciel.demo;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Application de démonstration d'intégration partenaire Libriciel/Pastell
 * 
 * Cette API simule l'écosystème d'intégration de Libriciel pour les partenaires.
 * Elle démontre :
 * - Authentification JWT (similaire OAuth2)
 * - Gestion de flux documentaires
 * - Documentation API complète
 * - Bonnes pratiques d'intégration
 * 
 * @author Malik Ibo
 */
@SpringBootApplication
@OpenAPIDefinition(
    info = @Info(
        title = "Libriciel Partner Integration API - Demo",
        version = "1.0.0",
        description = """
            API de démonstration pour l'intégration des partenaires avec le système Libriciel/Pastell.
            
            ## Authentification
            
            Cette API utilise l'authentification JWT. Pour l'utiliser :
            
            1. Appelez `POST /api/v1/auth/token` avec vos credentials
            2. Récupérez le token JWT dans la réponse
            3. Utilisez ce token dans le header Authorization : `Bearer {token}`
            
            **Credentials de démo** :
            - client_id: `partnerDemo`
            - client_secret: `secret123`
            
            ## Workflow documentaire
            
            Les documents suivent un cycle de vie avec différents statuts :
            - BROUILLON → EN_ATTENTE_SIGNATURE → SIGNE → EN_COURS_TRANSMISSION → TRANSMIS
            
            ## Ressources utiles
            
            - Documentation Swagger : `/swagger-ui.html`
            - Collection Postman : Voir le fichier `libriciel-partner-api.postman_collection.json`
            - Guide d'intégration : Voir `README.md`
            """,
        contact = @Contact(
            name = "Malik Ibo - Candidature DevRel Libriciel",
            email = "malik97un@gmail.com",
            url = "https://malik-ibo.netlify.app"
        )
    ),
    servers = {
        @Server(url = "http://localhost:8080", description = "Serveur de développement local")
    },
    security = {
            @io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "bearer-auth")
    }
)
@SecurityScheme(
    name = "bearer-auth",
    type = SecuritySchemeType.HTTP,
    scheme = "bearer",
    bearerFormat = "JWT"
)
public class LibricielPartnerApiDemoApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(LibricielPartnerApiDemoApplication.class, args);
        
        System.out.println("\n" + "=".repeat(80));
        System.out.println("🚀 Libriciel Partner API Demo - Démarrée avec succès !");
        System.out.println("=".repeat(80));
        System.out.println("📖 Documentation Swagger : http://localhost:8080/swagger-ui.html");
        System.out.println("🔗 API Docs JSON : http://localhost:8080/api-docs");
        System.out.println("✅ Health Check : http://localhost:8080/api/v1/health");
        System.out.println("=".repeat(80) + "\n");
    }
}

# Architecture du projet

> Documentation technique de l'architecture de l'API Libriciel Partner Demo

---

## Vue d'ensemble

Cette API simule un système d'intégration partenaire type Libriciel/Pastell. L'architecture suit les principes **REST**, **Clean Architecture**, et **Spring Boot best practices**.

---

## Architecture en couches

```
┌─────────────────────────────────────────────────────────────┐
│                     CLIENT (Partenaire)                     │
│          (Postman, curl, application Java/Python...)        │
└────────────────────────┬────────────────────────────────────┘
                         │ HTTP/REST
                         │ JSON
                         │
┌────────────────────────▼────────────────────────────────────┐
│                    SECURITY LAYER                           │
│  ┌───────────────────────────────────────────────────────┐  │
│  │  JwtAuthenticationFilter                              │  │
│  │  - Intercepte les requêtes                            │  │
│  │  - Valide le token JWT                                │  │
│  │  - Injecte l'authentification dans SecurityContext    │  │
│  └───────────────────────────────────────────────────────┘  │
└────────────────────────┬────────────────────────────────────┘
                         │
┌────────────────────────▼────────────────────────────────────┐
│                   CONTROLLER LAYER                          │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐       │
│  │   Auth       │  │  Document    │  │   Health     │       │
│  │ Controller   │  │  Controller  │  │  Controller  │       │
│  └──────┬───────┘  └──────┬───────┘  └──────┬───────┘       │
│         │                  │                  │             │
│         │ Gestion des endpoints REST          │             │
│         │ Validation des entrées              │             │
│         │ Mapping Request/Response            │             │
└─────────┼──────────────────┼──────────────────┼─────────────┘
          │                  │                  │
┌─────────▼──────────────────▼──────────────────▼──────────────┐
│                    SERVICE LAYER                             │
│  ┌──────────────┐  ┌──────────────────────────────────┐      │
│  │  JwtService  │  │     DocumentService              │      │
│  │              │  │                                  │      │
│  │ - Générer    │  │ - Logique métier                 │      │
│  │   tokens     │  │ - Gestion du workflow            │      │
│  │ - Valider    │  │ - CRUD documents                 │      │
│  │   tokens     │  │ - Transitions de statut          │      │
│  └──────────────┘  └──────────────────────────────────┘      │
└─────────────────────────────┬────────────────────────────────┘
                              │
┌─────────────────────────────▼─────────────────────────────────┐
│                      DATA LAYER                               │
│  ┌──────────────────────────────────────────────────────┐     │
│  │  ConcurrentHashMap (Stockage en mémoire)             │     │
│  │  - Documents indexés par ID                          │     │
│  │  - Thread-safe                                       │     │
│  │                                                      │     │
│  │  En production : PostgreSQL, MongoDB, etc.           │     │
│  └──────────────────────────────────────────────────────┘     │
└───────────────────────────────────────────────────────────────┘
```

---

## Flux d'authentification

```
┌──────────┐                                    ┌──────────┐
│ Partner  │                                    │   API    │
└─────┬────┘                                    └─────┬────┘
      │                                               │
      │  POST /api/v1/auth/token                      │
      │  { clientId, clientSecret }                   │
      ├──────────────────────────────────────────────>│
      │                                               │
      │                        ┌──────────────────────┤
      │                        │ Valider credentials  │
      │                        │ (AuthController)     │
      │                        └──────────────────────┤
      │                                               │
      │                        ┌──────────────────────┤
      │                        │ Générer JWT          │
      │                        │ (JwtService)         │
      │                        └──────────────────────┤
      │                                               │
      │  200 OK                                       │
      │  { accessToken, expiresIn }                   │
      │<──────────────────────────────────────────────┤
      │                                               │
      │  Sauvegarder le token                         │
      ├──────┐                                        │
      │      │                                        │
      │<─────┘                                        │
      │                                               │
```

---

## Flux de création de document

```
┌──────────┐                                    ┌──────────┐
│ Partner  │                                    │   API    │
└─────┬────┘                                    └─────┬────┘
      │                                               │
      │  POST /api/v1/documents                       │
      │  Authorization: Bearer {token}                │
      │  { titre, type, ... }                         │
      ├──────────────────────────────────────────────>│
      │                                               │
      │                        ┌──────────────────────┤
      │                        │ Valider JWT          │
      │                        │ (JwtAuthFilter)      │
      │                        └──────────────────────┤
      │                                               │
      │                        ┌──────────────────────┤
      │                        │ Valider données      │
      │                        │ (DocumentController) │
      │                        └──────────────────────┤
      │                                               │
      │                        ┌──────────────────────┤
      │                        │ Créer document       │
      │                        │ - Générer ID         │
      │                        │ - Statut BROUILLON   │
      │                        │ - Timestamps         │
      │                        │ (DocumentService)    │
      │                        └──────────────────────┤
      │                                               │
      │                        ┌──────────────────────┤
      │                        │ Stocker en mémoire   │
      │                        │ (ConcurrentHashMap)  │
      │                        └──────────────────────┤
      │                                               │
      │  201 Created                                  │
      │  { id, titre, statut, ... }                   │
      │<──────────────────────────────────────────────┤
      │                                               │
```

---

## Modèle de données

### Document

```
Document
├── id: String (UUID)
├── titre: String (requis)
├── type: String (requis) - ex: "DELIBERATION", "ACTE"
├── statut: DocumentStatus (enum)
├── contenu: String (optionnel)
├── emetteur: String - ID du partenaire
├── destinataire: String
├── dateCreation: LocalDateTime
├── dateModification: LocalDateTime
└── metadata: Metadata
    ├── referenceInterne: String
    ├── classification: String
    ├── confidentiel: Boolean
    └── tags: String[]
```

### DocumentStatus (Enum)

```
BROUILLON
    ↓
EN_ATTENTE_SIGNATURE
    ↓
SIGNE
    ↓
EN_COURS_TRANSMISSION
    ↓
TRANSMIS

États alternatifs:
- REJETE
- ARCHIVE
```

---

## Sécurité

### JWT (JSON Web Token)

**Structure d'un token** :
```
Header (algorithme + type)
{
  "alg": "HS256",
  "typ": "JWT"
}

Payload (claims)
{
  "client_id": "partner-demo",
  "scope": "document:read document:write",
  "sub": "partner-demo",
  "iat": 1712246400,
  "exp": 1712332800
}

Signature
HMACSHA256(
  base64UrlEncode(header) + "." +
  base64UrlEncode(payload),
  secret
)
```

**Validation du token** :
1. Vérifier la signature (clé secrète)
2. Vérifier l'expiration (`exp` claim)
3. Extraire le `client_id`
4. Injecter dans `SecurityContext`

---

## Choix techniques

### Pourquoi Spring Boot ?
- Framework mature et robuste
- Large écosystème (Security, Web, Validation)
- Auto-configuration
- Aligné avec la stack Libriciel (Java)

### Pourquoi JWT ?
- Stateless : pas de session serveur
- Scalable : compatible microservices
- Standard : compatible OAuth2
- Auto-contenu : toutes les infos dans le token

### Pourquoi stockage en mémoire ?
- Simplicité pour la démo
- Pas de dépendance externe
- Démarrage immédiat
- **En production** : base de données requise

### Pourquoi Swagger/OpenAPI ?
- Documentation interactive
- Tests directs dans le navigateur
- Génération automatique depuis les annotations
- Standard de l'industrie

---

## Scalabilité et évolution

### Pour passer en production

#### 1. Base de données
Remplacer `ConcurrentHashMap` par :
```java
@Entity
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    // ...
}

@Repository
public interface DocumentRepository extends JpaRepository<Document, String> {
    List<Document> findByStatut(DocumentStatus statut);
    List<Document> findByEmetteur(String emetteur);
}
```

#### 2. Gestion des secrets
Utiliser **Spring Cloud Config** ou **HashiCorp Vault** :
```properties
# application-prod.properties
jwt.secret=${JWT_SECRET_FROM_VAULT}
```

#### 3. Logging et monitoring
```java
@Slf4j
@RestController
public class DocumentController {
    
    @PostMapping
    public ResponseEntity<Document> createDocument(...) {
        log.info("Creating document for partner: {}", clientId);
        // ...
    }
}
```

Intégrer :
- **Prometheus** pour les métriques
- **Grafana** pour les dashboards
- **ELK Stack** pour les logs centralisés

#### 4. Rate limiting
```java
@RateLimiter(name = "documentApi", fallbackMethod = "rateLimitFallback")
public Document createDocument(...) { ... }
```

#### 5. Webhooks
Notifier les partenaires des changements :
```java
@Async
public void notifyPartner(Document document, String event) {
    // POST vers webhook_url du partenaire
}
```

---

## Tests

### Tests unitaires
- `DocumentServiceTest` : Tests de la logique métier
- Couverture : création, lecture, mise à jour, suppression, workflow

### Tests d'intégration (à ajouter)
```java
@SpringBootTest
@AutoConfigureMockMvc
class DocumentControllerIntegrationTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Test
    void testCreateDocumentFlow() throws Exception {
        // Test complet : auth + création + workflow
    }
}
```

---

## Métriques et KPIs

### Métriques techniques
- Temps de réponse moyen par endpoint
- Taux d'erreur par endpoint
- Nombre de requêtes par seconde

### Métriques métier
- Nombre de documents créés par jour
- Répartition par statut
- Nombre de partenaires actifs
- Taux de succès des transmissions

---

## Conclusion

Cette architecture démontre :
- **Compréhension technique** : Spring Boot, REST, JWT
- **Scalabilité** : prêt pour l'évolution vers la production
- **Bonnes pratiques** : séparation des couches, tests, documentation
- **Expérience développeur** : API claire, documentation complète, exemples

C'est exactement le type d'architecture qu'un **DevRel** doit maîtriser pour accompagner les partenaires efficacement.

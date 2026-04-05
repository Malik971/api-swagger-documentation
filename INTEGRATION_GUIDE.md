# 📘 Guide d'Intégration Partenaire - Libriciel API

> **Guide pratique** pour intégrer votre logiciel avec l'écosystème Libriciel/Pastell

---

## 🎯 Objectif de ce guide

Ce document vous accompagne **étape par étape** dans l'intégration de votre solution avec les API Libriciel. Que vous soyez un éditeur de logiciel métier, un intégrateur, ou un développeur indépendant, vous trouverez ici tout ce dont vous avez besoin pour réussir votre intégration.

---

## 📋 Prérequis techniques

### Connaissances requises
- ✅ Protocole HTTP/HTTPS
- ✅ Format JSON
- ✅ Authentification par token (Bearer)
- ✅ Bases des APIs REST

### Outils recommandés
- **Postman** ou équivalent pour tester les APIs
- **Curl** ou un client HTTP dans votre langage
- **Git** pour versioning (optionnel)

---

## 🚀 Étapes d'intégration

### Étape 1 : Obtenir vos identifiants d'accès

Dans cette démo, utilisez :
```
client_id: partner-demo
client_secret: secret123
```

> ⚠️ **En production** : Ces identifiants vous seront fournis par Libriciel lors de votre onboarding partenaire.

### Étape 2 : Tester la connexion

Avant toute intégration, vérifiez que l'API est accessible :

```bash
curl http://localhost:8080/api/v1/health
```

✅ **Résultat attendu** : `{"status":"UP"}`

### Étape 3 : S'authentifier

Toutes les opérations sur les documents nécessitent un token d'authentification.

#### Requête

```bash
curl -X POST http://localhost:8080/api/v1/auth/token \
  -H "Content-Type: application/json" \
  -d '{
    "clientId": "partner-demo",
    "clientSecret": "secret123"
  }'
```

#### Réponse

```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJjbGllbnRfaWQiOiJwYXJ0bmVyLWRlbW8i...",
  "tokenType": "Bearer",
  "expiresIn": 86400
}
```

#### Sauvegarder le token

Stockez le `accessToken` dans votre application. Il est valide **24 heures**.

---

## 📄 Cas d'usage : Envoyer un document

### Scénario

Votre logiciel génère une **délibération du conseil municipal**. Vous devez l'envoyer dans le système de dématérialisation Libriciel.

### Code d'exemple (JavaScript/Node.js)

```javascript
const axios = require('axios');

const BASE_URL = 'http://localhost:8080/api/v1';
let accessToken = '';

// 1. Authentification
async function authenticate() {
  const response = await axios.post(`${BASE_URL}/auth/token`, {
    clientId: 'partner-demo',
    clientSecret: 'secret123'
  });
  
  accessToken = response.data.accessToken;
  console.log('✅ Authentifié avec succès');
}

// 2. Créer le document
async function createDocument() {
  const response = await axios.post(
    `${BASE_URL}/documents`,
    {
      titre: 'Délibération Conseil Municipal - Mars 2026',
      type: 'DELIBERATION',
      statut: 'BROUILLON',
      destinataire: 'Mairie de Montpellier',
      metadata: {
        referenceInterne: 'DEL-2026-031',
        classification: 'PUBLIC',
        confidentiel: false,
        tags: ['conseil', 'municipal']
      }
    },
    {
      headers: {
        'Authorization': `Bearer ${accessToken}`,
        'Content-Type': 'application/json'
      }
    }
  );
  
  const documentId = response.data.id;
  console.log(`✅ Document créé : ${documentId}`);
  return documentId;
}

// 3. Faire signer le document
async function signDocument(documentId) {
  await axios.patch(
    `${BASE_URL}/documents/${documentId}/statut`,
    { statut: 'SIGNE' },
    {
      headers: {
        'Authorization': `Bearer ${accessToken}`,
        'Content-Type': 'application/json'
      }
    }
  );
  
  console.log('✅ Document signé');
}

// 4. Transmettre le document
async function transmitDocument(documentId) {
  await axios.patch(
    `${BASE_URL}/documents/${documentId}/statut`,
    { statut: 'TRANSMIS' },
    {
      headers: {
        'Authorization': `Bearer ${accessToken}`,
        'Content-Type': 'application/json'
      }
    }
  );
  
  console.log('✅ Document transmis');
}

// Flux complet
async function main() {
  try {
    await authenticate();
    const docId = await createDocument();
    await signDocument(docId);
    await transmitDocument(docId);
    console.log('🎉 Intégration réussie !');
  } catch (error) {
    console.error('❌ Erreur :', error.message);
  }
}

main();
```

### Code d'exemple (Python)

```python
import requests

BASE_URL = "http://localhost:8080/api/v1"
access_token = ""

# 1. Authentification
def authenticate():
    global access_token
    response = requests.post(
        f"{BASE_URL}/auth/token",
        json={
            "clientId": "partner-demo",
            "clientSecret": "secret123"
        }
    )
    access_token = response.json()["accessToken"]
    print("✅ Authentifié avec succès")

# 2. Créer le document
def create_document():
    headers = {
        "Authorization": f"Bearer {access_token}",
        "Content-Type": "application/json"
    }
    
    document = {
        "titre": "Délibération Conseil Municipal - Mars 2026",
        "type": "DELIBERATION",
        "statut": "BROUILLON",
        "destinataire": "Mairie de Montpellier",
        "metadata": {
            "referenceInterne": "DEL-2026-031",
            "classification": "PUBLIC",
            "confidentiel": False,
            "tags": ["conseil", "municipal"]
        }
    }
    
    response = requests.post(
        f"{BASE_URL}/documents",
        json=document,
        headers=headers
    )
    
    document_id = response.json()["id"]
    print(f"✅ Document créé : {document_id}")
    return document_id

# 3. Changer le statut
def change_status(document_id, new_status):
    headers = {
        "Authorization": f"Bearer {access_token}",
        "Content-Type": "application/json"
    }
    
    requests.patch(
        f"{BASE_URL}/documents/{document_id}/statut",
        json={"statut": new_status},
        headers=headers
    )
    
    print(f"✅ Statut changé : {new_status}")

# Flux complet
def main():
    try:
        authenticate()
        doc_id = create_document()
        change_status(doc_id, "SIGNE")
        change_status(doc_id, "TRANSMIS")
        print("🎉 Intégration réussie !")
    except Exception as e:
        print(f"❌ Erreur : {e}")

if __name__ == "__main__":
    main()
```

### Code d'exemple (Java)

```java
import java.net.http.*;
import java.net.URI;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.*;

public class LibricielIntegration {
    private static final String BASE_URL = "http://localhost:8080/api/v1";
    private static String accessToken = "";
    private static HttpClient client = HttpClient.newHttpClient();
    private static ObjectMapper mapper = new ObjectMapper();
    
    // 1. Authentification
    public static void authenticate() throws Exception {
        Map<String, String> credentials = Map.of(
            "clientId", "partner-demo",
            "clientSecret", "secret123"
        );
        
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/auth/token"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(
                mapper.writeValueAsString(credentials)))
            .build();
        
        HttpResponse<String> response = client.send(
            request, HttpResponse.BodyHandlers.ofString());
        
        Map<String, Object> result = mapper.readValue(
            response.body(), Map.class);
        accessToken = (String) result.get("accessToken");
        
        System.out.println("✅ Authentifié avec succès");
    }
    
    // 2. Créer un document
    public static String createDocument() throws Exception {
        Map<String, Object> document = Map.of(
            "titre", "Délibération Conseil Municipal - Mars 2026",
            "type", "DELIBERATION",
            "statut", "BROUILLON",
            "destinataire", "Mairie de Montpellier",
            "metadata", Map.of(
                "referenceInterne", "DEL-2026-031",
                "classification", "PUBLIC",
                "confidentiel", false,
                "tags", List.of("conseil", "municipal")
            )
        );
        
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/documents"))
            .header("Authorization", "Bearer " + accessToken)
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(
                mapper.writeValueAsString(document)))
            .build();
        
        HttpResponse<String> response = client.send(
            request, HttpResponse.BodyHandlers.ofString());
        
        Map<String, Object> result = mapper.readValue(
            response.body(), Map.class);
        String documentId = (String) result.get("id");
        
        System.out.println("✅ Document créé : " + documentId);
        return documentId;
    }
    
    // 3. Changer le statut
    public static void changeStatus(String documentId, String newStatus) 
            throws Exception {
        Map<String, String> statusUpdate = Map.of("statut", newStatus);
        
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/documents/" + documentId + "/statut"))
            .header("Authorization", "Bearer " + accessToken)
            .header("Content-Type", "application/json")
            .method("PATCH", HttpRequest.BodyPublishers.ofString(
                mapper.writeValueAsString(statusUpdate)))
            .build();
        
        client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("✅ Statut changé : " + newStatus);
    }
    
    // Flux complet
    public static void main(String[] args) {
        try {
            authenticate();
            String docId = createDocument();
            changeStatus(docId, "SIGNE");
            changeStatus(docId, "TRANSMIS");
            System.out.println("🎉 Intégration réussie !");
        } catch (Exception e) {
            System.err.println("❌ Erreur : " + e.getMessage());
        }
    }
}
```

---

## 🔄 Workflow documentaire - Détails

### Cycle de vie complet

```
┌─────────────┐
│  BROUILLON  │ ← État initial
└──────┬──────┘
       │
       ▼
┌──────────────────────────┐
│ EN_ATTENTE_SIGNATURE     │ ← Document prêt à signer
└──────┬───────────────────┘
       │
       ▼
┌─────────────┐
│    SIGNE    │ ← Signature électronique effectuée
└──────┬──────┘
       │
       ▼
┌──────────────────────────┐
│ EN_COURS_TRANSMISSION    │ ← Transmission en cours
└──────┬───────────────────┘
       │
       ▼
┌─────────────┐
│  TRANSMIS   │ ← Document transmis avec succès
└─────────────┘
```

### Statuts alternatifs

- **REJETE** : Le document a été refusé (erreur, non-conformité, etc.)
- **ARCHIVE** : Le document est archivé pour conservation

---

## 🛡️ Gestion des erreurs

### Codes HTTP courants

| Code | Signification | Action recommandée |
|------|---------------|-------------------|
| `200` | Succès | Continuer |
| `201` | Créé | Document créé avec succès |
| `401` | Non autorisé | Vérifier le token / Se réauthentifier |
| `404` | Non trouvé | Vérifier l'ID du document |
| `400` | Requête invalide | Vérifier le format JSON / les champs obligatoires |
| `500` | Erreur serveur | Contacter le support Libriciel |

### Exemple de gestion d'erreur (JavaScript)

```javascript
async function safeRequest(url, options) {
  try {
    const response = await axios(url, options);
    return response.data;
  } catch (error) {
    if (error.response) {
      switch (error.response.status) {
        case 401:
          console.log('Token expiré, réauthentification...');
          await authenticate();
          return safeRequest(url, options); // Retry
        case 404:
          console.error('Document introuvable');
          break;
        case 400:
          console.error('Requête invalide :', error.response.data);
          break;
        default:
          console.error('Erreur serveur :', error.response.status);
      }
    }
    throw error;
  }
}
```

---

## ✅ Checklist d'intégration

### Phase 1 : Préparation
- [ ] Lire la documentation API
- [ ] Obtenir les identifiants (client_id / client_secret)
- [ ] Installer Postman et importer la collection
- [ ] Tester manuellement l'authentification

### Phase 2 : Développement
- [ ] Implémenter l'authentification dans votre code
- [ ] Tester la création d'un document
- [ ] Tester les transitions de statut
- [ ] Implémenter la gestion d'erreur
- [ ] Ajouter des logs pour le debugging

### Phase 3 : Tests
- [ ] Tester le workflow complet
- [ ] Tester les cas d'erreur
- [ ] Vérifier le renouvellement du token
- [ ] Valider les données transmises

### Phase 4 : Production
- [ ] Remplacer les URLs de démo par les URLs de production
- [ ] Sécuriser le stockage des credentials
- [ ] Mettre en place un monitoring
- [ ] Prévoir une gestion des logs

---

## 📞 Support & Contact

### Vous êtes bloqué ?

1. **Consultez la FAQ** dans le README principal
2. **Testez avec Postman** pour isoler le problème
3. **Vérifiez les logs** de votre application
4. **Contactez le support** Libriciel

### Ressources utiles

- 📖 **Swagger UI** : http://localhost:8080/swagger-ui.html
- 📄 **README** : Voir `README.md`
- 📮 **Collection Postman** : `libriciel-partner-api.postman_collection.json`

---

## 🎓 Bonnes pratiques

### Sécurité
- ✅ Ne jamais exposer vos credentials dans le code
- ✅ Utiliser des variables d'environnement
- ✅ Renouveler le token avant expiration
- ✅ Logger les erreurs, pas les tokens

### Performance
- ✅ Réutiliser le même token pendant 24h
- ✅ Gérer les retry en cas d'erreur temporaire
- ✅ Implémenter un cache si nécessaire

### Fiabilité
- ✅ Toujours vérifier les codes HTTP
- ✅ Valider les données avant envoi
- ✅ Gérer les timeouts
- ✅ Prévoir un système de retry

---

**Besoin d'aide ?** Ce guide est créé par Malik Ibo dans le cadre de sa candidature DevRel chez Libriciel. L'objectif est de faciliter votre intégration et de vous accompagner vers le succès ! 🚀

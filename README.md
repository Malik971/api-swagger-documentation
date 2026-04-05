# 🔌 Libriciel Partner API - Démo d'Intégration

> **Projet de démonstration** créé par **Malik Ibo** dans le cadre de sa candidature au poste de **DevRel** chez **Libriciel SCOP**.

Ce projet simule l'écosystème d'intégration d'un partenaire avec les APIs Libriciel/Pastell. Il démontre ma compréhension du rôle DevRel : **faciliter les intégrations techniques** à travers une documentation claire, des exemples concrets, et une expérience développeur optimale.

---

## 📋 Table des matières

- [Vue d'ensemble](#-vue-densemble)
- [Démarrage rapide](#-démarrage-rapide)
- [Authentification](#-authentification)
- [Endpoints API](#-endpoints-api)
- [Workflow documentaire](#-workflow-documentaire)
- [Exemples d'utilisation](#-exemples-dutilisation)
- [Collection Postman](#-collection-postman)
- [Architecture](#-architecture)
- [FAQ](#-faq)

---

## 🎯 Vue d'ensemble

### Contexte

Cette API simule le système d'intégration que Libriciel propose aux éditeurs partenaires pour connecter leurs logiciels à l'écosystème Pastell de dématérialisation administrative.

### Que fait cette API ?

Elle permet aux partenaires de :
- ✅ S'authentifier de manière sécurisée (JWT)
- ✅ Créer et gérer des documents dans le flux de dématérialisation
- ✅ Suivre le cycle de vie des documents (workflow)
- ✅ Interroger l'état et les statistiques du système

### Technologies utilisées

- **Backend** : Spring Boot 3.2.0, Java 17
- **Sécurité** : Spring Security + JWT
- **Documentation** : Swagger/OpenAPI 3.0
- **Build** : Maven

---

## 🚀 Démarrage rapide

### Prérequis

- Java 17 ou supérieur
- Maven 3.8+
- (Optionnel) Postman pour tester l'API

### Installation

```bash
# 1. Cloner le repository
git clone https://github.com/Malik971/libriciel-partner-api-demo.git
cd libriciel-partner-api-demo

# 2. Compiler le projet
mvn clean install

# 3. Lancer l'application
mvn spring-boot:run
```

L'API sera accessible sur : **http://localhost:8080**

### Vérification

```bash
# Test du health check (endpoint public)
curl http://localhost:8080/api/v1/health
```

Réponse attendue :
```json
{
  "status": "UP",
  "timestamp": "2026-04-04T16:30:00",
  "service": "Libriciel Partner API Demo",
  "version": "1.0.0"
}
```

---

## 🔐 Authentification

### Principe

L'API utilise l'authentification **JWT (JSON Web Token)**, similaire au flow OAuth2 Client Credentials.

### Obtenir un token d'accès

**Endpoint** : `POST /api/v1/auth/token`

**Credentials de démo** :
```json
{
  "clientId": "partner-demo",
  "clientSecret": "secret123"
}
```

**Exemple avec curl** :
```bash
curl -X POST http://localhost:8080/api/v1/auth/token \
  -H "Content-Type: application/json" \
  -d '{
    "clientId": "partner-demo",
    "clientSecret": "secret123"
  }'
```

**Réponse** :
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
  "tokenType": "Bearer",
  "expiresIn": 86400
}
```

### Utiliser le token

Une fois le token obtenu, incluez-le dans toutes vos requêtes :

```bash
curl http://localhost:8080/api/v1/documents \
  -H "Authorization: Bearer {votre_token}"
```

### Durée de validité

- **Durée** : 24 heures (86400 secondes)
- **Renouvellement** : Appelez `/api/v1/auth/token` pour obtenir un nouveau token

---

## 📡 Endpoints API

### Documentation interactive

👉 **Swagger UI** : http://localhost:8080/swagger-ui.html

Toute l'API est documentée et testable directement dans Swagger.

### Principaux endpoints

| Méthode | Endpoint | Description | Auth requise |
|---------|----------|-------------|--------------|
| `POST` | `/api/v1/auth/token` | Obtenir un token d'accès | ❌ Non |
| `GET` | `/api/v1/auth/verify` | Vérifier un token | ✅ Oui |
| `GET` | `/api/v1/health` | Health check | ❌ Non |
| `POST` | `/api/v1/documents` | Créer un document | ✅ Oui |
| `GET` | `/api/v1/documents` | Lister tous les documents | ✅ Oui |
| `GET` | `/api/v1/documents/{id}` | Récupérer un document | ✅ Oui |
| `GET` | `/api/v1/documents/mes-documents` | Mes documents | ✅ Oui |
| `PUT` | `/api/v1/documents/{id}` | Modifier un document | ✅ Oui |
| `PATCH` | `/api/v1/documents/{id}/statut` | Changer le statut | ✅ Oui |
| `DELETE` | `/api/v1/documents/{id}` | Supprimer un document | ✅ Oui |
| `GET` | `/api/v1/documents/stats` | Statistiques | ✅ Oui |

---

## 🔄 Workflow documentaire

### Statuts disponibles

Les documents suivent un cycle de vie avec différents états :

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
```

**Autres statuts** :
- `REJETE` : Document refusé
- `ARCHIVE` : Document archivé

### Transitions de statut

Pour faire passer un document d'un statut à un autre :

```bash
curl -X PATCH http://localhost:8080/api/v1/documents/{id}/statut \
  -H "Authorization: Bearer {token}" \
  -H "Content-Type: application/json" \
  -d '{"statut": "SIGNE"}'
```

---

## 💡 Exemples d'utilisation

### Scénario complet : Créer et transmettre un document

#### Étape 1 : S'authentifier

```bash
TOKEN=$(curl -s -X POST http://localhost:8080/api/v1/auth/token \
  -H "Content-Type: application/json" \
  -d '{"clientId":"partner-demo","clientSecret":"secret123"}' \
  | jq -r '.accessToken')
```

#### Étape 2 : Créer un document

```bash
DOC_ID=$(curl -s -X POST http://localhost:8080/api/v1/documents \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "titre": "Délibération Conseil Municipal",
    "type": "DELIBERATION",
    "statut": "BROUILLON",
    "destinataire": "Mairie de Montpellier",
    "metadata": {
      "referenceInterne": "DEL-2026-042",
      "classification": "PUBLIC",
      "confidentiel": false,
      "tags": ["conseil", "municipal", "2026"]
    }
  }' | jq -r '.id')

echo "Document créé avec l'ID : $DOC_ID"
```

#### Étape 3 : Faire signer le document

```bash
curl -X PATCH http://localhost:8080/api/v1/documents/$DOC_ID/statut \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"statut": "EN_ATTENTE_SIGNATURE"}'
```

#### Étape 4 : Marquer comme signé

```bash
curl -X PATCH http://localhost:8080/api/v1/documents/$DOC_ID/statut \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"statut": "SIGNE"}'
```

#### Étape 5 : Transmettre le document

```bash
curl -X PATCH http://localhost:8080/api/v1/documents/$DOC_ID/statut \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"statut": "TRANSMIS"}'
```

#### Étape 6 : Vérifier le statut

```bash
curl http://localhost:8080/api/v1/documents/$DOC_ID \
  -H "Authorization: Bearer $TOKEN"
```

### Filtrer les documents par statut

```bash
# Lister tous les documents transmis
curl "http://localhost:8080/api/v1/documents?statut=TRANSMIS" \
  -H "Authorization: Bearer $TOKEN"
```

### Obtenir des statistiques

```bash
curl http://localhost:8080/api/v1/documents/stats \
  -H "Authorization: Bearer $TOKEN"
```

---

## 📮 Collection Postman

Une collection Postman complète est fournie pour faciliter les tests : `libriciel-partner-api.postman_collection.json`

### Importer la collection

1. Ouvrez Postman
2. Cliquez sur **Import**
3. Sélectionnez le fichier `libriciel-partner-api.postman_collection.json`

### Structure de la collection

La collection contient :
- ✅ **Authentification** : Obtenir et vérifier un token
- ✅ **Documents - CRUD** : Créer, lire, modifier, supprimer
- ✅ **Documents - Workflow** : Transitions de statut
- ✅ **Documents - Recherche** : Filtres et statistiques
- ✅ **Santé** : Health check

### Variables d'environnement Postman

Créez un environnement Postman avec :
```
baseUrl = http://localhost:8080
token = (sera rempli automatiquement après auth)
```

---

## 🏗️ Architecture

### Structure du projet

```
src/main/java/fr/libriciel/demo/
├── LibricielPartnerApiDemoApplication.java    # Application principale
├── controller/
│   ├── AuthController.java                     # Authentification
│   ├── DocumentController.java                 # Gestion documents
│   └── HealthController.java                   # Health check
├── model/
│   ├── Document.java                           # Modèle document
│   ├── DocumentStatus.java                     # Statuts workflow
│   ├── AuthRequest.java                        # Requête auth
│   └── AuthResponse.java                       # Réponse auth
├── service/
│   └── DocumentService.java                    # Logique métier
└── security/
    ├── JwtService.java                         # Gestion JWT
    ├── JwtAuthenticationFilter.java            # Filtre auth
    └── SecurityConfig.java                     # Configuration sécurité
```

### Choix techniques

#### Pourquoi JWT ?
- ✅ Stateless : pas de session côté serveur
- ✅ Scalable : compatible avec une architecture microservices
- ✅ Standard : compatible OAuth2 (Client Credentials flow)
- ✅ Sécurisé : signature cryptographique

#### Stockage en mémoire
Pour cette démo, les documents sont stockés en mémoire (`ConcurrentHashMap`). En production, on utiliserait une base de données (PostgreSQL, MongoDB, etc.).

---

## ❓ FAQ

### Comment renouveler un token expiré ?
Appelez à nouveau `/api/v1/auth/token` avec vos credentials.

### Puis-je modifier les credentials de démo ?
Oui, modifiez `AuthController.java` ligne 39 ou implémentez une vraie base de données.

### Comment tester l'API sans Postman ?
Utilisez Swagger UI : http://localhost:8080/swagger-ui.html

### Les données persistent-elles après redémarrage ?
Non, dans cette démo, les données sont stockées en mémoire. Au redémarrage, tout est effacé.

### Comment ajouter d'autres types de documents ?
Modifiez simplement le champ `type` dans votre requête. Il n'y a pas de validation stricte dans cette démo.

### Puis-je utiliser cette API en production ?
Non, c'est une **démo pédagogique**. Pour la production :
- Utilisez une vraie base de données
- Implémentez une gestion sécurisée des secrets
- Ajoutez du rate limiting
- Utilisez HTTPS
- Implémentez une vraie gestion des utilisateurs

---

## 🎓 Pourquoi ce projet ?

Ce projet démontre ma compréhension du rôle **DevRel** chez Libriciel :

### 1. Compréhension technique
- ✅ APIs REST
- ✅ Authentification JWT
- ✅ Architecture Spring Boot
- ✅ Sécurité applicative

### 2. Pédagogie et documentation
- ✅ README structuré et clair
- ✅ Exemples concrets d'utilisation
- ✅ Documentation Swagger complète
- ✅ Collection Postman prête à l'emploi

### 3. Compréhension métier
- ✅ Workflow documentaire administratif
- ✅ Flux de dématérialisation
- ✅ Interopérabilité partenaires
- ✅ Cycle de vie d'un document

### 4. Facilitation d'intégration
- ✅ Démarrage rapide en 3 commandes
- ✅ Scénarios complets pas à pas
- ✅ Gestion des erreurs explicite
- ✅ Expérience développeur optimisée

---

## 👤 Auteur

**Malik Ibo**  
Candidat au poste de DevRel chez Libriciel SCOP

- 📧 Email : malik97un@gmail.com
- 💼 Portfolio : [malik-ibo.netlify.app](https://malik-ibo.netlify.app)
- 🐙 GitHub : [@Malik971](https://github.com/Malik971)

---

## 📝 Licence

Projet de démonstration créé dans un contexte de candidature professionnelle.

---

**Prêt à intégrer avec Libriciel ? 🚀**

Si vous êtes un partenaire et souhaitez intégrer vos solutions avec Libriciel, ce projet vous donne un aperçu de l'expérience que je souhaite créer en tant que DevRel : **claire, documentée, et centrée sur votre réussite**.

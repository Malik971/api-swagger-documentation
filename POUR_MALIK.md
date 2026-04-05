# 📦 Votre projet DevRel est prêt, Malik !

> **Guide complet** pour comprendre, utiliser et présenter votre projet

---

## 🎉 Ce qui a été créé pour vous

### 📁 Structure complète du projet

```
libriciel-partner-api-demo/
├── src/
│   ├── main/
│   │   ├── java/fr/libriciel/demo/
│   │   │   ├── LibricielPartnerApiDemoApplication.java    # Point d'entrée
│   │   │   ├── controller/
│   │   │   │   ├── AuthController.java                    # Authentification
│   │   │   │   ├── DocumentController.java                # CRUD documents
│   │   │   │   └── HealthController.java                  # Health check
│   │   │   ├── model/
│   │   │   │   ├── Document.java                          # Modèle document
│   │   │   │   ├── DocumentStatus.java                    # Enum statuts
│   │   │   │   ├── AuthRequest.java                       # Requête auth
│   │   │   │   └── AuthResponse.java                      # Réponse auth
│   │   │   ├── service/
│   │   │   │   └── DocumentService.java                   # Logique métier
│   │   │   └── security/
│   │   │       ├── JwtService.java                        # Gestion JWT
│   │   │       ├── JwtAuthenticationFilter.java           # Filtre auth
│   │   │       └── SecurityConfig.java                    # Config sécurité
│   │   └── resources/
│   │       └── application.properties                     # Configuration
│   └── test/
│       └── java/fr/libriciel/demo/
│           └── service/
│               └── DocumentServiceTest.java               # Tests unitaires
│
├── pom.xml                                                # Dépendances Maven
├── Dockerfile                                             # Image Docker
├── docker-compose.yml                                     # Orchestration
├── start.sh                                               # Script Linux/Mac
├── start.bat                                              # Script Windows
├── .gitignore                                             # Fichiers ignorés
├── LICENSE                                                # Licence MIT
│
├── README.md                                              # ⭐ DOC PRINCIPALE
├── QUICKSTART.md                                          # Démarrage rapide
├── INTEGRATION_GUIDE.md                                   # Guide d'intégration
├── ARCHITECTURE.md                                        # Documentation archi
├── PRESENTATION_GUIDE.md                                  # Guide présentation
│
└── libriciel-partner-api.postman_collection.json         # Collection Postman
```

---

## 🚀 Comment utiliser ce projet

### Option 1 : Démarrage rapide (recommandé)

#### Sur Linux/Mac :
```bash
cd libriciel-partner-api-demo
./start.sh
```

#### Sur Windows :
```bash
cd libriciel-partner-api-demo
start.bat
```

L'application sera accessible sur : **http://localhost:8080**

### Option 2 : Avec Docker
```bash
cd libriciel-partner-api-demo
docker-compose up
```

### Option 3 : Manuellement
```bash
cd libriciel-partner-api-demo
mvn clean install
mvn spring-boot:run
```

---

## 🧪 Tester l'API

### Méthode 1 : Postman (recommandé pour l'entretien)

1. Ouvrez Postman
2. Import → `libriciel-partner-api.postman_collection.json`
3. Exécutez "1. Obtenir un token" (dossier Authentification)
4. Le token sera automatiquement sauvegardé
5. Testez les autres requêtes !

**Credentials** :
- client_id: `partner-demo`
- client_secret: `secret123`

### Méthode 2 : Swagger UI

Ouvrez : http://localhost:8080/swagger-ui.html

Testez directement dans le navigateur !

### Méthode 3 : curl

```bash
# 1. S'authentifier
TOKEN=$(curl -s -X POST http://localhost:8080/api/v1/auth/token \
  -H "Content-Type: application/json" \
  -d '{"clientId":"partner-demo","clientSecret":"secret123"}' \
  | jq -r '.accessToken')

# 2. Créer un document
curl -X POST http://localhost:8080/api/v1/documents \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "titre": "Test Document",
    "type": "DELIBERATION",
    "statut": "BROUILLON",
    "destinataire": "Mairie de Montpellier"
  }'
```

---

## 📖 Documentation à lire

### Pour comprendre le projet
1. **README.md** : Documentation principale, vue d'ensemble
2. **ARCHITECTURE.md** : Comprendre comment ça marche techniquement
3. **QUICKSTART.md** : Tester en 3 minutes

### Pour l'entretien
1. **PRESENTATION_GUIDE.md** : ⭐ **TRÈS IMPORTANT** - Comment présenter ce projet
2. **INTEGRATION_GUIDE.md** : Montrer comment vous aidez les partenaires

---

## 🎤 Présenter ce projet en entretien

### Préparation avant l'entretien

1. **Testez que tout fonctionne** :
   ```bash
   ./start.sh
   # Attendez que l'app démarre
   # Ouvrez http://localhost:8080/swagger-ui.html
   ```

2. **Ouvrez ces onglets dans votre navigateur** :
   - Swagger UI : http://localhost:8080/swagger-ui.html
   - README.md dans VS Code ou votre IDE
   - INTEGRATION_GUIDE.md

3. **Ouvrez Postman** avec la collection importée

4. **Relisez PRESENTATION_GUIDE.md** pour le discours

### Pendant l'entretien

**Structure de présentation (5-7 min)** :

1. **Introduction** (30 sec)
   > "J'ai créé ce projet pour démontrer ma compréhension du rôle DevRel..."

2. **Contexte métier** (1 min)
   > "En étudiant Libriciel, j'ai compris que..."

3. **Démo technique** (3 min)
   - Montrez le README
   - Lancez l'app
   - Ouvrez Swagger
   - Montrez Postman
   - Montrez le guide d'intégration

4. **Ce que ça démontre** (1 min)
   > "Ce projet démontre : technique + pédagogie + métier..."

5. **Lien avec le poste** (1 min)
   > "En tant que DevRel chez Libriciel, je ferais..."

**Consultez PRESENTATION_GUIDE.md pour tous les détails !**

---

## 🔥 Points forts à mettre en avant

### Compétences techniques
- ✅ Spring Boot / Java (stack Libriciel)
- ✅ JWT / OAuth2-like authentication
- ✅ REST API design
- ✅ Swagger/OpenAPI documentation
- ✅ Tests unitaires (JUnit)
- ✅ Docker / containerisation

### Compétences DevRel
- ✅ Documentation claire et complète
- ✅ Exemples de code multi-langages (Java, Python, JavaScript)
- ✅ Collection Postman professionnelle
- ✅ Guide d'intégration étape par étape
- ✅ Expérience développeur optimisée

### Compréhension métier
- ✅ Workflow documentaire administratif
- ✅ Flux de dématérialisation
- ✅ Interopérabilité système
- ✅ Secteur public / collectivités

---

## 📤 Publier sur GitHub

### Étape 1 : Créer le repository sur GitHub

1. Allez sur https://github.com/Malik971
2. Cliquez sur "New repository"
3. Nom : `libriciel-partner-api-demo`
4. Description : "API de démonstration pour l'intégration partenaire Libriciel - Candidature DevRel"
5. Public
6. Créez le repository

### Étape 2 : Pousser le code

```bash
cd libriciel-partner-api-demo
git init
git add .
git commit -m "Initial commit - Libriciel Partner API Demo

Projet de démonstration créé dans le cadre de ma candidature au poste de DevRel chez Libriciel SCOP.

Fonctionnalités:
- API REST Spring Boot simulant l'écosystème Pastell
- Authentification JWT
- Documentation Swagger complète
- Collection Postman
- Guides d'intégration multi-langages
- Tests unitaires

Ce projet démontre ma compréhension du rôle DevRel: faciliter les intégrations techniques à travers une documentation claire et une expérience développeur optimale."

git branch -M main
git remote add origin https://github.com/Malik971/libriciel-partner-api-demo.git
git push -u origin main
```

### Étape 3 : Améliorer le README GitHub

Sur GitHub, ajoutez en haut du README.md :

```markdown
# 🔌 Libriciel Partner API - Demo

[![Java](https://img.shields.io/badge/Java-17-orange)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.0-brightgreen)](https://spring.io/projects/spring-boot)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

> Projet de démonstration créé par **Malik Ibo** pour sa candidature au poste de **DevRel** chez **Libriciel SCOP**
```

---

## 💡 Questions fréquentes

### "Dois-je vraiment comprendre tout le code ?"
**Oui, mais pas ligne par ligne.**

Vous devez comprendre :
- ✅ L'architecture globale (couches : controller → service → data)
- ✅ Comment fonctionne l'authentification JWT
- ✅ Le workflow documentaire
- ✅ Pourquoi c'est pertinent pour Libriciel

### "Et si on me pose une question technique pointue ?"
**Soyez honnête.**

Exemple :
> "Je n'ai pas implémenté X dans cette démo, mais en production, je l'ajouterais en faisant Y. C'est une limitation volontaire pour garder le projet simple et compréhensible."

### "Dois-je lancer l'app pendant l'entretien ?"
**Oui, si vous présentez en partage d'écran.**

Préparez :
1. App lancée avant l'entretien
2. Swagger ouvert
3. Postman ouvert avec collection
4. Code ouvert dans IDE

Si c'est un entretien en présentiel, montrez sur votre laptop.

### "Dois-je mentionner ce projet dans ma lettre de motivation ?"
**Absolument !**

Exemple :
> "Pour mieux comprendre le rôle DevRel chez Libriciel, j'ai créé un projet de démonstration qui simule l'intégration d'un partenaire avec une API type Pastell. Ce projet illustre ma compréhension du rôle : faciliter les intégrations techniques à travers une documentation claire, des exemples concrets, et une expérience développeur optimale.
>
> 🔗 GitHub : https://github.com/Malik971/libriciel-partner-api-demo"

---

## ✅ Checklist finale

### Avant de publier sur GitHub
- [ ] Tester que `./start.sh` fonctionne
- [ ] Tester que l'app se lance correctement
- [ ] Vérifier que Swagger s'ouvre
- [ ] Tester la collection Postman
- [ ] Relire le README
- [ ] Vérifier qu'il n'y a pas de secrets dans le code

### Avant l'entretien
- [ ] App testée et fonctionnelle
- [ ] Onglets préparés (Swagger, code, docs)
- [ ] Postman avec collection importée
- [ ] Relire PRESENTATION_GUIDE.md
- [ ] Préparer 2-3 phrases clés par section
- [ ] Lien GitHub prêt à partager

### Pendant l'entretien
- [ ] Montrer, ne pas lire
- [ ] Aller à l'essentiel (5-7 min max)
- [ ] Relier chaque élément au poste DevRel
- [ ] Être enthousiaste mais professionnel
- [ ] Proposer de leur envoyer le lien GitHub

---

## 🎯 Messages clés à faire passer

1. **"Je comprends le rôle DevRel"**
   → Pas juste du code, c'est faciliter l'intégration

2. **"Je comprends Libriciel"**
   → Interopérabilité, secteur public, Pastell, logiciel libre

3. **"Je sais documenter"**
   → README, guides, exemples multi-langages, Postman

4. **"Je suis technique"**
   → Spring Boot, JWT, REST, tests, Docker

5. **"Je pense partenaires"**
   → Expérience développeur, onboarding, facilitation

---

## 🚀 Prochaines étapes

1. **Testez le projet** : `./start.sh`
2. **Lisez PRESENTATION_GUIDE.md** en détail
3. **Préparez votre discours** (5-7 min)
4. **Publiez sur GitHub**
5. **Mentionnez-le dans votre candidature**
6. **Préparez-vous à le présenter en entretien**

---

**Malik, ce projet est votre meilleur atout. Il montre que vous ne vous contentez pas de dire que vous comprenez le rôle DevRel - vous le démontrez concrètement. Bonne chance pour votre entretien chez Libriciel ! 🚀**

---

## 📞 Besoin d'aide ?

Si vous avez des questions sur le projet :
1. Relisez les docs (README, ARCHITECTURE, INTEGRATION_GUIDE)
2. Testez avec Postman
3. Regardez les exemples de code
4. Consultez les tests unitaires

**Vous avez tout ce qu'il faut pour réussir ! 💪**

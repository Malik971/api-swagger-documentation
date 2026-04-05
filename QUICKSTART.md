# ⚡ Démarrage ultra-rapide

> **3 minutes pour tester l'API**

---

## Prérequis
- Java 17+
- Maven 3.8+

---

## Démarrage en 3 commandes

### Linux / Mac
```bash
./start.sh
```

### Windows
```bash
start.bat
```

### Méthode manuelle
```bash
mvn clean install
mvn spring-boot:run
```

---

## Test en 3 étapes

### 1. Obtenir un token
```bash
curl -X POST http://localhost:8080/api/v1/auth/token \
  -H "Content-Type: application/json" \
  -d '{"clientId":"partner-demo","clientSecret":"secret123"}'
```

Sauvegardez le `accessToken` !

### 2. Créer un document
```bash
curl -X POST http://localhost:8080/api/v1/documents \
  -H "Authorization: Bearer VOTRE_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "titre": "Test Document",
    "type": "DELIBERATION",
    "statut": "BROUILLON"
  }'
```

### 3. Lister les documents
```bash
curl http://localhost:8080/api/v1/documents \
  -H "Authorization: Bearer VOTRE_TOKEN"
```

---

## Méthode alternative : Postman

1. Importez `libriciel-partner-api.postman_collection.json`
2. Exécutez "1. Obtenir un token"
3. Le token sera automatiquement sauvegardé
4. Testez les autres requêtes !

---

## Méthode alternative : Swagger UI

Ouvrez : **http://localhost:8080/swagger-ui.html**

Testez directement dans le navigateur ! 🚀

---

## Docker (optionnel)

```bash
docker-compose up
```

---

**Pour en savoir plus** : Consultez `README.md` et `INTEGRATION_GUIDE.md`

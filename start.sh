#!/bin/bash

# Script de démarrage rapide pour Libriciel Partner API Demo
# Ce script vérifie les prérequis et lance l'application

set -e

echo "======================================================================"
echo "🚀 Libriciel Partner API Demo - Démarrage rapide"
echo "======================================================================"
echo ""

# Fonction pour vérifier si une commande existe
command_exists() {
    command -v "$1" >/dev/null 2>&1
}

# Vérification de Java
echo "📋 Vérification des prérequis..."
echo ""

if ! command_exists java; then
    echo "❌ Java n'est pas installé"
    echo "   Veuillez installer Java 17 ou supérieur"
    exit 1
fi

JAVA_VERSION=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}' | cut -d'.' -f1)
if [ "$JAVA_VERSION" -lt 17 ]; then
    echo "⚠️  Java $JAVA_VERSION détecté - Java 17+ recommandé"
else
    echo "✅ Java $JAVA_VERSION détecté"
fi

# Vérification de Maven
if ! command_exists mvn; then
    echo "❌ Maven n'est pas installé"
    echo "   Veuillez installer Maven 3.8+"
    exit 1
fi

echo "✅ Maven détecté"
echo ""

# Compilation et démarrage
echo "🔨 Compilation du projet..."
mvn clean install -DskipTests

echo ""
echo "======================================================================"
echo "✅ Compilation réussie !"
echo "======================================================================"
echo ""
echo "🚀 Démarrage de l'application..."
echo ""
echo "📖 Documentation Swagger : http://localhost:8080/swagger-ui.html"
echo "🔗 API Docs : http://localhost:8080/api-docs"
echo "✅ Health Check : http://localhost:8080/api/v1/health"
echo ""
echo "⏹️  Pour arrêter : Ctrl+C"
echo ""
echo "======================================================================"
echo ""

mvn spring-boot:run

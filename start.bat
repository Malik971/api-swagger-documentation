@echo off
REM Script de démarrage rapide pour Windows

echo ======================================================================
echo 🚀 Libriciel Partner API Demo - Démarrage rapide
echo ======================================================================
echo.

echo 📋 Vérification des prérequis...
echo.

REM Vérification de Java
where java >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo ❌ Java n'est pas installé
    echo    Veuillez installer Java 17 ou supérieur
    pause
    exit /b 1
)

echo ✅ Java détecté

REM Vérification de Maven
where mvn >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo ❌ Maven n'est pas installé
    echo    Veuillez installer Maven 3.8+
    pause
    exit /b 1
)

echo ✅ Maven détecté
echo.

echo 🔨 Compilation du projet...
call mvn clean install -DskipTests

if %ERRORLEVEL% NEQ 0 (
    echo ❌ Erreur lors de la compilation
    pause
    exit /b 1
)

echo.
echo ======================================================================
echo ✅ Compilation réussie !
echo ======================================================================
echo.
echo 🚀 Démarrage de l'application...
echo.
echo 📖 Documentation Swagger : http://localhost:8080/swagger-ui.html
echo 🔗 API Docs : http://localhost:8080/api-docs
echo ✅ Health Check : http://localhost:8080/api/v1/health
echo.
echo ⏹️  Pour arrêter : Ctrl+C
echo.
echo ======================================================================
echo.

call mvn spring-boot:run

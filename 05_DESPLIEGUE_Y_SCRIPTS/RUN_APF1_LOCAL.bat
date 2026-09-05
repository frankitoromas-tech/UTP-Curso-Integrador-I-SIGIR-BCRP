@echo off
chcp 65001 >nul
title SIGIR-BCRP - Lanzador Inteligente de Avance de Proyecto Final 1
cls

echo ==============================================================================
echo   UNIVERSIDAD TECNOLOGICA DEL PERU (UTP) - CURSO INTEGRADOR I
echo   PROYECTO: SIGIR - BCRP (Sistema de Gestion de Incidentes Regulatorios)
echo   LIDER TECNICO: Frank Emiliano Vargas Huaman ^| Docente: Yony Zamata Condori
echo ==============================================================================
echo.

set "BASE_DIR=%~dp0.."
set "FRONTEND_DIR=%BASE_DIR%\04_FRONTEND_UI"
set "BACKEND_DIR=%BASE_DIR%\03_BACKEND_SPRINGBOOT"

echo [VERIFICACION PREVIA DE COMPONENTES]:
if exist "%BASE_DIR%\01_DOCUMENTACION_TECNICA\INFORME_ACADEMICO_APF1_COMPLETO.docx" (
    echo  [OK] Informe Academico Word APA 7 (Canvas UTP)
)
if exist "%BASE_DIR%\01_DOCUMENTACION_TECNICA\INFORME_ACADEMICO_APF1_COMPLETO.pdf" (
    echo  [OK] Informe Academico PDF Listo con Diagramas BPMN/UML (Canvas UTP)
)
if exist "%BASE_DIR%\02_BASE_DE_DATOS\01_SCHEMA_DDL_POSTGRESQL.sql" (
    echo  [OK] Scripts DDL con Inmutabilidad Append-Only e Indice Anti-Carreras
)
if exist "%BACKEND_DIR%\pom.xml" (
    echo  [OK] Backend Spring Boot 3 con Puerto IIncidenteService y Telemetria Paralela
)
if exist "%FRONTEND_DIR%\index.html" (
    echo  [OK] Consola Web NOC con Cliente API Hibrido (Online/Offline)
)

echo.
echo ------------------------------------------------------------------------------
echo   SELECCIONE MODO DE EJECUCION:
echo ------------------------------------------------------------------------------
echo   [1] Consola Web NOC Inmediata (Cliente Hibrido con Auto-Deteccion)
echo   [2] Ejecutar Suite Completa de Pruebas Unitarias Backend (Maven Test)
echo   [3] Iniciar Backend Spring Boot Localmente (mvn spring-boot:run)
echo   [4] Despliegue Empresarial con Docker Compose (PostgreSQL 15 + API + Nginx)
echo   [5] Ver Informe Academico Oficial en PDF (Entregable Canvas UTP)
echo   [6] Abrir Diapositivas de Presentacion PowerPoint (.pptx)
echo   [7] Salir
echo ------------------------------------------------------------------------------
set /p OPCION="Ingrese su opcion [1-7]: "

if "%OPCION%"=="1" goto MODO_FRONTEND
if "%OPCION%"=="2" goto MODO_TESTS
if "%OPCION%"=="3" goto MODO_SPRING
if "%OPCION%"=="4" goto MODO_DOCKER
if "%OPCION%"=="5" goto MODO_INFORME
if "%OPCION%"=="6" goto MODO_PPTX
if "%OPCION%"=="7" goto SALIR

:MODO_PPTX
echo.
echo Abriendo Diapositivas Oficiales PowerPoint...
start "" "%BASE_DIR%\01_DOCUMENTACION_TECNICA\PRESENTACION_EJECUTIVA_APF1.pptx"
goto FIN

:MODO_INFORME
echo.
echo Abriendo Informe Academico Oficial en PDF (Canvas UTP)...
start "" "%BASE_DIR%\01_DOCUMENTACION_TECNICA\INFORME_ACADEMICO_APF1_COMPLETO.pdf"
goto FIN

:MODO_FRONTEND
echo.
echo Abriendo Consola Web NOC en el navegador...
start "" "%FRONTEND_DIR%\index.html"
goto FIN

:MODO_TESTS
echo.
echo Ejecutando bateria de pruebas automatizadas con Maven...
cd /d "%BACKEND_DIR%"
mvn test
echo.
pause
goto FIN

:MODO_SPRING
echo.
echo Iniciando Backend Spring Boot 3 en nueva ventana...
start "SIGIR Backend API (Puerto 8080)" cmd /k "cd /d "%BACKEND_DIR%" && mvn spring-boot:run"
echo Esperando 5 segundos para apertura de la Consola Web...
timeout /t 5 >nul
start "" "%FRONTEND_DIR%\index.html"
goto FIN

:MODO_DOCKER
echo.
echo Desplegando orquestacion Docker Compose multi-contenedor...
cd /d "%~dp0"
docker-compose up -d --build
echo.
echo Servicios levantados. Abriendo Consola Web y Documentacion Swagger...
timeout /t 6 >nul
start "" "http://localhost"
start "" "http://localhost:8080/swagger-ui.html"
goto FIN

:FIN
echo.
echo ==============================================================================
echo   SIGIR-BCRP inicializado exitosamente. Listo para sustentacion.
echo ==============================================================================
pause

:SALIR
exit /b 0

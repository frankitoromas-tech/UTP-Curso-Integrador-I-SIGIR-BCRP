# ==============================================================================
# UNIVERSIDAD TECNOLOGICA DEL PERU (UTP) - CURSO INTEGRADOR I
# PROYECTO: SIGIR - BCRP (Sistema de Gestion de Incidentes Regulatorios)
# LIDER TECNICO: Frank Emiliano Vargas Huaman | Docente: Yony Zamata Condori
# ==============================================================================

[Console]::OutputEncoding = [System.Text.Encoding]::UTF8
$OutputEncoding = [System.Text.Encoding]::UTF8

$BaseDir = Split-Path -Parent $PSScriptRoot
$FrontendIndex = Join-Path $BaseDir "04_FRONTEND_UI\index.html"
$BackendDir = Join-Path $BaseDir "03_BACKEND_SPRINGBOOT"

Clear-Host
Write-Host "==============================================================================" -ForegroundColor Cyan
Write-Host "   SIGIR - BCRP: Lanzador de Gestion y Verificacion (APF1)" -ForegroundColor Yellow
Write-Host "   Lider Tecnico: Frank Emiliano Vargas Huaman | Seccion 57524" -ForegroundColor White
Write-Host "==============================================================================" -ForegroundColor Cyan
Write-Host ""

$docsMdOk = Test-Path (Join-Path $BaseDir "01_DOCUMENTACION_TECNICA\INFORME_ACADEMICO_APF1_COMPLETO.md")
$docsDocxOk = Test-Path (Join-Path $BaseDir "01_DOCUMENTACION_TECNICA\INFORME_ACADEMICO_APF1_COMPLETO.docx")
$docsPdfOk = Test-Path (Join-Path $BaseDir "01_DOCUMENTACION_TECNICA\INFORME_ACADEMICO_APF1_COMPLETO.pdf")
$dbOk = Test-Path (Join-Path $BaseDir "02_BASE_DE_DATOS\01_SCHEMA_DDL_POSTGRESQL.sql")
$backendOk = Test-Path (Join-Path $BackendDir "pom.xml")
$frontendOk = Test-Path $FrontendIndex

Write-Host "[-] Informe Maestro (Markdown / IEEE 830): " -NoNewline
if ($docsMdOk) { Write-Host "[OK]" -ForegroundColor Green } else { Write-Host "[FALTA]" -ForegroundColor Red }

Write-Host "[-] Informe Oficial UTP en Formato Word:  " -NoNewline
if ($docsDocxOk) { Write-Host "[OK] (Listo para Canvas)" -ForegroundColor Green } else { Write-Host "[FALTA]" -ForegroundColor Red }

Write-Host "[-] Informe Oficial UTP en Formato PDF:   " -NoNewline
if ($docsPdfOk) { Write-Host "[OK] (Listo para Canvas)" -ForegroundColor Green } else { Write-Host "[FALTA]" -ForegroundColor Red }

Write-Host "[-] Base de Datos DDL (Inmutable/ACID):   " -NoNewline
if ($dbOk) { Write-Host "[OK]" -ForegroundColor Green } else { Write-Host "[FALTA]" -ForegroundColor Red }

Write-Host "[-] Backend Spring Boot (IIncidenteService):" -NoNewline
if ($backendOk) { Write-Host "[OK]" -ForegroundColor Green } else { Write-Host "[FALTA]" -ForegroundColor Red }

Write-Host "[-] Frontend Consola NOC (Cliente Hibrido):" -NoNewline
if ($frontendOk) { Write-Host "[OK]" -ForegroundColor Green } else { Write-Host "[FALTA]" -ForegroundColor Red }

Write-Host ""
Write-Host "------------------------------------------------------------------------------" -ForegroundColor Gray
Write-Host " SELECCIONE MODO DE EJECUCION:" -ForegroundColor White
Write-Host " [1] Abrir Consola Web NOC (Cliente Hibrido Reactivo)" -ForegroundColor Cyan
Write-Host " [2] Ejecutar Suite de Pruebas Unitarias Automatizadas (Maven Test)" -ForegroundColor Cyan
Write-Host " [3] Iniciar Backend Spring Boot 3 Localmente (mvn spring-boot:run)" -ForegroundColor Cyan
Write-Host " [4] Despliegue Empresarial Docker Compose (PostgreSQL 15 + API + Nginx)" -ForegroundColor Cyan
Write-Host " [5] Ver Guia Maestra de Sustentacion Oral (Diapositivas, Speech y FAQ)" -ForegroundColor Magenta
Write-Host " [6] Salir" -ForegroundColor Gray
Write-Host "------------------------------------------------------------------------------" -ForegroundColor Gray

$opcion = Read-Host "Ingrese opcion [1-6]"

switch ($opcion) {
    "1" {
        Write-Host "`nLanzando interfaz NOC interactiva en el navegador..." -ForegroundColor Green
        Start-Process $FrontendIndex
    }
    "2" {
        Write-Host "`nEjecutando pruebas con Maven..." -ForegroundColor Yellow
        Push-Location $BackendDir
        mvn test
        Pop-Location
    }
    "3" {
        Write-Host "`nIniciando Spring Boot Backend..." -ForegroundColor Green
        Start-Process powershell -ArgumentList "-NoExit", "-Command", "Set-Location '$BackendDir'; mvn spring-boot:run"
        Start-Sleep -Seconds 5
        Start-Process $FrontendIndex
    }
    "4" {
        Write-Host "`nIniciando Docker Compose..." -ForegroundColor Yellow
        $composeFile = Join-Path $PSScriptRoot "docker-compose.yml"
        docker-compose -f $composeFile up -d --build
        Start-Sleep -Seconds 6
        Start-Process "http://localhost"
        Start-Process "http://localhost:8080/swagger-ui.html"
    }
    "5" {
        Write-Host "`nAbriendo Guia Maestra de Sustentacion Oral..." -ForegroundColor Magenta
        $guiaPath = Join-Path $BaseDir "SUSTENTACION_ORAL_APF1.md"
        Start-Process $guiaPath
    }
    Default {
        Write-Host "`nSaliendo..." -ForegroundColor Gray
    }
}

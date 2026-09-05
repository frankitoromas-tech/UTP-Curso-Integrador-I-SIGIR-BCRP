# 🔴 (AC-S04-APF1) Avance de Proyecto Final 1 - Curso Integrador I: Sistemas Software - Sección 57524

**Universidad Tecnológica del Perú (UTP) - Sede Lima**  
**Facultad de Ingeniería | Carrera de Ingeniería de Sistemas e Informática**  
- **Docente:** Ing. Yony Zamata Condori  
- **Líder Técnico / Estudiante:** Frank Emiliano Vargas Huamán  
- **Caso:** Regulación Pagos Digitales - BCRP (Servicios UTP)  
- **Ciclo Académico:** 7mo Ciclo (2026)  

---

> ### 📄 ENTREGABLE ACADÉMICO OFICIAL: [INFORME ACADÉMICO APF1 EN PDF](01_DOCUMENTACION_TECNICA/INFORME_ACADEMICO_APF1_COMPLETO.pdf) | [FORMATO WORD APA 7](01_DOCUMENTACION_TECNICA/INFORME_ACADEMICO_APF1_COMPLETO.docx)
> **¡Entregable principal para la evaluación Canvas UTP!** Ubicado en `01_DOCUMENTACION_TECNICA/`.  
> Contiene el informe académico exhaustivo con carátula UTP, índice dinámico, alineación institucional con los 5 aspectos y BMC, evaluación de 3 alternativas (>=50% Java y 5 pantallas), Project Charter, WBS a 3 niveles, Gantt, especificación SRS IEEE 830, modelos BPMN 2.0 y Arquitectura Hexagonal.

---

## 📌 1. Visión General del Proyecto

El proyecto **SIGIR - BCRP** (*Sistema Integral de Gestión de Incidentes Regulatorios para Pagos Digitales*) es una solución empresarial de alta disponibilidad concebida para monitorear, categorizar y reportar de forma automatizada las contingencias e interrupciones en el sistema de transferencias inmediatas del Perú. Integra a participantes clave como **BCP (Yape)**, **Interbank (Plim)**, **Caja Municipal Cusco (Tunki)**, **Caja Rural de los Andes** y la **Cámara de Compensación Electrónica (CCE)**.

Este repositorio contiene la cimentación completa del **Avance de Proyecto Final 1 (APF1)**, cubriendo al 100% las directivas de la **Etapa 01** y los entregables requeridos en la rúbrica de evaluación de la Semana 04.

---

## 🗂️ 2. Mapa y Estructura de Entregables

```
🔴 (AC-S04-APF1) Avance de Proyecto Final 1 - Curso Integrador I - Sistemas Software - Sección 57524/
│
├── 📁 01_DOCUMENTACION_TECNICA/
│   ├── INFORME_ACADEMICO_APF1_COMPLETO.pdf    <-- Documento PDF oficial compilado con carátula UTP, índice dinámico y diagramas BPMN/UML (Listo para Canvas)
│   ├── INFORME_ACADEMICO_APF1_COMPLETO.docx   <-- Documento Word oficial en formato APA 7 con índice dinámico y figuras vectoriales (Editable)
│   ├── INFORME_ACADEMICO_APF1_COMPLETO.md     <-- Documento maestro con carátula UTP, marco institucional, RF/RNF, BPMN, UML y arquitectura
│   ├── 📁 imagenes/                           <-- Diagramas exportados en alta resolución (BPMN 2.0, UML Casos de Uso, Clases, Secuencias, Arquitectura Hexagonal y Consola NOC)
│   ├── PLANIFICACION_SCRUM_CRONOGRAMA.md      <-- WBS/EDT, Product Backlog, Sprints, Matriz RACI y Cronograma Gantt
│   ├── MODELO_PROCESOS_BPMN_2_0.md            <-- Diagramación y análisis comparativo AS-IS vs TO-BE en BPMN 2.0
│   ├── ESPECIFICACION_REQUISITOS_IEEE830.md   <-- Fichas técnicas de Requisitos Funcionales (RF-01 a 12) y No Funcionales (RNF-01 a 08)
│   └── ARQUITECTURA_Y_DISENO_SISTEMA.md       <-- Arquitectura Hexagonal, diagramas UML (Clases, Secuencia, Componentes) y diseño UI/UX
│
├── 📁 02_BASE_DE_DATOS/
│   ├── 01_SCHEMA_DDL_POSTGRESQL.sql            <-- Script DDL completo (tablas, constraints, claves foráneas, índices y comentarios)
│   ├── 02_SEED_DATA_INICIAL.sql                <-- Datos semilla (entidades financieras peruanas, categorías normativas e incidentes)
│   └── DICCIONARIO_DE_DATOS.md                 <-- Diccionario formal detallando campos, tipos de datos y reglas de integridad
│
├── 📁 03_BACKEND_SPRINGBOOT/                   <-- Código Fuente Backend (Java 17 / Spring Boot 3.3.3)
│   ├── pom.xml                                 <-- Dependencias: Spring Web, Data JPA, Security, LDAP/AD, PostgreSQL, OpenAPI Swagger
│   ├── src/main/java/pe/gob/bcrp/sigir/
│   │   ├── SigirApplication.java               <-- Clase principal con @EnableScheduling
│   │   ├── domain/entity/                      <-- Entidades JPA (Incidente, EntidadFinanciera, CategoriaIncidente, HistorialEstado)
│   │   ├── domain/enums/                       <-- Enums: EstadoIncidente, Severidad, OrigenDeteccion
│   │   ├── repository/                         <-- Repositorios Spring Data JPA con JPQL optimizado
│   │   ├── service/                            <-- Lógica de negocio (IncidenteService, SftReportService, EntidadFinancieraService)
│   │   ├── worker/                             <-- TelemetriaWorker (Polling de salud continuo cada 30s con auto-detección 5xx)
│   │   ├── security/                           <-- Configuración Spring Security con Active Directory / LDAP
│   │   └── web/                                <-- Controladores REST y DTOs con documentación OpenAPI
│   └── src/main/resources/
│       └── application.yml                     <-- Configuración de entorno y perfiles
│
├── 📁 04_FRONTEND_UI/                          <-- Consola NOC y Dashboard Web Interactivo
│   ├── index.html                              <-- SPA moderna con semáforos de salud en vivo, filtros y modales
│   ├── css/styles.css                          <-- Estilos de élite: modo oscuro corporativo BCRP, tipografía Plus Jakarta Sans
│   └── js/app.js                               <-- Lógica funcional de sondeo en vivo, transiciones de estado y exportación TXT
│
├── 📁 05_DESPLIEGUE_Y_SCRIPTS/                 <-- Instaladores y Automatización de Despliegue
│   ├── docker-compose.yml                      <-- Orquestación multi-contenedor (PostgreSQL 15 + Backend + Frontend Nginx)
│   ├── Dockerfile_Backend                      <-- Construcción multi-stage optimizada con Eclipse Temurin JDK 17
│   ├── Dockerfile_Frontend                     <-- Servidor web ligero Nginx Alpine
│   ├── RUN_APF1_LOCAL.bat                      <-- Lanzador rápido en 1 clic para Windows
│   └── RUN_APF1_LOCAL.ps1                      <-- Script de verificación y lanzamiento en PowerShell
│
└── README.md                                   <-- Esta guía de navegación
```

---

## 🎯 3. Matriz de Cumplimiento de Requerimientos del Caso

| Requerimiento del Caso de Estudio (Docente Yony Zamata) | Evidencia Técnica en el Proyecto |
| :--- | :--- |
| **Módulo de Mantenimiento** | Controladores y entidades para gestión de bancos, billeteras y categorías en `03_BACKEND_SPRINGBOOT` y `02_BASE_DE_DATOS`. |
| **Módulo de Gestión de Incidentes** | Lógica desacoplada vía puerto `IIncidenteService` con máquina de estados y tickets atómicos correlativos (`seq_ticket_incidente`). |
| **Módulo de Consulta y Actualización de Estados** | Consola web reactiva híbrida en `04_FRONTEND_UI` (Online/Offline) y bitácora de auditoría inmutable custodiada por trigger PostgreSQL (`trg_prohibir_mutacion_historial`). |
| **Conexión automática para recuperar caídas de servicio** | `TelemetriaWorker.java` con sondeo paralelo asíncrono (`CompletableFuture`) cada 30s con detección automática HTTP 5xx y timeouts. |
| **Mantener un solo login (AD)** | `SecurityConfig.java` configurado con `ActiveDirectoryLdapAuthenticationProvider` para inicio de sesión unificado y RBAC institucional. |
| **Generación de Reporte Normativo BCRP** | `SftReportService.java` genera el archivo plano delimitado por pipes conforme a la circular BCRP con hash criptográfico SHA-256 de 64 caracteres. |
| **Entregables APF1 (Planificación, BPMN, UML, DDL, Código, Pruebas, Instaladores)** | Carpetas `01_DOCUMENTACION_TECNICA`, `02_BASE_DE_DATOS`, `03_BACKEND_SPRINGBOOT` (8 tests aprobados) y `05_DESPLIEGUE_Y_SCRIPTS`. |

---

## 🚀 4. Guía de Ejecución Rápida

### Opción A: Lanzador Inteligente Unificado (Recomendada)
1. Ingrese a la carpeta `05_DESPLIEGUE_Y_SCRIPTS`.
2. Ejecute **`RUN_APF1_LOCAL.bat`** (doble clic) o **`RUN_APF1_LOCAL.ps1`** en PowerShell.
3. El lanzador presenta un menú interactivo:
   - **[1] Consola Web NOC Inmediata:** Abre el dashboard interactivo con cliente híbrido auto-detectable.
   - **[2] Ejecutar Pruebas Automatizadas:** Corre la suite completa de 8 pruebas unitarias con Maven (`mvn test`).
   - **[3] Iniciar Backend Spring Boot Localmente:** Compila y levanta la API en el puerto 8080.
   - **[4] Despliegue Empresarial Docker Compose:** Orquesta PostgreSQL 15, Spring Boot y Nginx.

### Opción B: Despliegue Manual con Docker Compose
1. Asegúrese de tener Docker Desktop activo.
2. Desde una terminal en la carpeta `05_DESPLIEGUE_Y_SCRIPTS`, ejecute:
   ```bash
   docker-compose up -d --build
   ```
3. Acceda a:
   - **Consola Web NOC:** `http://localhost`
   - **Documentación Swagger API Backend:** `http://localhost:8080/swagger-ui.html`
   - **Base de Datos PostgreSQL:** Puerto `5432` (Base de datos: `bcrp_incident_db`)


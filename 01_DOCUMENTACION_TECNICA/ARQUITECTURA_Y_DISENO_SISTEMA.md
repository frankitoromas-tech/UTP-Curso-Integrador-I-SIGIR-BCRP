# ARQUITECTURA Y DISEÑO TÉCNICO DEL SISTEMA
**Proyecto:** Sistema Integral de Gestión de Incidentes Regulatorios (SIGIR - BCRP)  
**Curso:** Curso Integrador I: Sistemas Software (Sección 57524)  
**Docente:** Ing. Yony Zamata Condori  
**Estudiante:** Frank Emiliano Vargas Huamán  

---

## 1. ESTILO ARQUITECTÓNICO: ARQUITECTURA HEXAGONAL (PORTS & ADAPTERS)

El sistema **SIGIR - BCRP** está fundamentado en los principios de la **Clean Architecture** y la **Arquitectura Hexagonal (Puertos y Adaptadores)** formulada por Alistair Cockburn. Este diseño garantiza que el núcleo de las reglas de negocio (entidades, ciclo de vida del incidente, validación de formatos BCRP) permanezca completamente aislado e independiente de los frameworks, bases de datos, protocolos de red y detalles de infraestructura.

```
       +---------------------------------------------------------------+
       |                      CAPA DE ADAPTADORES                      |
       |                                                               |
       |  +--------------------+             +----------------------+  |
       |  | Adaptador Web REST |             |  Worker Telemetría   |  |
       |  | (Spring MVC Cont.) |             | (Health Check Poller)|  |
       |  +---------+----------+             +----------+-----------+  |
       |            |                                   |              |
       |            v [Puerto de Entrada]               v              |
       |  +---------------------------------------------------------+  |
       |  |                CAPA DE APLICACIÓN                       |  |
       |  |   - IncidenteService     - EntidadFinancieraService     |  |
       |  |   - SftReportService     - ActiveDirectoryAuthService   |  |
       |  +----------------------------+----------------------------+  |
       |                               |                               |
       |                               v                               |
       |  +---------------------------------------------------------+  |
       |  |                  CAPA DE DOMINIO                        |  |
       |  |   - Incidente (Entity)   - EntidadFinanciera (Entity)   |  |
       |  |   - CategoriaIncidente   - HistorialEstado (Value Obj)  |  |
       |  |   - EstadoIncidente      - Severidad, OrigenDeteccion   |  |
       |  +----------------------------+----------------------------+  |
       |                               |                               |
       |            +------------------+------------------+            |
       |            | [Puerto de Salida]                  |            |
       |            v                                     v            |
       |  +--------------------+             +----------------------+  |
       |  |   Adaptador JPA    |             |  Adaptador AD / LDAP |  |
       |  | (PostgreSQL Repo)  |             |  (Spring Security)   |  |
       |  +--------------------+             +----------------------+  |
       +---------------------------------------------------------------+
```

---

## 2. DIAGRAMA DE COMPONENTES DEL SISTEMA (UML)

```mermaid
flowchart TB
    subgraph CLIENT_LAYER ["Capa de Presentación / Cliente"]
        UI_SPA["🖥️ Frontend Web SPA (Vite + Vanilla CSS / JS Moderno)"]
        NOC_DASH["📊 Dashboard NOC Semáforos en Tiempo Real"]
        FORM_INC["📝 Formulario Asistido Registro / Clasificación"]
        UI_SPA --> NOC_DASH
        UI_SPA --> FORM_INC
    end

    subgraph API_GATEWAY ["Capa de Exposición y Seguridad"]
        SEC_FILTER["🛡️ Spring Security 6 / RBAC Corporativo"]
        AD_CONNECTOR["🏢 Conector Active Directory (LDAPS / Puerto 389/636)"]
        SEC_FILTER --> AD_CONNECTOR
    end

    subgraph BACKEND_CORE ["Capa de Puertos de Servicio y Lógica de Negocio"]
        INC_CTRL["🎮 IncidenteController (REST OpenAPI v3)"]
        ENT_CTRL["🎮 EntidadFinancieraController"]
        REP_CTRL["🎮 ReporteBCRPController"]
        
        INC_PORT["🔌 IIncidenteService (Inbound Port)"]
        INC_SVC["⚙️ IncidenteService (Implementación Núcleo)"]
        SFT_SVC["⚙️ SftReportService (Compilador Normativo SHA-256)"]
        WORKER["⏱️ TelemetriaWorker (Sondeo Paralelo Asíncrono CompletableFuture)"]
        
        INC_CTRL --> INC_PORT
        INC_PORT --> INC_SVC
        ENT_CTRL --> INC_SVC
        REP_CTRL --> SFT_SVC
        WORKER --> INC_PORT
    end

    subgraph PERSISTENCE_LAYER ["Capa de Persistencia e Inmutabilidad Forense"]
        JPA_REPO["💾 Repositorios Spring Data JPA"]
        POSTGRES[("🐘 PostgreSQL 15 (Trigger Append-Only + Índice Parcial Anti-Carreras)")]
        INC_SVC --> JPA_REPO
        SFT_SVC --> JPA_REPO
        JPA_REPO --> POSTGRES
    end

    subgraph EXTERNAL_SYSTEMS ["Ecosistema Financiero Supervisado"]
        YAPE["📱 API BCP (Yape)"]
        PLIM["📱 API Interbank (Plim)"]
        CCE["🏦 Switch CCE"]
        TUNKI["📱 API Caja Cusco (Tunki)"]
        BCRP_SFTP["🏛️ Mesa Regulatoria BCRP (SFT)"]
    end

    UI_SPA -- "HTTP / REST JSON (Cliente Híbrido)" --> SEC_FILTER
    SEC_FILTER --> INC_CTRL
    SEC_FILTER --> ENT_CTRL
    SEC_FILTER --> REP_CTRL
    WORKER -- "HTTP GET Health Polling Paralelo" --> YAPE
    WORKER -- "HTTP GET Health Polling Paralelo" --> PLIM
    WORKER -- "HTTP GET Health Polling Paralelo" --> CCE
    WORKER -- "HTTP GET Health Polling Paralelo" --> TUNKI
    SFT_SVC -- "Envío TXT Normativo" --> BCRP_SFTP
```

---

## 3. DIAGRAMA DE SECUENCIA: CLASIFICACIÓN MANUAL DE INCIDENTES

Describe el flujo en el que un operador humano tipifica una contingencia no detectable por sondas (fraude financiero o manipulación de información):

```mermaid
sequenceDiagram
    autonumber
    actor Operador as Operador NOC / Analista
    participant UI as Frontend Web NOC (Cliente Híbrido)
    participant Auth as Spring Security 6 (AD Provider)
    participant Ctrl as IncidenteController
    participant Port as IIncidenteService
    participant Svc as IncidenteService
    participant DB as PostgreSQL 15 (ACID)

    Operador->>UI: Ingresa Credenciales de Red (frank.vargas@bcrp.local)
    UI->>Auth: Petición HTTP con Cabecera de Autorización Corporativa
    Auth->>Auth: Validar contra Active Directory (LDAP / Roles Institucionales)
    Auth-->>UI: Acceso Autorizado (Rol: ROLE_OPERADOR / NOC_LEAD)
    
    Operador->>UI: Completa Formulario Manual (Entidad: BCP, Categoría: FRAUDE_FIN, Detalle)
    UI->>Ctrl: POST /api/v1/incidentes/manual (IncidenteRegistroDTO)
    Ctrl->>Port: registrarIncidenteManual(dto, usuarioAD)
    Port->>Svc: Ejecutar lógica de validación
    
    Svc->>Svc: Validar RN-01 (Sin incidentes duplicados abiertos para la entidad)
    Svc->>Svc: Generar Ticket Atómico Monotónico (INC-YYYYMMDD-XXXXX)
    Svc->>DB: INSERT INTO incidentes (Protegido por Índice Parcial Único)
    DB-->>Svc: Persistido exitosamente
    
    Svc->>DB: INSERT INTO historial_estados (Blindado por Trigger Append-Only)
    Svc-->>Ctrl: IncidenteResponseDTO
    Ctrl-->>UI: 201 Created (JSON con Ticket y Metadatos)
    UI-->>Operador: Mostrar Notificación Toast de Éxito & Actualizar Matriz

```

---

## 4. DIAGRAMA DE ESTADOS DEL CICLO DE VIDA DEL INCIDENTE

El ciclo de vida se rige por una máquina de estados finita determinística:

```mermaid
stateDiagram-v2
    [*] --> REGISTRADO : Auto-Detección 5xx / Registro Manual
    
    REGISTRADO --> EN_EVALUACION : Operador asigna equipo técnico
    EN_EVALUACION --> EN_MITIGACION : Se inician protocolos de contingencia / failover
    EN_MITIGACION --> RESUELTO : Se restablece servicio y valida tráfico
    RESUELTO --> CERRADO : Supervisor BCRP valida reporte y sella tiempos
    
    CERRADO --> [*] : Compilación en archivo normativo SFT BCRP
```

---

## 5. DIAGRAMA DE DESPLIEGUE FÍSICO (DOCKER / INFRAESTRUCTURA)

```mermaid
flowchart TD
    subgraph DOCKER_HOST ["Host de Servidor / Estación de Trabajo (Windows / Linux)"]
        subgraph BRIDGE_NET ["Red Privada Docker: bcrp-network"]
            
            subgraph CONT_UI ["Contenedor: sigir-ui (Nginx:alpine)"]
                NGINX["Nginx Web Server"]
                STATIC_FILES["SPA HTML5 / CSS3 / Vanilla JS"]
                NGINX --> STATIC_FILES
            end

            subgraph CONT_BACKEND ["Contenedor: sigir-api (Java 17 / Spring Boot 3)"]
                APP["JAR Ejecutable: sigir-backend.jar"]
                JVM["Eclipse Temurin OpenJDK 17"]
                JVM --> APP
            end

            subgraph CONT_DB ["Contenedor: sigir-postgres (PostgreSQL 15)"]
                PG_ENGINE["PostgreSQL Engine"]
                PG_VOLUME[("Volumen Persistente: pgdata_incident")]
                PG_ENGINE --> PG_VOLUME
            end

        end
    end

    CLIENT["🌐 Navegador Web del Operador"] -- "HTTP Puerto 80" --> NGINX
    NGINX -- "Proxy Reverso /api" --> JVM
    JVM -- "JDBC Puerto 5432" --> PG_ENGINE
    JVM -- "LDAPS Puerto 636" --> EXT_AD["🏢 Active Directory Corporativo"]
```

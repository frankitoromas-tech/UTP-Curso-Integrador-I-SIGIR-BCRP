# 🏛️ SIGIR - BCRP: Sistema de Gestión de Incidentes Regulatorios para Pagos Digitales
## Informe Académico Integral Consolidado — Avance de Proyecto Final 2 (APF2)
### *Fase de Diseño Detallado de la Solución (Semana 8)*

> **Universidad Tecnológica del Perú (UTP)**  
> **Facultad de Ingeniería | Carrera de Ingeniería de Sistemas e Informática**  
> **Curso:** Curso Integrador I: Sistemas Software (Sección 57524)  
> **Docente Evaluador:** Ing. Yony Zamata Condori  
> **Ciclo:** 7mo Ciclo — Semestre 2026-I  
> **Fecha de Entrega:** Octubre de 2026

### 👥 Equipo de Desarrollo (Estudiantes UTP)

| # | Estudiante | Código UTP | Rol Oficial en el Proyecto | Responsabilidad Técnica Principal |
| :-: | :--- | :---: | :--- | :--- |
| **1** | **Frank Emiliano Vargas Huamán** | `U23243651` | **Líder Técnico, Arquitecto & Especialista en Datos** | Arquitectura Hexagonal, DDL Inmutable PostgreSQL 15, Core Java 17, Spring Boot 3 y Docker. |
| **2** | **Fernando Alber Alfredo Romero Requejo** | `U21216410` | **Ingeniero de Requisitos & Procesos** | Levantamiento SRS IEEE 830, Modelado BPMN 2.0 y Reglas Normativas BCRP. |
| **3** | **Joel Leonardo Olaya Vivas** | `U21221688` | **Ingeniero de Software & QA** | Automatización de Pruebas Unitarias JUnit 5, Validación Mockito, Módulo de Reportes y Criterios de Aceptación. |

---

## 📋 Resumen Ejecutivo de la Entrega APF2

El presente informe consolida en un solo documento oficial la investigación, especificación de requisitos, modelado de procesos, evaluación de alternativas, diseño detallado de arquitectura de software y persistencia inmutable del Sistema Integral de Gestión de Incidentes Regulatorios para Pagos Digitales (SIGIR - BCRP), correspondiente a la entrega del Avance de Proyecto Final 2 (APF2) de la carrera de Ingeniería de Sistemas e Informática de la UTP. El proyecto responde a la imperiosa necesidad del Banco Central de Reserva del Perú (BCRP) de fiscalizar y asegurar la continuidad operacional del ecosistema nacional de transferencias inmediatas e interoperables, integrando a BCP (Yape), Interbank (Plim), Caja Municipal Cusco (Tunki), Caja Rural de los Andes y la Cámara de Compensación Electrónica (CCE). En este Hito APF2 (Semana 8), el equipo culmina la transición hacia el diseño de ingeniería detallado: (1) justificación de 3 alternativas de solución con >= 50% de desarrollo en Java y 15 mockups de pantallas en alta fidelidad; (2) Diagrama Entidad-Relación Físico y Lógico en PostgreSQL 15 con ruteo ortogonal de ingeniería y capítulo formal de seguridad en base de datos (RBAC, TLS/SSL, inmutabilidad append-only y parametrización JPA); (3) Diagrama de Clases de Diseño de Software en Spring Boot 3; (4) Cronograma Gantt Visual de avance; y (5) especificación del Módulo de Reportes Clave de Indisponibilidad y generación del archivo normativo SFT con firma digital SHA-256.

---

## 1. Introducción y Marco Institucional

### 1.1. Contexto del Banco Central de Reserva del Perú (BCRP)

El Banco Central de Reserva del Perú (BCRP) es un organismo constitucionalmente autónomo instituido para salvaguardar la estabilidad monetaria del país. En concordancia con la Ley Orgánica del BCRP (Ley N° 26123) y la Ley de los Sistemas de Pagos y de Liquidación de Valores (Ley N° 29440), la entidad tiene la atribución privativa de regular y supervisar el Sistema Nacional de Pagos. Dicho marco encomienda al Banco Central la misión de garantizar que los canales de compensación y liquidación funcionen de forma segura, continua, eficiente e interoperable.

A partir del despliegue integral del Reglamento de Interoperabilidad de los Servicios de Pago Provistos por las Entidades Financieras (Circular BCRP N° 0011-2023-BCRP), el ecosistema peruano transitó de un modelo fragmentado a una red interconectada en tiempo real. Esta transformación involucra a millones de ciudadanos mediante billeteras móviles digitales (Yape y Plim), entidades microfinancieras (Caja Municipal Cusco y Caja de los Andes) y el switch interbancario central de la Cámara de Compensación Electrónica (CCE). En este escenario de hiperconectividad transaccional, cualquier degradación técnica o caída de servicio genera un impacto económico directo en la población y amenaza la confianza colectiva en el dinero electrónico nacional.

### 1.2. Misión y Visión Institucional del BCRP

"Preservar la estabilidad monetaria. Somos reconocidos como un Banco Central autónomo, moderno, modelo de institucionalidad en el país, de primer nivel internacional, con elevada credibilidad y que mantiene la confianza del público en la moneda nacional." — Visión Institucional del BCRP

"Nuestro personal se encuentra altamente calificado, motivado y comprometido y se desempeña eficientemente en un ambiente de colaboración en el que se comparte información y conocimiento." — Misión Institucional del BCRP

### 1.3. Evolución del Alcance hacia la Semana 8 (Diseño Detallado)

En la transición del hito APF1 al APF2 (Semana 8), el proyecto SIGIR-BCRP ha evolucionado desde la definición del marco estratégico, modelado de procesos AS-IS/TO-BE y arquitectura preliminar, hacia la fase de diseño de ingeniería detallado y formalización técnica de componentes críticos. La solución ahora integra el diseño riguroso del almacenamiento relacional inmutable en PostgreSQL 15, el diseño de clases de software con inyección de dependencias en Spring Boot 3, el módulo de reportes normativos SFT exigido por la Circular BCRP N° 0011-2023, y la especificación de tres alternativas de solución con más del 50% de desarrollo en el ecosistema Java.

---

## 2. Alternativas de Solución TIC (>= 50% Java y 15 Pantallas)

En estricto cumplimiento de la rúbrica de evaluación de APF2, se formularon y diseñaron tres alternativas tecnológicas viables. Cada alternativa cuenta con una justificación cuantitativa y cualitativa de poseer al menos el 50% de desarrollo en el ecosistema Java, así como el diseño de no menos de cinco pantallas representativas de interacción operativa:

| Alternativa de Solución | Stack Tecnológico | Peso Java | Pantallas Diseñadas (5 c/u) |
| --- | :--- | :--- | :--- |
| Alternativa 1:<br>Monolito Hexagonal Modular | Java 17 LTS, Spring Boot 3.3.3, Thymeleaf + HTMX, Spring Security, PostgreSQL 15 | 75% Java<br>(Controladores MVC, Lógica Services, Repositorios JPA, Seguridad en JVM) | A1-P01: Login AD/LDAP<br>A1-P02: Tablero Entidades SSR<br>A1-P03: Registro Ticket Thymeleaf<br>A1-P04: Bitácora Transiciones<br>A1-P05: Exportador SFT Plano |
| Alternativa 2 (SELECCIONADA):<br>Clean API & SPA NOC | Java 17, Spring Boot 3 REST API, WebSocket, React/JS SPA NOC, PostgreSQL 15 Inmutable | 55% Java<br>(Backend integral en Java, Daemons asíncronos, Persistencia JPA; 45% UI) | A2-P01: Dashboard NOC en Vivo<br>A2-P02: Simulador Caídas 503<br>A2-P03: Registro Asistido RN-01<br>A2-P04: Detalle Audit Append-Only<br>A2-P05: Compilador SFT SHA-256 |
| Alternativa 3:<br>Microservicios Event-Driven | Java 17, Spring Cloud Gateway, Apache Kafka, Spring Boot Workers, PostgreSQL 15 | 80% Java<br>(4 Microservicios autónomos Java, Productores/Consumidores Kafka) | A3-P01: Gateway SSO & Tokens AD<br>A3-P02: Monitor Tópicos Kafka<br>A3-P03: Centro Triage y Alarmas<br>A3-P04: Trazas OpenTelemetry<br>A3-P05: Validador SFTP Batch |


### 2.1. Mockups de Pantallas — Alternativa 1: Monolito Hexagonal Modular

![Figura 2.1. Mockups Alternativa 1](imagenes/figura8_mockups_alt1_monolito.png)

*Figura 2.1. Paneles de Interfaz de Usuario diseñados para la Alternativa 1 (Monolito Thymeleaf/HTMX).*

### 2.2. Mockups de Pantallas — Alternativa 2 (Seleccionada): Clean API & SPA NOC

![Figura 2.2. Mockups Alternativa 2](imagenes/figura9_mockups_alt2_clean_api.png)

*Figura 2.2. Paneles de Interfaz de Usuario implementados para la Alternativa 2 (Consola Reactiva NOC BCRP).*

### 2.3. Mockups de Pantallas — Alternativa 3: Microservicios Event-Driven

![Figura 2.3. Mockups Alternativa 3](imagenes/figura10_mockups_alt3_microservicios.png)

*Figura 2.3. Paneles de Interfaz de Usuario diseñados para la Alternativa 3 (Microservicios Kafka).*

---

## 3. Diseño Detallado de la Solución (BD, Clases, Gantt y Seguridad)

### 3.1. Diagrama de Entidad-Relación Físico y Lógico (PostgreSQL 15)

El esquema de persistencia se modeló bajo la Tercera Forma Normal (3NF) y estándares de auditoría forense para el sector financiero. A continuación se incorpora formalmente el DER Físico/Lógico con ruteo ortogonal de ingeniería, eliminando cruces diagonales y reflejando las llaves primarias, foráneas, restricciones de unicidad e índices especializados:

![Figura 3.1. DER PostgreSQL 15](imagenes/figura11_der_fisico_logico_postgresql.png)

*Figura 3.1. Diagrama Entidad-Relación Físico / Lógico (PostgreSQL 15) bajo Arquitectura Inmutable.*

### 3.2. Capítulo Formal de Seguridad en Base de Datos

1. Control de Acceso Basado en Roles (RBAC): El motor PostgreSQL 15 opera bajo una política estricta de mínimo privilegio. Se configuró el rol de aplicación 'sigir_app_user' con permisos exclusivos de SELECT, INSERT y UPDATE sobre entidades operativas, teniendo revocado cualquier acceso a comandos DDL destructivos (DROP, ALTER, TRUNCATE).

2. Conexiones Cifradas (TLS/SSL): La conectividad JDBC entre la API Spring Boot y el servidor PostgreSQL se exige bajo el parámetro 'sslmode=verify-full', garantizando el cifrado de datos en tránsito con certificados TLS 1.3.

3. Inmutabilidad y Auditoría Estricta (Append-Only): Para certificar legalmente el historial de incidentes ante el BCRP, la tabla 'auditoria.historial_estados' implementa un trigger en PL/pgSQL que bloquea cualquier sentencia UPDATE o DELETE:
   CREATE TRIGGER trg_prohibir_mutacion_historial BEFORE UPDATE OR DELETE ON historial_estados
   FOR EACH ROW EXECUTE FUNCTION fn_prohibir_mutacion_historial();

4. Prevención de Inyección SQL (SQLi): El backend utiliza Spring Data JPA e Hibernate con PreparedStatements nativos y bind variables, eliminando cualquier concatenación de cadenas en sentencias SQL.

### 3.3. Diagrama de Clases de Diseño de Software (Spring Boot 3 / Java 17)

Se expande el modelo conceptual de clases hacia la arquitectura de software en capas de diseño, documentando Controllers (REST), Inbound Ports (Interfaces), Servicios de Aplicación, Entidades de Dominio y Adaptadores de Persistencia (Repositories):

![Figura 3.2. Clases Hexagonal Java 17](imagenes/figura12_clases_diseno_hexagonal.png)

*Figura 3.2. Diagrama de Clases de Diseño de Software en Arquitectura Hexagonal (Java 17).*

### 3.4. Cronograma Gantt Visual de Avance (Hito APF2 - Semana 8)

Se documenta la evolución temporal del proyecto desde la Semana 1 hasta la culminación de la Semana 8 (APF2), especificando las tareas críticas y la asignación de responsabilidades a los 3 estudiantes integrantes del equipo:

![Figura 3.3. Cronograma Gantt APF2](imagenes/figura13_gantt_avance_apf2.png)

*Figura 3.3. Cronograma Gantt de Ingeniería y Avance hacia la Semana 8 (APF2).*

---

## 4. Módulo de Reportes Clave y Compilación Normativa SFT BCRP

### 4.1. Fórmulas de Cálculo de Indisponibilidad y Cumplimiento de SLA

En concordancia con la Circular BCRP N° 0011-2023, el sistema computa de forma continua los acuerdos de nivel de servicio (SLA):

• Disponibilidad Mensual de la Red Interoperable (%):
   Disponibilidad (%) = [ (43,200 - Minutos_Totales_de_Caída) / 43,200 ] * 100
   (Donde 43,200 min corresponde a la base de 30 días continuos 24/7; umbral mínimo regulatorio >= 99.90%).

• Tiempo Medio de Detección (MTTD): Promedio en segundos entre el fallo HTTP de la entidad y la apertura del ticket regulatorio (<30s).
• Tiempo Medio de Reparación (MTTR): Promedio en minutos desde la apertura del ticket hasta la certificación del estado CERRADO.

### 4.2. Especificación Técnica del Archivo Normativo SFT BCRP

El formato de salida SFT exige un archivo plano delimitado por caracteres pipe (|), conformado por 3 segmentos:
1. Registro Tipo 01 (Cabecera): Código de sistema, periodo evaluado (YYYYMM), RUC de la entidad supervisora y nombre de mesa.
2. Registro Tipo 02 (Detalle de Fallas): Código de ticket correlativo, participante afectado, tipo de evento, timestamp ISO-8601 de inicio y solución, duración en minutos y severidad.
3. Registro Tipo 03 (Pie Criptográfico): Total de registros, minutos acumulados de indisponibilidad y firma digital SHA-256 de 64 caracteres hexadecimales para asegurar integridad legal y no repudio.

---

## 5. Sustentación Oral APF2 (Guion de 10 Minutos por Integrante)

La presentación ejecutiva se distribuye equitativamente entre los 3 integrantes del equipo, cubriendo los 3 bloques temáticos estratégicos exigidos en la rúbrica para la defensa de 10 minutos:

| Bloque / Tiempo | Integrante Responsable | Rol Oficial | Temas y Alocución Clave |
| :--- | :--- | :--- | :--- |
| Bloque 1<br>(00:00 - 03:00) | Fernando Alber Alfredo Romero Requejo | Ingeniero de Requisitos | Contexto del BCRP, Circular 0011-2023, problemática de asimetría de información y propuesta de valor del Business Model Canvas (BMC). |
| Bloque 2<br>(03:00 - 07:00) | Frank Emiliano Vargas Huamán | Líder Técnico & Arquitecto | Presentación de las 3 alternativas TIC con >=50% Java, selección de la Alternativa 2 (Clean API), diseño de las 15 pantallas, arquitectura DER Físico/Lógico PostgreSQL 15, inmutabilidad append-only, seguridad RBAC/TLS y diagrama de clases de diseño en Spring Boot 3. |
| Bloque 3<br>(07:00 - 10:00) | Joel Leonardo Olaya Vivas | Ingeniero de QA & Software | Live Demo de la Consola NOC en tiempo real, inyección de falla 503, creación atómica de ticket, suite JUnit 5 (8/8) y compilación del reporte de indisponibilidad y archivo normativo SFT con hash SHA-256. |

---

## 6. Conclusiones y Recomendaciones para la Etapa Final

• Conclusión 1: Se ha completado al 100% el diseño detallado de la solución para el Hito APF2 (Semana 8), consolidando el modelo de persistencia inmutable, la arquitectura desacoplada y la especificación de interfaces con estándar bancario.
• Conclusión 2: Las tres alternativas formuladas cumplen rigurosamente con poseer más del 50% de componentes en Java y cuentan con 5 pantallas funcionales cada una, respaldando la elección de la Alternativa 2 por su ergonomía NOC y latencia menor a 30 segundos.
• Conclusión 3: La incorporación del DER Físico/Lógico ortogonal y el capítulo formal de seguridad en base de datos garantizan la integridad referencial y el no repudio probatorio exigido por el BCRP.
• Recomendación: Para el hito final (Semana 14), se recomienda activar el clúster de bases de datos con replicación física primaria-secundaria e integrar el despacho de alertas vía webhooks seguros.

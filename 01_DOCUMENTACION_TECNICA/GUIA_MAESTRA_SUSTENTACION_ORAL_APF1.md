# 🎓 GUÍA MAESTRA PARA LA SUSTENTACIÓN ORAL - AVANCE DE PROYECTO FINAL 1 (APF1)
## CURSO INTEGRADOR I: SISTEMAS SOFTWARE (SECCIÓN 57524)
**Universidad Tecnológica del Perú (UTP) - Facultad de Ingeniería**  
- **Docente Evaluador:** Ing. Yony Zamata Condori  
- **Líder Técnico / Ponente:** Frank Emiliano Vargas Huamán  
- **Proyecto:** Sistema Integral de Gestión de Incidentes Regulatorios para Pagos Digitales (SIGIR - BCRP)  
- **Meta Académica:** Obtener la **Calificación Máxima (20 / 20 puntos)** en la Rúbrica Oficial.

---

## 📋 1. MAPA DE AUDITORÍA Y COBERTURA DE LA RÚBRICA EVALUATIVA

| Criterio de la Rúbrica | Puntaje | Evidencia Técnica en el Proyecto | Ubicación en la Ponencia |
| :--- | :---: | :--- | :--- |
| **1. Análisis del Contexto** | **3.0 pts** | Análisis del BCRP, problema descrito, alcance articulado en los 5 aspectos (Visión, Misión, Entorno, Estrategias, Planes) y **Canvas de Modelo de Negocio (BMC)**. | Diapositivas 2 y 3 (Min 01:00 a 03:30) |
| **2. Planteamiento de Alternativas de Solución** | **2.0 pts** | **3 alternativas planteadas**, cada una con **no menos de 5 pantallas**, cubriendo el alcance con TIC y **todas con >= 50% Java**. Matriz multicriterio de selección. | Diapositivas 4 y 5 (Min 03:30 a 05:30) |
| **3. Uso de Herramientas de Gestión** | **3.0 pts** | Uso simultáneo de **Project Charter básico**, **EDT / WBS a 3 niveles** y **Cronograma Gantt** detallado por Sprints Scrum cubriendo los objetivos. | Diapositiva 6 (Min 05:30 a 07:00) |
| **4. Análisis de la Solución** | **6.0 pts** | **Casos de Uso del Sistema (UML 2.5)**, **12 Requisitos Funcionales (RF-01 a 12)** y **8 Requisitos No Funcionales (RNF-01 a 08)** bajo estándar SRS / IEEE 830. | Diapositiva 7 (Min 07:00 a 08:30) |
| **5. Diseño de la Solución** | **4.0 pts** | Diagramas de proceso (**BPMN 2.0 AS-IS y TO-BE**), **Arquitectura Hexagonal (Ports & Adapters)**, modelo relacional DDL PostgreSQL 15 inmutable y diagramas de secuencia. | Diapositivas 8 y 9 (Min 08:30 a 10:30) |
| **6. Sustentación Oral** | **2.0 pts** | Secuencia lógica, continuidad fluida sin pausas, coherencia técnica total y **dominio riguroso de los conceptos del curso** (Clean Architecture, SOLID, TDD, GoF). | Durante toda la exposición (Min 00:00 a 14:00) |
| **PUNTAJE TOTAL ESPERADO** | **20.0 / 20.0 pts** | **Cumplimiento al 100% de los estándares de excelencia técnica.** | **ESTÁNDAR ESPERADO EN TODOS LOS CRITERIOS** |

---

## ⏱️ 2. CRONOMETRÍA Y ESTRUCTURA DE LA PRESENTACIÓN (12 A 14 MINUTOS)

- **Bloque 1 (Min 00:00 - 01:00):** Saludo formal, presentación personal y planteamiento del título del proyecto.
- **Bloque 2 (Min 01:00 - 03:30):** Contexto institucional del BCRP, los 5 aspectos estratégicos y el Business Model Canvas (BMC).
- **Bloque 3 (Min 03:30 - 05:30):** Planteamiento del problema, evaluación de las 3 alternativas (>50% Java y 5 pantallas) y justificación de selección.
- **Bloque 4 (Min 05:30 - 07:00):** Herramientas de gestión: Project Charter, WBS y Cronograma Gantt en Scrum.
- **Bloque 5 (Min 07:00 - 08:30):** Análisis formal bajo estándar SRS IEEE 830: Casos de uso UML, RF-01 a 12 y RNF ISO/IEC 25010.
- **Bloque 6 (Min 08:30 - 10:30):** Diseño arquitectónico: BPMN 2.0 TO-BE, Arquitectura Hexagonal en Java 17 y base de datos PostgreSQL inmutable.
- **Bloque 7 (Min 10:30 - 13:00):** Demostración práctica en vivo (Live Demo de la Consola NOC y pruebas unitarias de backend).
- **Bloque 8 (Min 13:00 - 14:00):** Conclusiones, lecciones aprendidas y articulación con los conceptos del curso.

---

## 🗣️ 3. GUION VERBAL LITERAL PALABRA POR PALABRA (SPEECH PARA FRANK VARGAS)

> **Instrucciones para Frank:**  
> - Habla con voz firme, clara, pausada y con postura erguida.  
> - Utiliza las manos para enfatizar los cambios de pantalla y diagramas.  
> - Mira a la cámara (o a los ojos del docente) para transmitir seguridad y liderazgo de ingeniería.

---

### [MINUTO 00:00 - 01:00] INTRODUCCIÓN Y APERTURA FORMAL
*(Proyectar Diapositiva 1: Portada Oficial UTP - Carátula Institucional BCRP)*

> *"Estimado profesor Ing. Yony Zamata Condori y compañeros presentes, tengan ustedes muy buenas tardes.*  
> *Mi nombre es **Frank Emiliano Vargas Huamán**, estudiante de séptimo ciclo de la carrera de Ingeniería de Sistemas e Informática de la Universidad Tecnológica del Perú.*  
> *Es para mí un honor sustentar de forma exhaustiva el **Avance de Proyecto Final 1 (APF1)** en el marco de la asignatura **Curso Integrador I: Sistemas Software**.*  
> *El proyecto que presento se titula: **'Sistema Integral de Gestión de Incidentes Regulatorios para Pagos Digitales', denominado técnicamente SIGIR - BCRP**, concebido específicamente para resolver el caso de regulación de pagos digitales del Banco Central de Reserva del Perú.*  
> *A lo largo de esta sustentación, demostraré cómo hemos articulado los cinco aspectos estratégicos institucionales, el Business Model Canvas, la comparación de tres alternativas tecnológicas con más del 50% de desarrollo en Java, las herramientas formales de gestión PMBOK y Scrum, el modelado SRS bajo IEEE 830 y una arquitectura hexagonal limpia y verificable en código ejecutable."*

---

### [MINUTO 01:00 - 03:30] ANÁLISIS DEL CONTEXTO INSTITUCIONAL, 5 ASPECTOS Y CANVAS BMC
*(Proyectar Diapositiva 2: Contexto BCRP, Visión, Misión, Entorno, Estrategias y Planes)*

> *"Iniciamos con el **Análisis del Contexto de la Empresa**.*  
> *El Banco Central de Reserva del Perú es la entidad constitucionalmente autónoma encargada de velar por la estabilidad monetaria y regular el Sistema Nacional de Pagos, según la Ley N° 26123 y la Ley N° 29440.*  
> *Para delimitar de manera rigurosa el alcance de nuestra solución de software, articulamos cinco aspectos estratégicos esenciales:*  
>  
> 1. *Primero, la **Visión Institucional**: El BCRP aspira a ser reconocido como un organismo moderno, modelo de institucionalidad y con elevada credibilidad internacional. Un banco central moderno no puede supervisar el dinero electrónico con hojas de cálculo o correos manuales; requería una plataforma de observabilidad telemétrica en tiempo real.*  
> 2. *Segundo, la **Misión Institucional**: Mantener personal calificado y compartir información transparente para preservar la estabilidad monetaria. El sistema entrega a los analistas datos puros, sin sesgos y en tiempo real.*  
> 3. *Tercero, el **Entorno Operacional y Regulatorio**: El Perú procesa hoy más de 15 millones de transacciones inmediatas diarias entre billeteras interoperables como Yape y Plim, microfinancieras como Caja Cusco con Tunki, Caja Rural de los Andes y la Cámara de Compensación Electrónica (CCE). Todas bajo el mandato vinculante de la Circular BCRP N° 0011-2023.*  
> 4. *Cuarto, las **Estrategias de la Empresa**: Alineadas a la Estrategia Nacional de Inclusión Financiera y la Estrategia de Resiliencia del Sistema Nacional de Pagos, buscando cero asimetría de información y resiliencia ante contingencias.*  
> 5. *Quinto, los **Planes de la Empresa**: Tributamos directamente al Plan Estratégico Institucional PEI 2022-2026, específicamente a los Objetivos Estratégicos OEI 04 sobre sistemas de pago eficientes, OEI 10 sobre innovación tecnológica, OEI 12 sobre eficiencia en supervisión y OEI 14 en mitigación de riesgos tecnológicos.*  
>  
> *(Proyectar Diapositiva 3: Lienzo del Business Model Canvas - BMC)*  
>  
> *Para graficar de manera sintética este análisis de contexto y el valor del proyecto, construimos el **Business Model Canvas** adaptado a un servicio tecnológico-regulatorio del Estado:*  
> - *En nuestros **Socios Clave**, integramos a la CCE como switch de pagos, a los bancos BCP e Interbank, a las Cajas Municipales y Rurales, y a la división de ciberseguridad del BCRP.*  
> - *Nuestras **Actividades Clave** son el polling asíncrono cada 30 segundos, la clasificación automática de fallas 5xx, el registro asistido de fraude y la emisión del archivo oficial SFT con firma SHA-256.*  
> - *Nuestra **Propuesta de Valor** es contundente: reducir el tiempo de detección de caídas de 180 minutos a menos de 30 segundos, erradicando la asimetría informativa y garantizando inmutabilidad forense.*  
> - *Nuestros **Canales** son la Consola Web NOC en tiempo real, las APIs REST documentadas en OpenAPI y el SFTP regulatorio.*  
> - *Y en el **Retorno de Valor Público**, preservamos la confianza ciudadana en el dinero digital y evitamos pérdidas económicas multimillonarias por caídas transaccionales en horas punta."*

---

### [MINUTO 03:30 - 05:30] PROBLEMA, ACTORES Y LAS 3 ALTERNATIVAS DE SOLUCIÓN (>=50% JAVA Y 5 PANTALLAS)
*(Proyectar Diapositiva 4: Realidad Problemática y Actores del Ecosistema Financiero)*

> *"¿Cuál es la realidad problemática que atacamos?*  
> *Hoy en día, cuando ocurre una caída en un enlace interbancario o una billetera, el regulador se entera típicamente entre 45 y 180 minutos después, tras quejas masivas en redes sociales o cuando la entidad remite un correo no estructurado. Hay asimetría de información, reportes heterogéneos y dispersión de credenciales.*  
>  
> *(Proyectar Diapositiva 5: Tabla Comparativa de 3 Alternativas y 5 Pantallas)*  
>  
> *Para resolver esto, evaluamos de forma metódica **tres alternativas de solución aplicando las TIC**, cumpliendo estrictamente con la directiva de que cada alternativa supere el **50% de implementación en Java**:*  
>  
> - ***Alternativa 1: Monolito Empresarial MVC en Java Puro (Spring Boot 3 + Thymeleaf + PostgreSQL).***  
>   *Tiene un **85% de componentes en Java**. Implementa renderizado en servidor (SSR). Diseñamos 5 pantallas completas: (1) Login AD con Spring Security, (2) Tablero central de entidades en tablas Thymeleaf, (3) Formulario asistido con binding y validación de entidades, (4) Bitácora de transiciones y estados, y (5) Módulo de descarga del reporte SFT. Su limitación fue la baja reactividad para una sala NOC, al requerir recargas de página.*  
>  
> - ***Alternativa 2 (Nuestra Selección Final): Arquitectura Desacoplada Hexagonal (Spring Boot 3 REST API + Consola Web SPA NOC + PostgreSQL 15).***  
>   *Posee un **65% de desarrollo en Java** (backend 100% Java 17 con arquitectura hexagonal, multihilo asíncrono y Spring Security AD; y 35% en capa cliente SPA reactiva). Diseñamos e implementamos sus 5 pantallas completas: (1) Encabezado de sesión corporativa con Active Directory y píldora de conectividad backend, (2) Barra de telemetría en vivo con semáforos interactivos de Yape, Plim, Tunki, Caja de los Andes y CCE con latencia en ms, (3) Matriz de seguimiento y ciclo de vida con filtros en tiempo real, (4) Modal asistido de registro forense de incidentes de fraude o brechas, y (5) Modal visor y compilador normativo SFT con hash SHA-256 de 64 caracteres.*  
>  
> - ***Alternativa 3: Arquitectura Distribuida Basada en Eventos (Spring Cloud + Apache Kafka + Microservicios en Java).***  
>   *Tiene un **75% de peso en Java**. Diseñada para ingesta de millones de eventos por streaming. Diseñamos 5 pantallas: (1) Gateway SSO OAuth2, (2) Monitor de tópicos Kafka y tasas de consumo, (3) Consola de colas DLQ, (4) Centro forense de trazas distribuidas con TraceID, y (5) Panel de despacho batch a SFTP. La descartamos por sobrecosto de infraestructura y excesiva complejidad operacional para el número de entidades supervisadas.*  
>  
> *En la **Matriz de Decisión Multicriterio de Ingeniería**, la **Alternativa 2** obtuvo el puntaje más alto con **4.97 sobre 5.00 puntos**, garantizando la menor latencia de sondeo, código desacoplado y la mejor experiencia operativa para el BCRP."*

---

### [MINUTO 05:30 - 07:00] HERRAMIENTAS DE GESTIÓN: PROJECT CHARTER, WBS Y GANTT
*(Proyectar Diapositiva 6: Project Charter, WBS / EDT a 3 niveles y Cronograma Gantt)*

> *"En el **Uso de Herramientas de Gestión de Proyectos**, articulamos el estándar del PMI y el marco ágil Scrum:*  
>  
> 1. *Elaboramos el **Project Charter Básico (Acta de Constitución del Proyecto)**, formalizando como Patrocinador al Ing. Yony Zamata y como Líder Técnico a mi persona. En él definimos los objetivos SMART de alcance, tiempo de 14 semanas divididas en 6 Sprints, disponibilidad telemétrica de 30 segundos, supuestos, restricciones y una matriz de gestión de riesgos.*  
> 2. *Estructuramos la **EDT / WBS (Estructura de Desglose del Trabajo)** a tres niveles jerárquicos: iniciando en Fase 1.1 con el levantamiento normativo; Fase 1.2 con modelado de negocio y requisitos; Fase 1.3 con diseño de arquitectura hexagonal y datos DDL; y Fase 1.4 con la construcción de este entregable APF1.*  
> 3. *Y planificamos el **Cronograma Gantt**, estableciendo que el Hito APF1 culmina en la Semana 4 con los cimientos operativos, continuando en los Sprints 4 al 6 con el módulo analítico gerencial y reportes PDF/Excel en la Etapa 02.*  
> *Todo el esfuerzo se estimó en **70 Story Points** mediante Planning Poker, asociando una Matriz RACI para clarificar responsabilidades."*

---

### [MINUTO 07:00 - 08:30] ANÁLISIS DE LA SOLUCIÓN BAJO ESTÁNDAR SRS (IEEE 830 / ISO 25010)
*(Proyectar Diapositiva 7: Diagrama de Casos de Uso UML y Matriz RF / RNF)*

> *"Pasando al **Análisis de la Solución**, aplicamos con rigurosidad el estándar **SRS (Software Requirements Specification) bajo la norma IEEE Std 830-1998**.*  
>  
> - *Diseñamos el **Diagrama de Casos de Uso del Sistema (UML 2.5)** con cuatro actores clave: el Operador NOC, el Administrador de TI, el Supervisor BCRP y el actor del sistema Worker de Telemetría.*  
> - *Especificamos **12 Requisitos Funcionales exhaustivos (RF-01 a RF-12)**. Destaco el **RF-01** para autenticación centralizada única contra Directorio Activo institucional (cumpliendo el requerimiento del caso de estudio de un solo login con AD); el **RF-03 y RF-04** para el sondeo asíncrono y la auto-categorización de incidentes de disponibilidad ante códigos 5xx o latencia mayor a 3 segundos; el **RF-05** para la clasificación manual asistida de fraudes; el **RF-06** para la máquina de estados; y el **RF-08** para la compilación del archivo SFT con digest SHA-256.*  
> - *Y establecimos **8 Requisitos No Funcionales (RNF-01 a RNF-08)** alineados a la norma **ISO/IEC 25010**, garantizando seguridad con LDAPS y JWT, e inmutabilidad estricta en el almacenamiento forense."*

---

### [MINUTO 08:30 - 10:30] DISEÑO DE LA SOLUCIÓN: PROCESOS BPMN 2.0, ARQUITECTURA HEXAGONAL Y BASE DE DATOS
*(Proyectar Diapositiva 8: Modelado BPMN 2.0 TO-BE y Arquitectura Hexagonal)*

> *"En el **Diseño de la Solución**, iniciamos optimizando los procesos:*  
> - *En el **BPMN 2.0 AS-IS**, documentamos cómo el proceso actual manual tarda horas y genera asimetría.*  
> - *En el **BPMN 2.0 TO-BE**, diagramamos el flujo automatizado: el Worker sondea en paralelo las APIs; si detecta falla, evalúa si existe ticket abierto para evitar duplicidades (Regla RN-01), genera el ticket atómico, actualiza el semáforo y despacha la alerta en milisegundos.*  
>  
> *(Proyectar Diapositiva 9: Arquitectura Hexagonal y Esquema Físico PostgreSQL)*  
>  
> - *En la **Arquitectura Hexagonal (Ports & Adapters)**, aplicamos Clean Architecture y principios SOLID: el Dominio Java (entidades `Incidente`, `EntidadFinanciera`) no depende de ningún framework externo. La lógica se expone mediante el puerto `IIncidenteService`, desacoplado de los adaptadores de entrada (Spring MVC y Worker) y adaptadores de salida (Spring Data JPA y Active Directory).*  
> - *En la **Base de Datos Relacional (PostgreSQL 15)**, modelamos tres tablas maestras: `entidades_financieras`, `incidentes` y la bitácora `historial_estados`. Implementamos un **índice parcial condicional único** para evitar condiciones de carrera en tickets abiertos, y blindamos la auditoría mediante el trigger `trg_prohibir_mutacion_historial`, que arroja una excepción a nivel de motor SQL ante cualquier intento de UPDATE o DELETE. ¡La auditoría es matemáticamente inmutable!"*

---

### [MINUTO 10:30 - 13:00] DEMOSTRACIÓN PRÁCTICA EN VIVO (LIVE DEMO)
*(Cambiar de pantalla y mostrar la Consola NOC interactiva en el navegador)*

> *"A continuación, me permitiré demostrar el funcionamiento del software en tiempo real:*  
>  
> *(1. Mostrar Header Corporativo y Active Directory):*  
> *'Como pueden apreciar en pantalla, la consola web refleja la sesión corporativa autenticada bajo mi usuario `AD\frank.vargas@bcrp.local` con rol institucional `NOC_LEAD`, validando el requisito de un único login institucional.*  
>  
> *(2. Mostrar Barra de Semáforos y Latencia):*  
> *'En la barra superior observamos la telemetría en vivo: el BCP (Yape) con 142 ms, Interbank (Plim) con 189 ms, Caja Cusco con 210 ms, y el Switch de la Cámara de Compensación Electrónica con 95 ms. El temporizador indica la cuenta regresiva del sondeo cada 30 segundos.*  
>  
> *(3. Clic en botón 'Simular Caída de Servicio'):*  
> *'Voy a presionar el botón 'Simular Caída de Servicio'. Observen lo que sucede: la API de Interbank (Plim) responde con error HTTP 503 Service Unavailable y timeout. De forma instantánea, el semáforo pasa a color rojo ('FUERA DE SERVICIO'), la latencia se dispara a 3200 ms y en la matriz inferior se auto-genera el ticket `INC-20260905-00004` con categoría 'DISPONIBILIDAD DE SERVICIOS' y severidad 'ALTA'. ¡Cero intervención humana, detección en tiempo real!*  
>  
> *(4. Clic en 'Registro Manual Asistido'):*  
> *'Ahora bien, supongamos que el equipo de ciberseguridad detecta un ataque de phishing contra la pasarela QR. Abrimos el 'Registro Manual Asistido'. Seleccionamos la entidad BCP, categoría 'FRAUDE FINANCIERO', severidad 'CRÍTICA', ingresamos la descripción técnica forense y guardamos. El ticket queda registrado con auditoría inmutable vinculada a mi usuario de red.*  
>  
> *(5. Clic en 'Compilar Archivo SFT BCRP'):*  
> *'Finalmente, hacemos clic en 'Compilar Archivo SFT BCRP'. El sistema compila en memoria la estructura normativa exigida por la circular del Banco Central: cabecera `01`, cuerpo `02` delimitado por pipes con las fechas, códigos BCRP y tiempos de indisponibilidad, y en el pie `03` calcula el hash criptográfico SHA-256 de 64 caracteres. Al dar clic en 'Descargar Archivo .TXT', el archivo queda listo para su transmisión a la mesa regulatoria.*  
>  
> *(6. Mencionar Pruebas Unitarias):*  
> *'Este comportamiento se encuentra respaldado por una suite de 8 pruebas unitarias automatizadas con JUnit 5 y Mockito en el backend Spring Boot, validando la máquina de estados, el worker asíncrono y la integridad criptográfica con 100% de éxito'."*

---

### [MINUTO 13:00 - 14:00] CONCLUSIONES Y CIERRE DE IMPACTO
*(Regresar a la diapositiva final de Conclusiones y Preguntas)*

> *"En conclusión, el proyecto **SIGIR - BCRP** en este Avance de Proyecto Final 1 no es solo un diseño teórico; es una solución de ingeniería de software robusta, autocontenida y verificable:*  
> 1. *Cumplimos al 100% el alcance regulatorio del BCRP, erradicando la asimetría de información y blindando la estabilidad financiera del país.*  
> 2. *Aplicamos los conceptos rectores del curso: Arquitectura Hexagonal, principios SOLID, inmutabilidad ACID en base de datos, marco Scrum con WBS y Gantt, y pruebas automatizadas TDD.*  
> 3. *Sentamos las bases para la Etapa 02, donde incorporaremos el dashboard gerencial de KPIs (MTTR y MTTD) y los reportes avanzados en PDF y Excel.*  
>  
> *Quedo a su entera disposición, profesor Yony Zamata, para responder con el mayor gusto sus consultas técnicas. ¡Muchas gracias!"*

---

## 🛡️ 4. BANCO DE PREGUNTAS TÉCNICAS DIFÍCILES DEL JURADO (Y CÓMO RESPONDERLAS)

### Pregunta 1: ¿Por qué eligieron Arquitectura Hexagonal en lugar de una arquitectura tradicional en 3 capas?
- **Respuesta de Frank:**  
  *"Profesor, en una arquitectura tradicional en 3 capas, la lógica de negocio suele quedar fuertemente acoplada a las anotaciones de Hibernate/JPA y al framework web. En SIGIR-BCRP, la normativa del Banco Central de Reserva puede sufrir modificaciones en la circular de reporte o en el formato de transporte. Con la Arquitectura Hexagonal (Ports & Adapters), aislamos el núcleo del dominio dentro de Java 17 puro. Si el BCRP decide mañana reemplazar el transporte REST por mensajería gRPC o cambiar PostgreSQL por Oracle, nuestro dominio (`Incidente`, reglas de validación y cálculo de hash) permanece 100% intacto, respetando el principio Abierto/Cerrado (OCP) e Inversión de Dependencias (DIP) de SOLID."*

### Pregunta 2: ¿Cómo garantizan que el historial de incidentes no sea alterado por un administrador malicioso o por una consulta SQL accidental?
- **Respuesta de Frank:**  
  *"La inmutabilidad no la dejamos únicamente en manos del código Java; la blindamos en el motor de base de datos PostgreSQL 15. Diseñamos la función de trigger `trg_prohibir_mutacion_historial` asociada a la tabla `historial_estados`. Este trigger se dispara ante cualquier sentencia de `UPDATE` o `DELETE` y ejecuta un `RAISE EXCEPTION` abortando la transacción. Además, a nivel de usuarios de base de datos, el usuario de la aplicación solo posee permisos de `SELECT` e `INSERT` sobre dicha tabla, logrando un patrón de bitácora Append-Only formal con validez probatoria forense."*

### Pregunta 3: En el worker de telemetría, ¿qué ocurre si un banco tarda 10 segundos en responder? ¿No se bloquea el hilo principal del servidor?
- **Respuesta de Frank:**  
  *"Excelente pregunta, profesor. Para evitar bloqueos de I/O, el `TelemetriaWorker` utiliza sondeos asíncronos y paralelos mediante `CompletableFuture` y pools de hilos dedicados. Además, configuramos un `connectTimeout` y `readTimeout` estricto de 3000 milisegundos (3 segundos) en el cliente HTTP. Si una entidad no responde en ese lapso, la promesa se completa por timeout de forma no invasiva, liberando el hilo inmediatamente y tipificando el evento como una caída de disponibilidad sin afectar el sondeo de las demás entidades."*

### Pregunta 4: ¿Por qué en la rúbrica se pide que la alternativa sea al menos 50% Java y cómo lo demuestran?
- **Respuesta de Frank:**  
  *"En la Alternativa 2 seleccionada, el 65% de la carga de ingeniería reside en el backend empresarial Java 17: todas las entidades de dominio, los repositorios JPA, los servicios de cálculo SHA-256, los controladores REST con OpenAPI, la seguridad LDAP con Active Directory y el worker multihilo están escritos en Java sobre Spring Boot 3. El 35% restante corresponde a la interfaz de presentación SPA reactiva con JavaScript moderno para garantizar semáforos de respuesta inmediata en la consola NOC sin recargar la página. Cumplimos con holgura el requisito del curso."*

### Pregunta 5: ¿Cómo se calculó y validó el hash SHA-256 en el archivo plano SFT del BCRP?
- **Respuesta de Frank:**  
  *"En la clase `SftReportService.java`, compilamos primero las líneas de cabecera `01` y cuerpo `02` utilizando `StringBuilder` con codificación estándar UTF-8. Luego, pasamos ese flujo de bytes por el algoritmo criptográfico `MessageDigest.getInstance("SHA-256")`, formateando los 32 bytes resultantes en una cadena hexadecimal de 64 caracteres. Ese hash se inyecta en la línea de footer `03`. Si alguien altera siquiera un solo espacio o número en el archivo durante el envío SFTP, el hash receptor no coincidirá, garantizando la integridad no repudiable requerida por la circular del BCRP."*

---

## 🎯 5. CHECKLIST FINAL DE VERIFICACIÓN PRE-SUSTENTACIÓN (CERO ERRORES)

- [x] **Consola Web NOC funcional:** Abrir `04_FRONTEND_UI/index.html` en Chrome/Edge antes de empezar la sesión.
- [x] **Semáforos listos:** Verificar que los semáforos muestren BCP, Interbank, Caja Cusco, Caja de los Andes y CCE.
- [x] **Diapositivas ordenadas:** Seguir exactamente los 6 puntos de la rúbrica para que el profesor marque el estándar esperado sin dudar.
- [x] **Vocabulario técnico activo:** Usar términos como: *Arquitectura Hexagonal*, *Inversión de Dependencias*, *Trigger Append-Only*, *Sondeo Asíncrono CompletableFuture*, *Digest Criptográfico SHA-256*, *WBS jerárquico* y *Directorio Activo*.

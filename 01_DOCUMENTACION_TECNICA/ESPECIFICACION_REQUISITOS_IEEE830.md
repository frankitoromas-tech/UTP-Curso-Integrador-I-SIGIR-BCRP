# ESPECIFICACIÓN DE REQUISITOS DE SOFTWARE (NORMA IEEE STD 830-1998)
**Proyecto:** Sistema Integral de Gestión de Incidentes Regulatorios (SIGIR - BCRP)  
**Curso:** Curso Integrador I: Sistemas Software (Sección 57524)  
**Docente:** Ing. Yony Zamata Condori  
**Estudiante:** Frank Emiliano Vargas Huamán  

---

## 1. INTRODUCCIÓN

### 1.1. Propósito
El presente documento tiene como objetivo definir y formalizar los requerimientos funcionales, no funcionales y reglas de negocio del software **SIGIR - BCRP**, de acuerdo con las directivas internacionales del estándar **IEEE Std 830-1998 (Recommended Practice for Software Requirements Specifications)**.

### 1.2. Alcance del Producto
El sistema constituye una solución empresarial integral para la supervisión y control de incidentes tecnológicos, operativos y de seguridad en el Sistema de Pagos Digitales del Perú, integrando a los participantes del switch de transferencias inmediatas (Yape, Plim, Tunki, Caja de los Andes y CCE) con el ente regulador (BCRP).

---

## 2. REGLAS DE NEGOCIO DEL SISTEMA (RN)

- **RN-01 (Unicidad de Incidentes Abiertos):** No podrá existir más de un incidente abierto simultáneo (`REGISTRADO`, `EN_EVALUACION` o `EN_MITIGACION`) para el mismo servicio y entidad financiera.
- **RN-02 (Detección y Auto-Categorización de Disponibilidad):** Todo fallo de red capturado por el servicio de telemetría con código HTTP en rango 500-599 o interrupción por *socket timeout* (> 3000 ms) se catalogará de forma obligatoria e inmutable con la categoría `DISP_SERV` (Disponibilidad de Servicios).
- **RN-03 (Clasificación Restringida):** Las categorías de `FRAUDE_FIN` (Fraude Financiero) y `BRECHA_DATOS` (Manipulación de Información) sólo podrán ser registradas y tipificadas por un operador o analista humano autenticado.
- **RN-04 (Inmutabilidad de Auditoría):** Una vez insertado un registro de transición de estado en la tabla `historial_estados`, queda estrictamente prohibida su edición o eliminación física.
- **RN-05 (Cumplimiento de Integridad SFT BCRP):** Todo archivo generado para el BCRP debe contener un registro de pie (*Footer*) con la función hash criptográfica SHA-256 calculada sobre la totalidad de los bytes del cuerpo del archivo.
- **RN-06 (Autenticación Obligatoria AD):** Ningún usuario operativo podrá autenticarse con cuentas locales; todas las solicitudes deben ser resueltas contra el Directorio Activo institucional (Active Directory / LDAP).

---

## 3. FICHAS TÉCNICAS DE REQUISITOS FUNCIONALES (RF)

### RF-01: Autenticación Centralizada mediante Directorio Activo (AD / LDAP)
- **Módulo:** Seguridad y Control de Acceso.
- **Actor Principal:** Operador NOC / Supervisor BCRP / Administrador TI.
- **Prioridad:** Alta (Obligatorio para Etapa 01).
- **Descripción:** El sistema debe autenticar al personal contra el servidor de Directorio Activo corporativo (LDAP sobre TLS / LDAPS), validando pertenencia a grupos de seguridad y emitiendo un token criptográfico JWT para la sesión web.
- **Precondición:** El servidor Active Directory debe estar accesible en el puerto seguro 636.
- **Postcondición:** El usuario accede a la consola con los privilegios exactos de su rol (`ROLE_OPERADOR`, `ROLE_SUPERVISOR`, `ROLE_ADMIN`).
- **Criterio de Aceptación:** Si las credenciales o el grupo de seguridad son inválidos, el sistema deniega el acceso registrando el intento en la bitácora de seguridad.

### RF-02: Mantenimiento de Entidades Financieras Interconectadas
- **Módulo:** Mantenimiento Base.
- **Actor Principal:** Administrador TI.
- **Prioridad:** Alta.
- **Descripción:** Permite crear, modificar, consultar y cambiar el estado (activo/inactivo) de las instituciones participantes del sistema de pagos (código BCRP, razón social, nombre comercial, URL de monitoreo y frecuencia de sondeo).
- **Precondición:** Usuario autenticado con rol `ROLE_ADMIN`.
- **Postcondición:** La entidad queda registrada y habilitada para el worker de telemetría.
- **Criterio de Aceptación:** El código de entidad BCRP debe ser único en la base de datos (clave candidata).

### RF-03: Sondeo Continuo de Telemetría (Health Check Polling)
- **Módulo:** Telemetría e Interconexión.
- **Actor Principal:** Worker de Telemetría (Proceso Daemon del Sistema).
- **Prioridad:** Alta.
- **Descripción:** El sistema ejecutará un sondeo asíncrono periódico a los endpoints de salud expuestos por las entidades financieras registradas para verificar latencia y disponibilidad.
- **Precondición:** Entidades configuradas con estado activo y URL de health check válida.
- **Postcondición:** Se actualiza el semáforo de conectividad y la métrica de latencia en milisegundos.
- **Criterio de Aceptación:** El worker no debe bloquear el hilo principal y debe tolerar caídas de conexión sin interrumpir el servicio.

### RF-04: Auto-Categorización de Caídas de Servicio
- **Módulo:** Motor de Incidentes.
- **Actor Principal:** Worker de Telemetría.
- **Prioridad:** Alta (Etapa 01).
- **Descripción:** Al recibir un código de error HTTP 5xx o agotarse el tiempo de espera en el sondeo a una entidad, el sistema auto-generará un ticket en estado `REGISTRADO`, con severidad `ALTA` o `CRITICA` y categoría `DISP_SERV`.
- **Precondición:** Falla de disponibilidad confirmada y ausencia de incidente abierto previo para dicha entidad.
- **Postcondición:** Ticket creado y visible en la consola de incidentes en tiempo real.
- **Criterio de Aceptación:** El ticket incluye la traza exacta de error y marca de tiempo con precisión de milisegundos.

### RF-05: Registro y Clasificación Manual de Incidentes
- **Módulo:** Motor de Incidentes.
- **Actor Principal:** Operador NOC / Analista de Riesgos.
- **Prioridad:** Alta (Etapa 01).
- **Descripción:** Interfaz asistida para registrar y clasificar contingencias que no admiten detección automática, tales como fraude financiero masivo o brechas de datos.
- **Precondición:** Operador autenticado con rol `ROLE_OPERADOR`.
- **Postcondición:** Incidente registrado y asociado al responsable que lo ingresó.
- **Criterio de Aceptación:** Validación obligatoria de campos: entidad afectada, categoría, severidad, fecha/hora real de inicio y descripción técnica.

### RF-06: Gestión de la Máquina de Estados del Incidente
- **Módulo:** Ciclo de Vida y Auditoría.
- **Actor Principal:** Operador NOC / Supervisor BCRP.
- **Prioridad:** Alta (Etapa 01).
- **Descripción:** Permite avanzar los estados del incidente siguiendo el flujo estricto: `REGISTRADO` $\rightarrow$ `EN_EVALUACION` $\rightarrow$ `EN_MITIGACION` $\rightarrow$ `RESUELTO` $\rightarrow$ `CERRADO`.
- **Precondición:** Existencia del incidente y justificación técnica del cambio.
- **Postcondición:** Estado actualizado e inserción inmutable en la tabla `historial_estados`.
- **Criterio de Aceptación:** No se permiten saltos hacia atrás ni cierre sin haber pasado por el estado `RESUELTO`.

### RF-07: Consola Operativa NOC y Semáforos en Tiempo Real
- **Módulo:** Consulta y Visualización.
- **Actor Principal:** Operador NOC / Supervisor BCRP.
- **Prioridad:** Alta.
- **Descripción:** Pantalla de control que visualiza el estado en vivo de cada canal (Yape, Plim, Tunki, CCE), mostrando indicadores visuales verde/amarillo/rojo y el listado interactivo de incidentes activos.
- **Precondición:** Acceso autorizado a la plataforma.
- **Postcondición:** Interfaz reactiva actualizada mediante refresco automático o WebSocket.
- **Criterio de Aceptación:** El operador puede filtrar incidentes por severidad, entidad y rango de fechas con latencia inferior a 300 ms.

### RF-08: Compilación del Archivo Normativo SFT BCRP (.TXT)
- **Módulo:** Regulatorio BCRP.
- **Actor Principal:** Supervisor BCRP / Sistema Automatizado.
- **Prioridad:** Alta (Etapa 02).
- **Descripción:** Generación del archivo plano delimitado con pipes (`|`) conteniendo cabecera regulatoria, detalle de incidentes cerrados y pie con firma de integridad criptográfica SHA-256.
- **Precondición:** Incidentes en estado `CERRADO` dentro del período normativo a declarar.
- **Postcondición:** Archivo TXT generado, guardado en base de datos y disponible para descarga inmediata.
- **Criterio de Aceptación:** Validación estricta con el formato oficial publicado en la circular BCRP de incidentes de pagos digitales.

### RF-09: Dashboard Ejecutivo y Analítica de KPIs
- **Módulo:** Gestión Estratégica (Gerencial).
- **Actor Principal:** Gerencia de Operaciones / Supervisor BCRP.
- **Prioridad:** Media (Etapa 02).
- **Descripción:** Visualización de métricas estratégicas: Tiempo Medio de Recuperación (MTTR), Tiempo Medio de Detección (MTTD) y porcentaje global de Uptime por entidad participante.
- **Precondición:** Existencia de datos históricos de incidentes resueltos.
- **Postcondición:** Renderizado de gráficos interactivos y comparativas de SLA.
- **Criterio de Aceptación:** Cálculo automático de fórmulas de SLA en tiempo real.

### RF-10: Exportación de Reportes en PDF y Excel
- **Módulo:** Reportes Operativos y Administrativos.
- **Actor Principal:** Operador / Auditor.
- **Prioridad:** Media (Etapa 02).
- **Descripción:** Motor de exportación a documentos PDF formales (con membrete institucional) y hojas de cálculo Excel (.XLSX) con el histórico completo de eventos.
- **Precondición:** Selección de filtros de fecha y entidad en la consola.
- **Postcondición:** Descarga del archivo binario formateado.
- **Criterio de Aceptación:** El documento Excel incluye todas las columnas de la base de datos y fórmulas de conteo de horas de interrupción.

### RF-11: Motor de Notificaciones Automáticas por Correo SMTP
- **Módulo:** Notificaciones.
- **Actor Principal:** Motor de Despacho Asíncrono.
- **Prioridad:** Media (Etapa 02).
- **Descripción:** Envío automático de alertas a los correos electrónicos de los delegados de las entidades y auditores BCRP al dispararse un incidente crítico.
- **Precondición:** Ocurrencia de evento de severidad `ALTA` o `CRITICA`.
- **Postcondición:** Notificación entregada y registrada en el log del sistema.
- **Criterio de Aceptación:** El correo incluye ticket, entidad, hora de inicio y enlace directo al sistema.

---

## 4. REQUISITOS NO FUNCIONALES (RNF)

| Código | Categoría | Requisito Técnico | Métrica / Criterio |
| :--- | :--- | :--- | :--- |
| **RNF-01** | Seguridad | Autenticación robusta y control de acceso RBAC. | Directorio Activo (LDAPS puerto 636) y tokens JWT HMAC-SHA256 con expiración a las 8 horas. |
| **RNF-02** | Integridad | Políticas de almacenamiento Append-Only en auditoría. | Tabla `historial_estados` sin permisos de `UPDATE` o `DELETE` a nivel de motor SQL. |
| **RNF-03** | Disponibilidad | Alta disponibilidad en el módulo de recepción de eventos. | 99.9% de Uptime anual; colas de procesamiento tolerantes a desconexiones de red. |
| **RNF-04** | Rendimiento | Latencia reducida en APIs de consulta y telemetría. | Respuestas HTTP REST en menos de 500 ms con concurrencia de hasta 150 operadores. |
| **RNF-05** | Compatibilidad | Portabilidad multi-plataforma y soporte de navegadores. | Despliegue en contenedores Docker y compatibilidad con Chrome 110+, Edge 110+ y Firefox 110+. |
| **RNF-06** | Usabilidad | Diseño accesible, moderno y ergonómico para entornos NOC. | Modo oscuro corporativo con semáforos de contraste alto y cumplimiento de accesibilidad WCAG 2.1 AA. |
| **RNF-07** | Escalabilidad | Capacidad de procesamiento elástica. | Arquitectura backend desacoplada (stateless), permitiendo adición de réplicas en clúster. |
| **RNF-08** | Mantenibilidad | Estándares de código limpio y arquitectura desacoplada. | Cumplimiento estricto de Clean Architecture, principios SOLID y cobertura de pruebas unitarias superior al 80%. |

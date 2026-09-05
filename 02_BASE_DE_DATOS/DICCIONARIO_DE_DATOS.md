# DICCIONARIO DE DATOS RELACIONAL
**Proyecto:** Sistema Integral de Gestión de Incidentes Regulatorios (SIGIR - BCRP)  
**Base de Datos:** PostgreSQL 15+ / MySQL 8.x  
**Curso:** Curso Integrador I: Sistemas Software (Sección 57524)  
**Docente:** Ing. Yony Zamata Condori  
**Estudiante:** Frank Emiliano Vargas Huamán  

---

## 1. RESUMEN DE ENTIDADES Y VOLUMETRÍA ESTIMADA

| Nombre de Tabla | Descripción / Propósito de Negocio | Tipo de Tabla | Crecimiento Estimado |
| :--- | :--- | :---: | :--- |
| `entidades_financieras` | Catálogo de bancos, cajas municipales y switch CCE. | Maestra | Fijo (~50 registros) |
| `contactos_regulatorios` | Delegados técnicos y de cumplimiento de cada entidad. | Maestra | Fijo (~150 registros) |
| `categorias_incidentes` | Tipología normativa según circular de pagos BCRP. | Catálogo | Estático (3 a 10 registros) |
| `incidentes` | Registro central de fallas operacionales y tecnológicas. | Transaccional | ~5,000 registros / año |
| `historial_estados` | Bitácora inmutable de transiciones y auditoría (Append-Only). | Auditoría | ~25,000 registros / año |
| `reportes_regulatorios_bcrp` | Archivos oficiales generados y transmitidos al BCRP. | Regulatoria | ~365 registros / año |

---

## 2. ESTRUCTURA DETALLADA DE TABLAS

### 2.1. Tabla: `entidades_financieras`
- **Llave Primaria:** `id_entidad`
- **Descripción:** Almacena las entidades financieras y cámaras de compensación autorizadas en el sistema de pagos peruano.

| Columna | Tipo de Dato | Longitud | Nulo | Restricción / Default | Descripción de Negocio |
| :--- | :--- | :---: | :---: | :--- | :--- |
| `id_entidad` | BIGSERIAL | 8 bytes | NO | PRIMARY KEY | Identificador único autoincremental de la entidad. |
| `codigo_bcrp` | VARCHAR | 10 | NO | UNIQUE | Código normativo del BCRP (ej: '002', '003', '901'). |
| `razon_social` | VARCHAR | 150 | NO | - | Razón social legal completa de la entidad financiera. |
| `nombre_comercial`| VARCHAR | 80 | NO | - | Nombre del canal o marca comercial (ej: 'BCP (Yape)'). |
| `canal_interoperable`| VARCHAR | 60 | NO | - | Tipo de canal transaccional (Billetera móvil, switch). |
| `url_healthcheck` | VARCHAR | 255 | SÍ | - | Endpoint REST/HTTP de sondeo para el worker de telemetría. |
| `frecuencia_monitoreo_seg` | INT | 4 bytes | NO | DEFAULT 30, CHECK (>=5) | Frecuencia en segundos para las consultas de disponibilidad. |
| `activo` | BOOLEAN | 1 byte | NO | DEFAULT TRUE | Indica si la entidad está habilitada para supervisión. |
| `fecha_creacion` | TIMESTAMP | 8 bytes | NO | DEFAULT CURRENT_TIMESTAMP | Marca de tiempo de registro de la entidad. |

---

### 2.2. Tabla: `categorias_incidentes`
- **Llave Primaria:** `id_categoria`
- **Descripción:** Clasificación formal de contingencias según los lineamientos de la regulación BCRP.

| Columna | Tipo de Dato | Longitud | Nulo | Restricción / Default | Descripción de Negocio |
| :--- | :--- | :---: | :---: | :--- | :--- |
| `id_categoria` | BIGSERIAL | 8 bytes | NO | PRIMARY KEY | Identificador único de la categoría. |
| `codigo_normativo`| VARCHAR | 20 | NO | UNIQUE | Código de la circular (DISP_SERV, FRAUDE_FIN, BRECHA_DATOS). |
| `nombre` | VARCHAR | 100 | NO | - | Nombre legible de la categoría para el operador. |
| `descripcion` | TEXT | Variable | NO | - | Detalle explicativo de los eventos amparados bajo este rubro. |
| `es_deteccion_automatica` | BOOLEAN | 1 byte | NO | DEFAULT FALSE | Determina si el worker de telemetría puede asignarla automáticamente. |

---

### 2.3. Tabla: `incidentes`
- **Llave Primaria:** `id_incidente`
- **Llaves Foráneas:** `id_entidad` $\rightarrow$ `entidades_financieras.id_entidad`, `id_categoria` $\rightarrow$ `categorias_incidentes.id_categoria`.

| Columna | Tipo de Dato | Longitud | Nulo | Restricción / Default | Descripción de Negocio |
| :--- | :--- | :---: | :---: | :--- | :--- |
| `id_incidente` | BIGSERIAL | 8 bytes | NO | PRIMARY KEY | Identificador numérico interno. |
| `codigo_ticket` | VARCHAR | 30 | NO | UNIQUE | Código oficial del incidente (ej: 'INC-2026-00001'). |
| `id_entidad` | BIGINT | 8 bytes | NO | FK (entidades_financieras) | Entidad afectada por el incidente. |
| `id_categoria` | BIGINT | 8 bytes | NO | FK (categorias_incidentes)| Categoría normativa asignada. |
| `severidad` | VARCHAR | 15 | NO | CHECK (BAJA, MEDIA, ALTA, CRITICA) | Nivel de criticidad e impacto operacional. |
| `origen_deteccion`| VARCHAR | 20 | NO | CHECK (AUTOMATICO, MANUAL_OPERADOR)| Si fue inferido por sonda o ingresado por personal. |
| `estado_actual` | VARCHAR | 25 | NO | CHECK (REGISTRADO, EN_EVALUACION, EN_MITIGACION, RESUELTO, CERRADO) | Estado actual en la máquina de estados. |
| `fecha_hora_inicio` | TIMESTAMP | 8 bytes | NO | - | Fecha y hora real en que se originó la contingencia. |
| `fecha_hora_deteccion` | TIMESTAMP | 8 bytes | NO | - | Momento en que fue detectada por el sistema o reportada. |
| `fecha_hora_solucion` | TIMESTAMP | 8 bytes | SÍ | - | Momento exacto del restablecimiento del servicio. |
| `servicio_afectado` | VARCHAR | 100 | NO | - | Microservicio, switch o endpoint involucrado. |
| `descripcion_detallada` | TEXT | Variable | NO | - | Bitácora técnica y vector de la falla. |
| `impacto_estimado_usuarios` | INT | 4 bytes | NO | DEFAULT 0 | Proyección de transacciones o clientes perjudicados. |
| `usuario_creador` | VARCHAR | 80 | NO | - | Usuario corporativo de AD o Daemon del sistema. |
| `fecha_creacion` | TIMESTAMP | 8 bytes | NO | DEFAULT CURRENT_TIMESTAMP | Fecha de inserción física en base de datos. |

---

### 2.4. Tabla: `historial_estados`
- **Llave Primaria:** `id_historial`
- **Llave Foránea:** `id_incidente` $\rightarrow$ `incidentes.id_incidente`.

| Columna | Tipo de Dato | Longitud | Nulo | Restricción / Default | Descripción de Negocio |
| :--- | :--- | :---: | :---: | :--- | :--- |
| `id_historial` | BIGSERIAL | 8 bytes | NO | PRIMARY KEY | Identificador único de auditoría. |
| `id_incidente` | BIGINT | 8 bytes | NO | FK (incidentes) | Incidente al cual pertenece esta transición. |
| `estado_anterior` | VARCHAR | 25 | SÍ | - | Estado previo al cambio (NULL en creación). |
| `estado_nuevo` | VARCHAR | 25 | NO | - | Nuevo estado alcanzado en el ciclo de vida. |
| `fecha_transicion`| TIMESTAMP | 8 bytes | NO | DEFAULT CURRENT_TIMESTAMP | Marca de tiempo inmutable con milisegundos. |
| `usuario_responsable`| VARCHAR | 80 | NO | - | Persona de AD o proceso que ordenó el cambio. |
| `comentario_tecnico` | TEXT | Variable | NO | - | Justificación de ingeniería para avanzar de estado. |

---

### 2.5. Tabla: `reportes_regulatorios_bcrp`
- **Llave Primaria:** `id_reporte`

| Columna | Tipo de Dato | Longitud | Nulo | Restricción / Default | Descripción de Negocio |
| :--- | :--- | :---: | :---: | :--- | :--- |
| `id_reporte` | BIGSERIAL | 8 bytes | NO | PRIMARY KEY | Clave primaria del reporte consolidado. |
| `numero_envio` | VARCHAR | 40 | NO | UNIQUE | Código de remisión (ej: 'SFT-BCRP-20260903-01'). |
| `fecha_generacion`| TIMESTAMP | 8 bytes | NO | DEFAULT CURRENT_TIMESTAMP | Fecha de compilación del paquete normativo. |
| `nombre_archivo` | VARCHAR | 120 | NO | - | Nombre físico del archivo plano .TXT generado. |
| `total_registros` | INT | 4 bytes | NO | - | Total de incidentes detallados en el cuerpo. |
| `hash_sha256` | VARCHAR | 64 | NO | - | Hash criptográfico de comprobación de integridad. |
| `estado_envio` | VARCHAR | 20 | NO | CHECK (PENDIENTE, ENVIADO, RECHAZADO, ACEPTADO) | Estado de recepción ante el BCRP. |
| `contenido_plano`| TEXT | Variable | NO | - | Texto completo del archivo plano emitido. |
| `usuario_generador`| VARCHAR | 80 | NO | - | Usuario que firmó y disparó el reporte normativo. |

---

## 3. Mecanismos de Alta Disponibilidad, Concurrencia e Integridad Forense

### 3.1. Secuencia Atómica para Tickets Regulatorios
- **Objeto:** `seq_ticket_incidente`
- **Tipo:** `SEQUENCE` (PostgreSQL)
- **Propósito:** Generar correlativos monotónicos crecientes para el código de ticket `INC-YYYYMMDD-XXXXX`, garantizando orden cronológico y eliminando colisiones por aleatoriedad.

### 3.2. Restricción Parcial Única Anti-Carreras (TOCTOU Prevention)
- **Objeto:** `uniq_incidente_abierto_por_entidad`
- **Definición:** `CREATE UNIQUE INDEX uniq_incidente_abierto_por_entidad ON incidentes (id_entidad) WHERE estado_actual NOT IN ('RESUELTO', 'CERRADO');`
- **Impacto de Negocio:** Impide a nivel de motor ACID que dos o más procesos de telemetría o analistas registren simultáneamente incidentes abiertos para la misma entidad financiera.

### 3.3. Inmutabilidad Estricta de la Bitácora (Trigger Append-Only)
- **Función:** `fn_bloquear_modificacion_historial()`
- **Trigger:** `trg_prohibir_mutacion_historial`
- **Definición:** Bloquea todo intento de `UPDATE` o `DELETE` sobre la tabla `historial_estados` emitiendo una excepción `VIOLACIÓN NORMATIVA BCRP`. La tabla es estrictamente de solo anexado (*Append-Only*).


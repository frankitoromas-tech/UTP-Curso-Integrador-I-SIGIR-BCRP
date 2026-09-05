-- ====================================================================================
-- SISTEMA INTEGRAL DE GESTIÓN DE INCIDENTES REGULATORIOS (SIGIR - BCRP)
-- Curso Integrador I: Sistemas Software (Sección 57524)
-- Docente: Ing. Yony Zamata Condori
-- Alumno: Frank Emiliano Vargas Huamán
-- Script DDL: Definición de Esquema de Base de Datos Relacional (PostgreSQL 15+)
-- ====================================================================================

-- 1. Limpieza preventiva de objetos previos si existieran
DROP TABLE IF EXISTS historial_estados CASCADE;
DROP TABLE IF EXISTS incidentes CASCADE;
DROP TABLE IF EXISTS reportes_regulatorios_bcrp CASCADE;
DROP TABLE IF EXISTS contactos_regulatorios CASCADE;
DROP TABLE IF EXISTS categorias_incidentes CASCADE;
DROP TABLE IF EXISTS entidades_financieras CASCADE;

-- 2. Tabla: Entidades Financieras del Ecosistema de Pagos Digitales
CREATE TABLE entidades_financieras (
    id_entidad BIGSERIAL PRIMARY KEY,
    codigo_bcrp VARCHAR(10) NOT NULL UNIQUE,
    razon_social VARCHAR(150) NOT NULL,
    nombre_comercial VARCHAR(80) NOT NULL,
    canal_interoperable VARCHAR(60) NOT NULL,       -- 'BILLETERA_MOVIL', 'SWITCH_CCE', 'BANCA_MOVIL'
    url_healthcheck VARCHAR(255) NULL,              -- Endpoint para el sondeo de telemetría automática
    frecuencia_monitoreo_seg INT NOT NULL DEFAULT 30,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_frecuencia_valida CHECK (frecuencia_monitoreo_seg >= 5)
);

COMMENT ON TABLE entidades_financieras IS 'Catálogo de bancos, cajas municipales y switches regulados por el BCRP.';
COMMENT ON COLUMN entidades_financieras.codigo_bcrp IS 'Identificador único normativo según la circular de pagos del BCRP.';
COMMENT ON COLUMN entidades_financieras.url_healthcheck IS 'URL del servicio REST donde el worker sondea la disponibilidad técnica.';

-- 3. Tabla: Contactos y Delegados Regulatorios por Entidad
CREATE TABLE contactos_regulatorios (
    id_contacto BIGSERIAL PRIMARY KEY,
    id_entidad BIGINT NOT NULL,
    nombre_completo VARCHAR(120) NOT NULL,
    email VARCHAR(100) NOT NULL,
    telefono VARCHAR(25) NOT NULL,
    cargo VARCHAR(80) NOT NULL,
    es_principal BOOLEAN NOT NULL DEFAULT FALSE,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_contacto_entidad FOREIGN KEY (id_entidad) 
        REFERENCES entidades_financieras(id_entidad) ON DELETE CASCADE
);

-- 4. Tabla: Catálogo Normativo de Categorías de Incidentes
CREATE TABLE categorias_incidentes (
    id_categoria BIGSERIAL PRIMARY KEY,
    codigo_normativo VARCHAR(20) NOT NULL UNIQUE,   -- 'DISP_SERV', 'FRAUDE_FIN', 'BRECHA_DATOS'
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT NOT NULL,
    es_deteccion_automatica BOOLEAN NOT NULL DEFAULT FALSE
);

COMMENT ON TABLE categorias_incidentes IS 'Tipificación de contingencias según normativa de pagos digitales del BCRP.';
COMMENT ON COLUMN categorias_incidentes.es_deteccion_automatica IS 'True si el incidente puede ser inferido directamente por la sonda de red.';

-- 5. Tabla: Incidentes Principales
CREATE TABLE incidentes (
    id_incidente BIGSERIAL PRIMARY KEY,
    codigo_ticket VARCHAR(30) NOT NULL UNIQUE,      -- Formato: INC-YYYY-NNNNN
    id_entidad BIGINT NOT NULL,
    id_categoria BIGINT NOT NULL,
    severidad VARCHAR(15) NOT NULL,
    origen_deteccion VARCHAR(20) NOT NULL,
    estado_actual VARCHAR(25) NOT NULL DEFAULT 'REGISTRADO',
    fecha_hora_inicio TIMESTAMP NOT NULL,
    fecha_hora_deteccion TIMESTAMP NOT NULL,
    fecha_hora_solucion TIMESTAMP NULL,
    servicio_afectado VARCHAR(100) NOT NULL,
    descripcion_detallada TEXT NOT NULL,
    impacto_estimado_usuarios INT NOT NULL DEFAULT 0,
    usuario_creador VARCHAR(80) NOT NULL,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_incidente_entidad FOREIGN KEY (id_entidad) 
        REFERENCES entidades_financieras(id_entidad) ON DELETE RESTRICT,
    CONSTRAINT fk_incidente_categoria FOREIGN KEY (id_categoria) 
        REFERENCES categorias_incidentes(id_categoria) ON DELETE RESTRICT,
    CONSTRAINT chk_severidad CHECK (severidad IN ('BAJA', 'MEDIA', 'ALTA', 'CRITICA')),
    CONSTRAINT chk_origen CHECK (origen_deteccion IN ('AUTOMATICO', 'MANUAL_OPERADOR')),
    CONSTRAINT chk_estado CHECK (estado_actual IN ('REGISTRADO', 'EN_EVALUACION', 'EN_MITIGACION', 'RESUELTO', 'CERRADO'))
);

COMMENT ON TABLE incidentes IS 'Registro central de fallas y contingencias operacionales del sistema de pagos.';

-- 6. Tabla: Historial y Auditoría Inmutable de Estados (Append-Only)
CREATE TABLE historial_estados (
    id_historial BIGSERIAL PRIMARY KEY,
    id_incidente BIGINT NOT NULL,
    estado_anterior VARCHAR(25) NULL,
    estado_nuevo VARCHAR(25) NOT NULL,
    fecha_transicion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    usuario_responsable VARCHAR(80) NOT NULL,
    comentario_tecnico TEXT NOT NULL,
    CONSTRAINT fk_historial_incidente FOREIGN KEY (id_incidente) 
        REFERENCES incidentes(id_incidente) ON DELETE CASCADE
);

COMMENT ON TABLE historial_estados IS 'Bitácora inmutable de transiciones de ciclo de vida para auditoría BCRP/SBS.';

-- 7. Tabla: Reportes Regulatorios Consolidados SFT BCRP
CREATE TABLE reportes_regulatorios_bcrp (
    id_reporte BIGSERIAL PRIMARY KEY,
    numero_envio VARCHAR(40) NOT NULL UNIQUE,
    fecha_generacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    nombre_archivo VARCHAR(120) NOT NULL,
    total_registros INT NOT NULL,
    hash_sha256 VARCHAR(64) NOT NULL,
    estado_envio VARCHAR(20) NOT NULL DEFAULT 'PENDIENTE',
    contenido_plano TEXT NOT NULL,
    usuario_generador VARCHAR(80) NOT NULL,
    CONSTRAINT chk_estado_envio CHECK (estado_envio IN ('PENDIENTE', 'ENVIADO', 'RECHAZADO', 'ACEPTADO'))
);

COMMENT ON TABLE reportes_regulatorios_bcrp IS 'Archivos planos TXT oficiales exportados al BCRP con firma criptográfica.';

-- 8. Creación de Índices de Alto Rendimiento para Consultas Operativas y Reportes
CREATE INDEX idx_incidentes_estado_actual ON incidentes (estado_actual);
CREATE INDEX idx_incidentes_id_entidad ON incidentes (id_entidad);
CREATE INDEX idx_incidentes_fechas ON incidentes (fecha_hora_inicio, fecha_hora_deteccion);
CREATE INDEX idx_incidentes_ticket ON incidentes (codigo_ticket);
CREATE INDEX idx_historial_id_incidente ON historial_estados (id_incidente);
CREATE INDEX idx_entidades_activo ON entidades_financieras (activo);

-- 9. Secuencia Atómica para Generación Segura de Tickets
CREATE SEQUENCE IF NOT EXISTS seq_ticket_incidente START WITH 10001 INCREMENT BY 1;

-- 10. Restricción Parcial Única Anti-Carrera (TOCTOU Prevention)
-- Asegura a nivel de base de datos que una entidad no pueda tener más de un incidente abierto simultáneo
CREATE UNIQUE INDEX IF NOT EXISTS uniq_incidente_abierto_por_entidad 
ON incidentes (id_entidad) 
WHERE estado_actual NOT IN ('RESUELTO', 'CERRADO');

-- 11. Mecanismo de Inmutabilidad Estricta (Append-Only) para Auditoría BCRP
CREATE OR REPLACE FUNCTION fn_bloquear_modificacion_historial()
RETURNS TRIGGER AS $$
BEGIN
    RAISE EXCEPTION 'VIOLACIÓN NORMATIVA BCRP: La tabla historial_estados es estrictamente inmutable (Append-Only). No se permiten operaciones UPDATE ni DELETE.';
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg_prohibir_mutacion_historial ON historial_estados;
CREATE TRIGGER trg_prohibir_mutacion_historial
BEFORE UPDATE OR DELETE ON historial_estados
FOR EACH ROW EXECUTE FUNCTION fn_bloquear_modificacion_historial();


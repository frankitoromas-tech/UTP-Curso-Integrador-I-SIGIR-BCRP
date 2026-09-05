-- ====================================================================================
-- SISTEMA INTEGRAL DE GESTIÓN DE INCIDENTES REGULATORIOS (SIGIR - BCRP)
-- Curso Integrador I: Sistemas Software (Sección 57524)
-- Script: Datos Semilla Iniciales (Seed Data)
-- ====================================================================================

-- 1. Inserción de Categorías Normativas del BCRP
INSERT INTO categorias_incidentes (codigo_normativo, nombre, descripcion, es_deteccion_automatica) VALUES
('DISP_SERV', 'Disponibilidad de Servicios', 'Caída de infraestructura, interrupción de APIs interoperables, degradación extrema de latencia o timeout transaccional.', TRUE),
('FRAUDE_FIN', 'Fraude Financiero', 'Transacciones no autorizadas concurrentes, vulneración de credenciales, suplantación de identidad en billeteras móviles.', FALSE),
('BRECHA_DATOS', 'Manipulación de Información / Brechas de Datos', 'Fuga de datos personales o financieros de usuarios, alteración ilícita de saldos o transacciones no repudiadas.', FALSE);

-- 2. Inserción de Entidades Financieras del Ecosistema de Pagos Digitales
INSERT INTO entidades_financieras (codigo_bcrp, razon_social, nombre_comercial, canal_interoperable, url_healthcheck, frecuencia_monitoreo_seg, activo) VALUES
('002', 'Banco de Crédito del Perú S.A.', 'BCP (Yape)', 'BILLETERA_MOVIL', 'https://api.viabcp.com/v1/payments/health', 15, TRUE),
('003', 'Banco Internacional del Perú S.A.A.', 'Interbank (Plim)', 'BILLETERA_MOVIL', 'https://api.interbank.pe/v1/interop/status', 15, TRUE),
('801', 'Caja Municipal de Ahorro y Crédito Cusco S.A.', 'Caja Cusco (Tunki)', 'BILLETERA_MOVIL', 'https://api.cmac-cusco.com.pe/actuator/health', 30, TRUE),
('812', 'Caja Rural de Ahorro y Crédito de los Andes S.A.', 'Caja de los Andes', 'BANCA_MOVIL', 'https://api.losandes.pe/healthcheck', 30, TRUE),
('901', 'Cámara de Compensación Electrónica S.A.', 'CCE (Switch Central)', 'SWITCH_CCE', 'https://switch.cce.com.pe/api/ping', 10, TRUE);

-- 3. Inserción de Contactos Regulatorios
INSERT INTO contactos_regulatorios (id_entidad, nombre_completo, email, telefono, cargo, es_principal) VALUES
(1, 'Carlos Mendoza Rivera', 'cmendoza@bcp.com.pe', '+51 987654321', 'Gerente de Continuidad Operativa', TRUE),
(2, 'Valeria Torres Salas', 'vtorres@interbank.pe', '+51 981234567', 'Jefe de Seguridad de la Información', TRUE),
(3, 'Guillermo Huamán Quispe', 'ghuaman@cmac-cusco.com.pe', '+51 984556677', 'Subgerente de Tecnologías de Información', TRUE),
(4, 'Milagros Vega Castro', 'mvega@losandes.pe', '+51 971122334', 'Oficial de Cumplimiento Normativo', TRUE),
(5, 'Eduardo Campos Prado', 'ecampos@cce.com.pe', '+51 999887766', 'Director de Operaciones y Redes', TRUE);

-- 4. Inserción de Incidentes de Demostración para Validación de la Etapa 01
-- Incidente 1: Caída de Servicio detectada automáticamente (En evaluación)
INSERT INTO incidentes (codigo_ticket, id_entidad, id_categoria, severidad, origen_deteccion, estado_actual, fecha_hora_inicio, fecha_hora_deteccion, servicio_afectado, descripcion_detallada, impacto_estimado_usuarios, usuario_creador)
VALUES (
    'INC-2026-00001',
    1, -- BCP (Yape)
    1, -- DISP_SERV
    'CRITICA',
    'AUTOMATICO',
    'EN_EVALUACION',
    CURRENT_TIMESTAMP - INTERVAL '45 minutes',
    CURRENT_TIMESTAMP - INTERVAL '44 minutes',
    'API_TRANSFERENCIAS_INMEDIATAS',
    'Falla masiva HTTP 503 Service Unavailable detectada en el endpoint de interoperabilidad hacia Plim.',
    12500,
    'SISTEMA_TELEMETRIA_DAEMON'
);

-- Incidente 2: Fraude Financiero registrado manualmente (En mitigación)
INSERT INTO incidentes (codigo_ticket, id_entidad, id_categoria, severidad, origen_deteccion, estado_actual, fecha_hora_inicio, fecha_hora_deteccion, servicio_afectado, descripcion_detallada, impacto_estimado_usuarios, usuario_creador)
VALUES (
    'INC-2026-00002',
    2, -- Interbank (Plim)
    2, -- FRAUDE_FIN
    'ALTA',
    'MANUAL_OPERADOR',
    'EN_MITIGACION',
    CURRENT_TIMESTAMP - INTERVAL '3 hours',
    CURRENT_TIMESTAMP - INTERVAL '2 hours',
    'PASARELA_DE_PAGOS_QR',
    'Reporte de transacciones anómalas concurrentes mediante códigos QR alterados en comercios de Lima Metropolitana.',
    420,
    'frank.vargas@bcrp.local'
);

-- Incidente 3: Falla resuelta y cerrada lista para reporte BCRP
INSERT INTO incidentes (codigo_ticket, id_entidad, id_categoria, severidad, origen_deteccion, estado_actual, fecha_hora_inicio, fecha_hora_deteccion, fecha_hora_solucion, servicio_afectado, descripcion_detallada, impacto_estimado_usuarios, usuario_creador)
VALUES (
    'INC-2026-00003',
    5, -- CCE
    1, -- DISP_SERV
    'ALTA',
    'AUTOMATICO',
    'CERRADO',
    CURRENT_TIMESTAMP - INTERVAL '1 day',
    CURRENT_TIMESTAMP - INTERVAL '23 hours 58 minutes',
    CURRENT_TIMESTAMP - INTERVAL '22 hours 30 minutes',
    'ENLACE_CANAL_SWITCH_CCE',
    'Timeout de sincronización en la cola de mensajes MQ hacia Caja Cusco. Reinicio de brokers aplicado exitosamente.',
    8900,
    'SISTEMA_TELEMETRIA_DAEMON'
);

-- 5. Inserción de Trazabilidad Histórica para los Incidentes de Demostración
INSERT INTO historial_estados (id_incidente, estado_anterior, estado_nuevo, fecha_transicion, usuario_responsable, comentario_tecnico) VALUES
(1, NULL, 'REGISTRADO', CURRENT_TIMESTAMP - INTERVAL '44 minutes', 'SISTEMA_TELEMETRIA_DAEMON', 'Creación automática por disparo de sonda de disponibilidad.'),
(1, 'REGISTRADO', 'EN_EVALUACION', CURRENT_TIMESTAMP - INTERVAL '30 minutes', 'frank.vargas@bcrp.local', 'Asignación al equipo de contingencia de BCP para revisión de switches perimetrales.'),
(2, NULL, 'REGISTRADO', CURRENT_TIMESTAMP - INTERVAL '2 hours', 'frank.vargas@bcrp.local', 'Registro manual tras notificación formal del Oficial de Seguridad de Interbank.'),
(2, 'REGISTRADO', 'EN_MITIGACION', CURRENT_TIMESTAMP - INTERVAL '90 minutes', 'frank.vargas@bcrp.local', 'Bloqueo preventivo de cuentas destino sospechosas e invocación de protocolo anti-phishing.'),
(3, NULL, 'REGISTRADO', CURRENT_TIMESTAMP - INTERVAL '23 hours 58 minutes', 'SISTEMA_TELEMETRIA_DAEMON', 'Pérdida de conectividad detectada.'),
(3, 'REGISTRADO', 'EN_MITIGACION', CURRENT_TIMESTAMP - INTERVAL '23 hours 10 minutes', 'soporte.cce@cce.com.pe', 'Reinicio de brokers de mensajería MQ.'),
(3, 'EN_MITIGACION', 'RESUELTO', CURRENT_TIMESTAMP - INTERVAL '22 hours 30 minutes', 'soporte.cce@cce.com.pe', 'Canal operativo al 100% y latencia estabilizada en 14ms.'),
(3, 'RESUELTO', 'CERRADO', CURRENT_TIMESTAMP - INTERVAL '22 hours', 'supervisor.bcrp@bcrp.local', 'Verificación de liquidación y cierre formal para compilación de reporte SFT.');

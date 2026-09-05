/**
 * ==========================================================================
 * SISTEMA INTEGRAL DE GESTIÓN DE INCIDENTES REGULATORIOS (SIGIR - BCRP)
 * Lógica Reactiva de Consola NOC & Cliente API Híbrido (Online / Offline Fallback)
 * Autor: Frank Emiliano Vargas Huamán | Curso Integrador I (UTP)
 * ==========================================================================
 */

// Configuración de integración con Backend Spring Boot
const API_CONFIG = {
    baseUrl: 'http://localhost:8080/api/v1',
    healthUrl: 'http://localhost:8080/actuator/health',
    authHeader: 'Basic ' + btoa('operador.noc:Bcrp2026!')
};

let isBackendConnected = false;

// Estado global de la aplicación (Sincronizado con PostgreSQL cuando online, o mock local offline)
const AppState = {
    entidades: [
        { id: 1, codigo: '002', nombre: 'BCP (Yape)', canal: 'Billetera Móvil', estado: 'SALUDABLE', latencia: 18, incidentes: 1 },
        { id: 2, codigo: '003', nombre: 'Interbank (Plim)', canal: 'Billetera Móvil', estado: 'SALUDABLE', latencia: 24, incidentes: 1 },
        { id: 3, codigo: '801', nombre: 'Caja Cusco (Tunki)', canal: 'Billetera Móvil', estado: 'SALUDABLE', latencia: 32, incidentes: 0 },
        { id: 4, codigo: '812', nombre: 'Caja de los Andes', canal: 'Banca Móvil', estado: 'SALUDABLE', latencia: 45, incidentes: 0 },
        { id: 5, codigo: '901', nombre: 'CCE (Switch Central)', canal: 'Switch Interbancario', estado: 'SALUDABLE', latencia: 12, incidentes: 1 }
    ],
    incidentes: [
        {
            id: 1,
            ticket: 'INC-20260903-10045',
            idEntidad: 1,
            entidadNombre: 'BCP (Yape)',
            categoriaCodigo: 'DISP_SERV',
            categoriaNombre: 'Disponibilidad de Servicios',
            severidad: 'CRITICA',
            origen: 'AUTOMATICO',
            fechaDeteccion: '2026-09-03 09:30:15',
            servicio: 'API_TRANSFERENCIAS_INMEDIATAS',
            descripcion: 'Caída de endpoint interbancario: HTTP 503 detectado por sonda central.',
            estado: 'EN_EVALUACION'
        },
        {
            id: 2,
            ticket: 'INC-20260903-10046',
            idEntidad: 2,
            entidadNombre: 'Interbank (Plim)',
            categoriaCodigo: 'FRAUDE_FIN',
            categoriaNombre: 'Fraude Financiero',
            severidad: 'ALTA',
            origen: 'MANUAL_OPERADOR',
            fechaDeteccion: '2026-09-03 08:15:00',
            servicio: 'PASARELA_QR_DINAMICO',
            descripcion: 'Reporte de QR adulterados concurrentes en comercios de Lima.',
            estado: 'EN_MITIGACION'
        },
        {
            id: 3,
            ticket: 'INC-20260902-09912',
            idEntidad: 5,
            entidadNombre: 'CCE (Switch Central)',
            categoriaCodigo: 'DISP_SERV',
            categoriaNombre: 'Disponibilidad de Servicios',
            severidad: 'ALTA',
            origen: 'AUTOMATICO',
            fechaDeteccion: '2026-09-02 18:40:22',
            servicio: 'SWITCH_MQ_CCE',
            descripcion: 'Retardo extremo >4500ms en cola MQ. Mitigado con reinicio de canal.',
            estado: 'CERRADO'
        }
    ],
    segundosSondeo: 30
};

// Inicialización del DOM
document.addEventListener('DOMContentLoaded', () => {
    inicializarTelemetria();
    renderizarMatrizIncidentes();
    actualizarKpis();
    configurarEventosUI();
    verificarConectividadBackend();

    // Verificación recurrente de conectividad con Backend cada 15 segundos
    setInterval(verificarConectividadBackend, 15000);
});

/**
 * Verifica si el backend Spring Boot está disponible y sincroniza datos reales
 */
async function verificarConectividadBackend() {
    const pill = document.getElementById('backendStatusPill');
    const dot = document.getElementById('backendStatusDot');
    const text = document.getElementById('backendStatusText');

    try {
        const response = await fetch(API_CONFIG.healthUrl, {
            method: 'GET',
            signal: AbortSignal.timeout(2000)
        });

        if (response.ok) {
            isBackendConnected = true;
            if (pill) {
                pill.className = 'backend-status-pill online';
                pill.title = 'Conectado a Spring Boot REST API & PostgreSQL (bcrp_incident_db)';
            }
            if (dot) dot.style.background = '#10B981';
            if (text) text.innerText = 'Backend Conectado (PostgreSQL)';

            // Sincronizar catálogo e incidentes reales desde PostgreSQL
            await sincronizarConBackend();
            return;
        }
    } catch (e) {
        // Modo offline / mock
    }

    isBackendConnected = false;
    if (pill) {
        pill.className = 'backend-status-pill offline';
        pill.title = 'Ejecutando en Modo Autónomo / Simulación Local. Inicie Docker Compose o RUN_APF1_LOCAL para sincronizar.';
    }
    if (dot) dot.style.background = '#F59E0B';
    if (text) text.innerText = 'Modo Simulación (Local Mock)';
}

/**
 * Sincroniza entidades e incidentes directamente desde la base de datos a través de la API REST
 */
async function sincronizarConBackend() {
    try {
        const resIncidentes = await fetch(`${API_CONFIG.baseUrl}/incidentes`, {
            headers: { 'Authorization': API_CONFIG.authHeader }
        });

        if (resIncidentes.ok) {
            const data = await resIncidentes.json();
            if (Array.isArray(data) && data.length > 0) {
                AppState.incidentes = data.map(dto => ({
                    id: dto.idIncidente,
                    ticket: dto.codigoTicket,
                    idEntidad: dto.idEntidad,
                    entidadNombre: dto.nombreEntidad,
                    categoriaCodigo: dto.codigoCategoria,
                    categoriaNombre: dto.nombreCategoria,
                    severidad: dto.severidad,
                    origen: dto.origenDeteccion,
                    fechaDeteccion: dto.fechaHoraDeteccion ? dto.fechaHoraDeteccion.replace('T', ' ').substring(0, 19) : '',
                    servicio: dto.servicioAfectado,
                    descripcion: dto.descripcionDetallada,
                    estado: dto.estadoActual
                }));
                renderizarMatrizIncidentes();
                actualizarKpis();
            }
        }
    } catch (err) {
        console.warn('Fallo en sincronización con backend:', err);
    }
}

/**
 * Renderiza los nodos de telemetría de cada entidad financiera
 */
function renderizarTelemetria() {
    const grid = document.getElementById('telemetryGrid');
    if (!grid) return;

    grid.innerHTML = AppState.entidades.map(ent => {
        const esCaido = ent.estado === 'CAIDO';
        return `
        <div class="telemetry-node ${esCaido ? 'node-caido' : ''}">
            <div class="node-header">
                <div>
                    <h4 class="node-name">${ent.nombre}</h4>
                    <span class="node-code">${ent.canal} · Cod. ${ent.codigo}</span>
                </div>
                <span class="node-status-pill ${esCaido ? 'caido' : 'saludable'}">
                    ${esCaido ? 'Interrupción' : 'Operativo'}
                </span>
            </div>
            <div class="node-metrics">
                <span>Latencia</span>
                <span class="node-latency" style="color: ${esCaido ? '#EF4444' : '#10B981'}">
                    ${ent.latencia} ms
                </span>
            </div>
        </div>
        `;
    }).join('');
}

/**
 * Control del temporizador de sondeo de telemetría en tiempo real
 */
function inicializarTelemetria() {
    renderizarTelemetria();
    const timerLabel = document.getElementById('timerSondeo');

    setInterval(() => {
        AppState.segundosSondeo--;
        if (AppState.segundosSondeo <= 0) {
            AppState.segundosSondeo = 30;
            // Fluctuación natural de latencias en sondeo
            AppState.entidades.forEach(e => {
                if (e.estado !== 'CAIDO') {
                    e.latencia = Math.floor(14 + Math.random() * 20);
                }
            });
            renderizarTelemetria();

            // Si el backend está online, refrescar datos desde PostgreSQL
            if (isBackendConnected) {
                sincronizarConBackend();
            }
        }
        if (timerLabel) {
            timerLabel.innerText = `Sondeo: ${AppState.segundosSondeo}s`;
        }
    }, 1000);
}

/**
 * Renderiza la tabla de incidentes aplicando filtros
 */
function renderizarMatrizIncidentes() {
    const tbody = document.getElementById('tablaIncidentesBody');
    if (!tbody) return;

    const texto = (document.getElementById('filtroBuscador')?.value || '').toLowerCase();
    const filtroSev = document.getElementById('filtroSeveridad')?.value || 'TODOS';
    const filtroEst = document.getElementById('filtroEstado')?.value || 'TODOS';

    const filtrados = AppState.incidentes.filter(inc => {
        const matchTexto = inc.ticket.toLowerCase().includes(texto) ||
                           inc.entidadNombre.toLowerCase().includes(texto) ||
                           inc.servicio.toLowerCase().includes(texto);
        const matchSev = (filtroSev === 'TODOS') || (inc.severidad === filtroSev);
        const matchEst = (filtroEst === 'TODOS') || (inc.estado === filtroEst);
        return matchTexto && matchSev && matchEst;
    });

    if (filtrados.length === 0) {
        tbody.innerHTML = `<tr><td colspan="8" style="text-align: center; color: var(--text-tertiary); padding: 36px 20px; font-size: 0.85rem;">No se encontraron incidentes con los filtros seleccionados.</td></tr>`;
        return;
    }

    tbody.innerHTML = filtrados.map(inc => `
        <tr>
            <td class="ticket-mono">${inc.ticket}</td>
            <td>
                <div class="entidad-cell">${inc.entidadNombre}</div>
                <span class="servicio-tag">${inc.servicio}</span>
            </td>
            <td style="color: var(--text-secondary);">${inc.categoriaNombre}</td>
            <td><span class="badge-severidad ${inc.severidad.toLowerCase()}">${inc.severidad}</span></td>
            <td><span style="font-size: 0.72rem; font-family: var(--font-mono); color: var(--text-tertiary);">${inc.origen}</span></td>
            <td style="font-family: var(--font-mono); font-size: 0.74rem; color: var(--text-tertiary);">${inc.fechaDeteccion}</td>
            <td><span class="badge-estado ${inc.estado.toLowerCase()}">${inc.estado.replace('_', ' ')}</span></td>
            <td>
                ${renderBotonesAccion(inc)}
            </td>
        </tr>
    `).join('');
}

/**
 * Genera botones de transición según la máquina de estados
 */
function renderBotonesAccion(inc) {
    if (inc.estado === 'REGISTRADO') {
        return `<button class="btn-action-table" onclick="avanzarEstado(${inc.id}, 'EN_EVALUACION')">Evaluar</button>`;
    } else if (inc.estado === 'EN_EVALUACION') {
        return `<button class="btn-action-table" onclick="avanzarEstado(${inc.id}, 'EN_MITIGACION')">Mitigar</button>`;
    } else if (inc.estado === 'EN_MITIGACION') {
        return `<button class="btn-action-table btn-resolve" onclick="avanzarEstado(${inc.id}, 'RESUELTO')">Resolver</button>`;
    } else if (inc.estado === 'RESUELTO') {
        return `<button class="btn-action-table" onclick="avanzarEstado(${inc.id}, 'CERRADO')">Cerrar</button>`;
    } else {
        return `<span style="font-size: 0.72rem; color: var(--text-muted);">Cerrado</span>`;
    }
}

/**
 * Avanza el estado del incidente cumpliendo las reglas de negocio (con persistencia real si backend activo)
 */
window.avanzarEstado = async function(idIncidente, nuevoEstado) {
    const inc = AppState.incidentes.find(i => i.id === idIncidente);
    if (!inc) return;

    if (isBackendConnected) {
        try {
            const resp = await fetch(`${API_CONFIG.baseUrl}/incidentes/${idIncidente}/estado`, {
                method: 'PATCH',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': API_CONFIG.authHeader
                },
                body: JSON.stringify({
                    nuevoEstado: nuevoEstado,
                    comentarioTecnico: `Transición autorizada desde Consola Web NOC hacia ${nuevoEstado}.`,
                    fechaHoraSolucion: (nuevoEstado === 'RESUELTO' || nuevoEstado === 'CERRADO') ? new Date().toISOString() : null
                })
            });

            if (!resp.ok) {
                const errorData = await resp.json().catch(() => ({}));
                alert(`Error en Backend Spring Boot: ${errorData.message || 'Transición inválida en máquina de estados'}`);
                return;
            }
        } catch (err) {
            console.error('Error enviando PATCH a Spring Boot:', err);
        }
    }

    // Actualización reactiva local
    inc.estado = nuevoEstado;

    // Si se resuelve o cierra, normalizar estado de salud de la entidad
    if (nuevoEstado === 'RESUELTO' || nuevoEstado === 'CERRADO') {
        const ent = AppState.entidades.find(e => e.id === inc.idEntidad);
        if (ent) {
            ent.estado = 'SALUDABLE';
            ent.latencia = 16;
            ent.incidentes = Math.max(0, ent.incidentes - 1);
        }
    }

    renderizarTelemetria();
    renderizarMatrizIncidentes();
    actualizarKpis();
};

/**
 * Actualiza los contadores de la barra de KPIs
 */
function actualizarKpis() {
    const total = AppState.incidentes.length;
    const criticos = AppState.incidentes.filter(i => i.estado !== 'CERRADO' && (i.severidad === 'CRITICA' || i.severidad === 'ALTA')).length;
    const resueltos = AppState.incidentes.filter(i => i.estado === 'RESUELTO' || i.estado === 'CERRADO').length;

    const elTotal = document.getElementById('kpiTotal');
    const elCriticos = document.getElementById('kpiCriticos');
    const elResueltos = document.getElementById('kpiResueltos');

    if (elTotal) elTotal.innerText = total;
    if (elCriticos) elCriticos.innerText = criticos;
    if (elResueltos) elResueltos.innerText = resueltos;
}

/**
 * Simulación de caída de servicio automática por telemetría
 */
function simularCaidaAutomatica() {
    const entidadAfectada = AppState.entidades[0]; // BCP (Yape)
    entidadAfectada.estado = 'CAIDO';
    entidadAfectada.latencia = 9999;
    entidadAfectada.incidentes++;

    const nuevoId = AppState.incidentes.length + 1;
    const randomSuffix = Math.floor(10000 + Math.random() * 90000);
    const ahora = new Date().toISOString().replace('T', ' ').substring(0, 19);

    const nuevoIncidente = {
        id: nuevoId,
        ticket: `INC-20260903-${randomSuffix}`,
        idEntidad: entidadAfectada.id,
        entidadNombre: entidadAfectada.nombre,
        categoriaCodigo: 'DISP_SERV',
        categoriaNombre: 'Disponibilidad de Servicios',
        severidad: 'CRITICA',
        origen: 'AUTOMATICO',
        fechaDeteccion: ahora,
        servicio: 'SWITCH_INTEROPERABLE_BCP',
        descripcion: 'ALERTA AUTOMÁTICA: Timeout >3000ms capturado por el worker concurrente de telemetría.',
        estado: 'REGISTRADO'
    };

    AppState.incidentes.unshift(nuevoIncidente);
    renderizarTelemetria();
    renderizarMatrizIncidentes();
    actualizarKpis();

    alert(`⚠️ ALERTA BCRP: Se ha auto-registrado el ticket ${nuevoIncidente.ticket} para ${entidadAfectada.nombre} por caída de disponibilidad.`);
}

/**
 * Generación del archivo TXT normativo SFT BCRP con hash SHA-256
 */
async function generarReporteSft() {
    // Si el backend está disponible, compilar el reporte oficial desde el servidor Spring Boot
    if (isBackendConnected) {
        try {
            const resp = await fetch(`${API_CONFIG.baseUrl}/reportes/generar-sft`, {
                method: 'POST',
                headers: { 'Authorization': API_CONFIG.authHeader }
            });

            if (resp.ok) {
                const reporteBD = await resp.json();
                document.getElementById('sftContenidoTxt').innerText = reporteBD.contenidoPlano;
                document.getElementById('sftHashSha256').innerText = reporteBD.hashSha256;

                document.getElementById('btnDescargarTxtFile').onclick = () => {
                    const blob = new Blob([reporteBD.contenidoPlano], { type: 'text/plain;charset=utf-8' });
                    const a = document.createElement('a');
                    a.href = URL.createObjectURL(blob);
                    a.download = reporteBD.nombreArchivo || `INCIDENTES_PAGOS_SFT.txt`;
                    a.click();
                };

                document.getElementById('modalSft').classList.add('active');
                return;
            }
        } catch (err) {
            console.warn('No se pudo compilar desde backend, usando compilador local:', err);
        }
    }

    // Modo autónomo local con cálculo SHA-256 Web Crypto API
    const cerrados = AppState.incidentes.filter(i => i.estado === 'CERRADO');
    const timestamp = new Date().toISOString().replace(/[-:T]/g, '').slice(0, 14);
    const numEnvio = `SFT-BCRP-${timestamp}`;

    let contenido = `HEADER|${numEnvio}|${timestamp}|${cerrados.length}\n`;

    cerrados.forEach(inc => {
        contenido += `DETALLE|${inc.ticket}|00${inc.idEntidad}|${inc.entidadNombre}|${inc.categoriaCodigo}|${inc.severidad}|${inc.fechaDeteccion}|${inc.fechaDeteccion}|${inc.origen}|${inc.servicio}\n`;
    });

    const sha256 = await calcularSha256(contenido);
    contenido += `FOOTER|${sha256}`;

    document.getElementById('sftContenidoTxt').innerText = contenido;
    document.getElementById('sftHashSha256').innerText = sha256;

    document.getElementById('btnDescargarTxtFile').onclick = () => {
        const blob = new Blob([contenido], { type: 'text/plain;charset=utf-8' });
        const a = document.createElement('a');
        a.href = URL.createObjectURL(blob);
        a.download = `INCIDENTES_PAGOS_${timestamp}.txt`;
        a.click();
    };

    document.getElementById('modalSft').classList.add('active');
}

/**
 * Función criptográfica auxiliar SHA-256 nativa del navegador
 */
async function calcularSha256(str) {
    const buffer = new TextEncoder().encode(str);
    const hashBuffer = await crypto.subtle.digest('SHA-256', buffer);
    const hashArray = Array.from(new Uint8Array(hashBuffer));
    return hashArray.map(b => b.toString(16).padStart(2, '0')).join('');
}

/**
 * Configuración de eventos y modales
 */
function configurarEventosUI() {
    // Filtros
    document.getElementById('filtroBuscador')?.addEventListener('input', renderizarMatrizIncidentes);
    document.getElementById('filtroSeveridad')?.addEventListener('change', renderizarMatrizIncidentes);
    document.getElementById('filtroEstado')?.addEventListener('change', renderizarMatrizIncidentes);

    // Acciones
    document.getElementById('btnSimularCaida')?.addEventListener('click', simularCaidaAutomatica);
    document.getElementById('btnGenerarSft')?.addEventListener('click', generarReporteSft);

    // Modal Manual
    const modalManual = document.getElementById('modalManual');
    document.getElementById('btnNuevoManual')?.addEventListener('click', () => {
        modalManual.classList.add('active');
    });
    document.getElementById('btnCerrarModalManual')?.addEventListener('click', () => {
        modalManual.classList.remove('active');
    });
    document.getElementById('btnCancelarManual')?.addEventListener('click', () => {
        modalManual.classList.remove('active');
    });

    // Envío Formulario Manual
    document.getElementById('formRegistroManual')?.addEventListener('submit', async (e) => {
        e.preventDefault();
        const idEnt = parseInt(document.getElementById('manualEntidad').value);
        const entidad = AppState.entidades.find(en => en.id === idEnt);
        const cat = document.getElementById('manualCategoria').value;
        const sev = document.getElementById('manualSeveridad').value;
        const serv = document.getElementById('manualServicio').value;
        const desc = document.getElementById('manualDescripcion').value;
        const ahoraISO = new Date().toISOString();

        if (isBackendConnected) {
            try {
                const resp = await fetch(`${API_CONFIG.baseUrl}/incidentes/manual`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': API_CONFIG.authHeader
                    },
                    body: JSON.stringify({
                        idEntidad: idEnt,
                        codigoCategoria: cat,
                        severidad: sev,
                        fechaHoraInicio: ahoraISO,
                        servicioAfectado: serv,
                        descripcionDetallada: desc,
                        impactoEstimadoUsuarios: 50
                    })
                });

                if (resp.ok) {
                    const nuevo = await resp.json();
                    AppState.incidentes.unshift({
                        id: nuevo.idIncidente,
                        ticket: nuevo.codigoTicket,
                        idEntidad: nuevo.idEntidad,
                        entidadNombre: nuevo.nombreEntidad,
                        categoriaCodigo: nuevo.codigoCategoria,
                        categoriaNombre: nuevo.nombreCategoria,
                        severidad: nuevo.severidad,
                        origen: nuevo.origenDeteccion,
                        fechaDeteccion: nuevo.fechaHoraDeteccion.replace('T', ' ').substring(0, 19),
                        servicio: nuevo.servicioAfectado,
                        descripcion: nuevo.descripcionDetallada,
                        estado: nuevo.estadoActual
                    });
                    modalManual.classList.remove('active');
                    document.getElementById('formRegistroManual').reset();
                    renderizarMatrizIncidentes();
                    actualizarKpis();
                    alert(`✅ Incidente registrado y persistido en PostgreSQL con Ticket: ${nuevo.codigoTicket}`);
                    return;
                } else {
                    const errObj = await resp.json().catch(() => ({}));
                    alert(`Error en persistencia: ${errObj.message || 'La entidad ya posee un ticket activo.'}`);
                    return;
                }
            } catch (err) {
                console.warn('Falla en llamada API backend:', err);
            }
        }

        // Modo offline / fallback
        const nuevoId = AppState.incidentes.length + 1;
        const ticket = `INC-20260903-${Math.floor(10000 + Math.random() * 90000)}`;
        const ahora = new Date().toISOString().replace('T', ' ').substring(0, 19);

        AppState.incidentes.unshift({
            id: nuevoId,
            ticket: ticket,
            idEntidad: idEnt,
            entidadNombre: entidad ? entidad.nombre : 'Entidad',
            categoriaCodigo: cat,
            categoriaNombre: cat === 'FRAUDE_FIN' ? 'Fraude Financiero' : 'Manipulación de Información',
            severidad: sev,
            origen: 'MANUAL_OPERADOR',
            fechaDeteccion: ahora,
            servicio: serv,
            descripcion: desc,
            estado: 'REGISTRADO'
        });

        modalManual.classList.remove('active');
        document.getElementById('formRegistroManual').reset();
        renderizarMatrizIncidentes();
        actualizarKpis();
    });

    // Modal SFT
    const modalSft = document.getElementById('modalSft');
    document.getElementById('btnCerrarModalSft')?.addEventListener('click', () => modalSft.classList.remove('active'));
    document.getElementById('btnCerrarSft')?.addEventListener('click', () => modalSft.classList.remove('active'));
}

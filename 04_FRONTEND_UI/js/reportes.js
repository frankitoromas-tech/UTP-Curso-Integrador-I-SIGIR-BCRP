/* ==========================================================================
   SIGIR - BCRP | Módulo de Reportes Clave & Compilador SFT (APF2)
   Lógica de Métricas (MTTR/MTTD/Uptime), Gráficos Reactivos y Sellado Criptográfico
   Autor: Frank Emiliano Vargas Huamán | Curso Integrador I (UTP)
   ========================================================================== */

const BASE = 'http://localhost:8080/api/v1';
const AUTH = 'Basic ' + btoa('operador.noc:Bcrp2026!');

const MOCK = [
  { idEntidad: 1, codigoBcrp: '002', nombreEntidad: 'BCP (Yape)', totalIncidentes: 2, incidentesCriticos: 2, mttrMinutos: 95.5, mttdMinutos: 1.0, minutosIndisponibles: 191, uptimePct: 99.56 },
  { idEntidad: 2, codigoBcrp: '003', nombreEntidad: 'Interbank (Plim)', totalIncidentes: 1, incidentesCriticos: 1, mttrMinutos: 45.0, mttdMinutos: 1.0, minutosIndisponibles: 45, uptimePct: 99.90 },
  { idEntidad: 3, codigoBcrp: '801', nombreEntidad: 'Caja Cusco (Tunki)', totalIncidentes: 0, incidentesCriticos: 0, mttrMinutos: 0.0, mttdMinutos: 0.0, minutosIndisponibles: 0, uptimePct: 100.00 },
  { idEntidad: 4, codigoBcrp: '812', nombreEntidad: 'Caja de los Andes', totalIncidentes: 0, incidentesCriticos: 0, mttrMinutos: 0.0, mttdMinutos: 0.0, minutosIndisponibles: 0, uptimePct: 100.00 },
  { idEntidad: 5, codigoBcrp: '901', nombreEntidad: 'CCE (Switch Central)', totalIncidentes: 1, incidentesCriticos: 1, mttrMinutos: 120.0, mttdMinutos: 2.0, minutosIndisponibles: 120, uptimePct: 99.72 }
];

let lastSFT = '', lastHash = '', lastName = 'INCIDENTES_PAGOS_SFT.txt';

function qp() {
  const d = document.getElementById('fDesde')?.value;
  const h = document.getElementById('fHasta')?.value;
  if (d && h) {
    return `?desde=${encodeURIComponent(d)}:00&hasta=${encodeURIComponent(h)}:00`;
  }
  return '';
}

async function getJSON(path) {
  const r = await fetch(BASE + path + qp(), { headers: { Authorization: AUTH } });
  if (!r.ok) throw new Error('HTTP ' + r.status);
  return r.json();
}

/**
 * Renderizado de gráfico moderno de barras de disponibilidad con umbral SLA 99.90%
 */
function drawChart(rows) {
  const c = document.getElementById('chartUptime');
  if (!c) return;
  const ctx = c.getContext('2d');
  
  // Limpiar lienzo
  ctx.clearRect(0, 0, c.width, c.height);

  const paddingLeft = 60;
  const paddingRight = 40;
  const paddingTop = 30;
  const paddingBottom = 40;
  const chartWidth = c.width - paddingLeft - paddingRight;
  const chartHeight = c.height - paddingTop - paddingBottom;

  // Rango de escala visual (de 99.0% a 100.0%)
  const minScale = 99.0;
  const maxScale = 100.0;
  const scaleRange = maxScale - minScale;

  // Línea guía de umbral BCRP 99.90%
  const slaTarget = 99.90;
  const slaY = paddingTop + chartHeight - ((slaTarget - minScale) / scaleRange) * chartHeight;

  ctx.save();
  ctx.strokeStyle = 'rgba(239, 68, 68, 0.7)';
  ctx.lineWidth = 1.5;
  ctx.setLineDash([6, 4]);
  ctx.beginPath();
  ctx.moveTo(paddingLeft, slaY);
  ctx.lineTo(c.width - paddingRight, slaY);
  ctx.stroke();

  // Etiqueta de umbral SLA
  ctx.fillStyle = '#EF4444';
  ctx.font = 'bold 11px Plus Jakarta Sans, sans-serif';
  ctx.textAlign = 'right';
  ctx.fillText('Meta SLA BCRP (99.90%)', c.width - paddingRight - 8, slaY - 6);
  ctx.restore();

  // Dibujar barras
  const numBars = rows.length;
  const barWidth = Math.min(65, (chartWidth / numBars) * 0.55);
  const gap = chartWidth / numBars;

  rows.forEach((r, i) => {
    const x = paddingLeft + (i * gap) + (gap - barWidth) / 2;
    const clampedPct = Math.max(minScale, Math.min(maxScale, r.uptimePct));
    const h = ((clampedPct - minScale) / scaleRange) * chartHeight;
    const y = paddingTop + chartHeight - h;

    const cumpleSla = r.uptimePct >= 99.90;

    // Color de barra con degradado
    const grad = ctx.createLinearGradient(0, y, 0, y + h);
    if (cumpleSla) {
      grad.addColorStop(0, '#10B981');
      grad.addColorStop(1, '#065F46');
    } else {
      grad.addColorStop(0, '#F59E0B');
      grad.addColorStop(1, '#92400E');
    }

    ctx.fillStyle = grad;
    ctx.beginPath();
    ctx.roundRect ? ctx.roundRect(x, y, barWidth, h, [4, 4, 0, 0]) : ctx.fillRect(x, y, barWidth, h);
    ctx.fill();

    // Texto de porcentaje encima de la barra
    ctx.fillStyle = cumpleSla ? '#34D399' : '#FBBF24';
    ctx.font = 'bold 11px JetBrains Mono, monospace';
    ctx.textAlign = 'center';
    ctx.fillText(`${r.uptimePct.toFixed(2)}%`, x + barWidth / 2, y - 6);

    // Etiqueta de nombre de entidad debajo
    ctx.fillStyle = '#94A3B8';
    ctx.font = '500 11px Plus Jakarta Sans, sans-serif';
    const shortName = r.nombreEntidad.split('(')[0].trim();
    ctx.fillText(shortName, x + barWidth / 2, c.height - 18);

    // Código BCRP
    ctx.fillStyle = '#64748B';
    ctx.font = '10px JetBrains Mono, monospace';
    ctx.fillText(`[${r.codigoBcrp}]`, x + barWidth / 2, c.height - 6);
  });
}

/**
 * Carga de datos y KPIs
 */
async function cargar() {
  let rows, kpis;
  try {
    rows = await getJSON('/reportes/indisponibilidad');
    kpis = await getJSON('/reportes/kpis');
    const pill = document.getElementById('connPill');
    if (pill) {
      pill.innerText = 'Backend Conectado (PostgreSQL)';
      pill.className = 'backend-status-pill online';
    }
  } catch (e) {
    rows = MOCK;
    kpis = { totalIncidentes: 4, activos: 2, cerrados: 2, mttrGlobalMinutos: 71.8, mttdGlobalMinutos: 1.3, uptimeGlobalPct: 99.76 };
    const pill = document.getElementById('connPill');
    if (pill) {
      pill.innerText = 'Modo Simulación (Autónomo)';
      pill.className = 'backend-status-pill offline';
    }
  }

  const ent = document.getElementById('fEntidad')?.value;
  const view = ent ? rows.filter(r => String(r.idEntidad) === ent) : rows;

  const tbody = document.getElementById('tbodyIndisp');
  if (tbody) {
    tbody.innerHTML = view.map(r => `
      <tr>
        <td style="font-weight:600;color:var(--text-primary);">${r.nombreEntidad}</td>
        <td class="ticket-mono">${r.codigoBcrp}</td>
        <td>${r.totalIncidentes}</td>
        <td><span class="badge-severidad ${r.incidentesCriticos > 0 ? 'critica' : 'baja'}">${r.incidentesCriticos}</span></td>
        <td style="font-family:var(--font-mono);">${r.mttrMinutos.toFixed(1)}</td>
        <td style="font-family:var(--font-mono);">${r.mttdMinutos.toFixed(1)}</td>
        <td style="font-family:var(--font-mono);">${r.minutosIndisponibles}</td>
        <td><span class="badge-estado ${r.uptimePct >= 99.90 ? 'resuelto' : 'en_mitigacion'}">${r.uptimePct.toFixed(2)}%</span></td>
      </tr>
    `).join('');
  }

  // Actualizar KPIs de cabecera
  const kTot = document.getElementById('kTotal');
  const kAct = document.getElementById('kActivos');
  const kCer = document.getElementById('kCerrados');
  const kMttr = document.getElementById('kMttr');
  const kMttd = document.getElementById('kMttd');
  const kUpt = document.getElementById('kUptime');

  if (kTot) kTot.innerText = kpis.totalIncidentes;
  if (kAct) kAct.innerText = kpis.activos;
  if (kCer) kCer.innerText = kpis.cerrados;
  if (kMttr) kMttr.innerText = kpis.mttrGlobalMinutos;
  if (kMttd) kMttd.innerText = kpis.mttdGlobalMinutos;
  if (kUpt) {
    kUpt.innerText = kpis.uptimeGlobalPct + '%';
    kUpt.style.color = kpis.uptimeGlobalPct >= 99.90 ? '#38BDF8' : '#F59E0B';
  }

  drawChart(view);

  // Poblar select si no tiene opciones
  const sel = document.getElementById('fEntidad');
  if (sel && sel.options.length <= 1) {
    rows.forEach(r => {
      const o = document.createElement('option');
      o.value = r.idEntidad;
      o.text = `${r.nombreEntidad} (${r.codigoBcrp})`;
      sel.appendChild(o);
    });
  }
}

// Configurar fechas por defecto al inicio del mes corriente
function inicializarFechas() {
  const now = new Date();
  const firstDay = new Date(now.getFullYear(), now.getMonth(), 1, 0, 0);
  const toLocalISO = (d) => new Date(d.getTime() - d.getTimezoneOffset() * 60000).toISOString().slice(0, 16);
  
  const fD = document.getElementById('fDesde');
  const fH = document.getElementById('fHasta');
  if (fD && !fD.value) fD.value = toLocalISO(firstDay);
  if (fH && !fH.value) fH.value = toLocalISO(now);
}

// Eventos de usuario
document.getElementById('btnAplicar')?.addEventListener('click', cargar);

document.getElementById('fEntidad')?.addEventListener('change', cargar);

document.getElementById('btnCSV')?.addEventListener('click', () => {
  const rows = [...document.querySelectorAll('#tbodyIndisp tr')].map(tr => 
    [...tr.children].map(td => td.innerText.replace(/;/g, ',')).join(';')
  ).join('\n');
  const blob = new Blob(['Entidad;CodigoBCRP;IncidentesTotales;Criticos;MTTR_min;MTTD_min;MinutosIndisponibles;Uptime_Pct\n' + rows], { type: 'text/csv;charset=utf-8' });
  const a = document.createElement('a');
  a.href = URL.createObjectURL(blob);
  a.download = `INDISPONIBILIDAD_BCRP_${new Date().toISOString().slice(0, 10)}.csv`;
  a.click();
});

document.getElementById('btnPrint')?.addEventListener('click', () => window.print());

document.getElementById('btnSFT')?.addEventListener('click', async () => {
  const ent = document.getElementById('fEntidad')?.value;
  let url = BASE + '/reportes/generar-sft';
  const query = qp();
  if (query) {
    url += query;
    if (ent) url += `&idEntidad=${ent}`;
  } else if (ent) {
    url += `?idEntidad=${ent}`;
  }

  try {
    const r = await fetch(url, { method: 'POST', headers: { Authorization: AUTH } });
    if (r.ok) {
      const j = await r.json();
      lastSFT = j.contenidoPlano;
      lastHash = j.hashSha256;
      lastName = j.nombreArchivo || 'SFT_BCRP_PAGOS.txt';
    } else {
      throw new Error('Fallback local');
    }
  } catch (e) {
    // Generación matemática real en modo simulación
    const timestamp = new Date().toISOString().replace(/[-:T]/g, '').slice(0, 14);
    const numEnvio = `SFT-BCRP-${timestamp}`;
    const entFilter = document.getElementById('fEntidad')?.value;
    const view = entFilter ? MOCK.filter(r => String(r.idEntidad) === entFilter) : MOCK.filter(r => r.totalIncidentes > 0);

    let raw = `01|${numEnvio}|${timestamp.slice(0,6)}|20131370649|MESA_REGULATORIA_BCRP\n`;
    view.forEach((r, idx) => {
      raw += `02|INC-20261003-${10001 + idx}|${r.codigoBcrp}|${r.nombreEntidad}|DISP_SERV|CRITICA|2026-10-03 09:30:00|2026-10-03 11:05:00|${r.minutosIndisponibles}|AUTOMATICO\n`;
    });

    const totalMinutos = view.reduce((acc, c) => acc + c.minutosIndisponibles, 0);

    // Calcular hash SHA-256 criptográfico con Web Crypto API
    const buf = await crypto.subtle.digest('SHA-256', new TextEncoder().encode(raw));
    const hash = Array.from(new Uint8Array(buf)).map(b => b.toString(16).padStart(2, '0')).join('');
    raw += `03|${view.length}|${totalMinutos}|${hash}`;

    lastSFT = raw;
    lastHash = hash;
    lastName = `SFT_BCRP_PAGOS_${timestamp}.txt`;
  }

  document.getElementById('sftPreview').innerText = lastSFT;
  document.getElementById('sftHash').innerText = lastHash;
});

document.getElementById('btnDL')?.addEventListener('click', () => {
  if (!lastSFT) {
    alert('Primero genere el archivo SFT haciendo clic en "Generar SFT con filtros".');
    return;
  }
  const blob = new Blob([lastSFT], { type: 'text/plain;charset=utf-8' });
  const a = document.createElement('a');
  a.href = URL.createObjectURL(blob);
  a.download = lastName;
  a.click();
});

document.getElementById('btnHist')?.addEventListener('click', async () => {
  try {
    const r = await fetch(BASE + '/reportes', { headers: { Authorization: AUTH } });
    if (r.ok) {
      const list = await r.json();
      document.getElementById('tbodyHist').innerHTML = list.map(x => `
        <tr>
          <td>${x.idReporte ?? x.id ?? ''}</td>
          <td class="ticket-mono">${x.numeroEnvio}</td>
          <td>${x.nombreArchivo}</td>
          <td>${x.totalRegistros}</td>
          <td style="font-family:var(--font-mono);font-size:.7rem;color:#38BDF8">${(x.hashSha256 || '').slice(0, 16)}…</td>
          <td><button class="btn-action-table" onclick="window.open('${BASE}/reportes/${x.idReporte ?? x.id}/descargar-txt')">TXT</button></td>
        </tr>
      `).join('');
      return;
    }
    throw new Error('Fallback');
  } catch (e) {
    // Historial simulado para demostración sin backend activo
    const mockHist = [
      { idReporte: 1, numeroEnvio: 'SFT-BCRP-20260901-0800', nombreArchivo: 'SFT_BCRP_PAGOS_20260901.txt', totalRegistros: 3, hashSha256: '9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08' },
      { idReporte: 2, numeroEnvio: 'SFT-BCRP-20260915-1830', nombreArchivo: 'SFT_BCRP_PAGOS_20260915.txt', totalRegistros: 2, hashSha256: '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8' },
      { idReporte: 3, numeroEnvio: 'SFT-BCRP-20261001-0900', nombreArchivo: 'SFT_BCRP_PAGOS_20261001.txt', totalRegistros: 4, hashSha256: '4b227777d4dd1fc61c6f884f48641d02b4d121d3fd328cb08b5531fcacdabf8a' }
    ];
    document.getElementById('tbodyHist').innerHTML = mockHist.map(x => `
      <tr>
        <td>${x.idReporte}</td>
        <td class="ticket-mono">${x.numeroEnvio}</td>
        <td>${x.nombreArchivo}</td>
        <td>${x.totalRegistros}</td>
        <td style="font-family:var(--font-mono);font-size:.7rem;color:#38BDF8">${x.hashSha256.slice(0, 16)}…</td>
        <td><button class="btn-action-table" onclick="alert('Descargando archivo histórico firmado SHA-256: ${x.nombreArchivo}')">TXT</button></td>
      </tr>
    `).join('');
  }
});

// Inicialización
inicializarFechas();
cargar();

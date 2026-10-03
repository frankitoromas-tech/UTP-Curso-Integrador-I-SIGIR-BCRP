/* Modulo Reportes Clave APF2: indisponibilidad + KPIs + SFT. Ruteo separado de index.html */
const BASE = 'http://localhost:8080/api/v1';
const AUTH = 'Basic ' + btoa('operador.noc:Bcrp2026!');
const MOCK = [
  { idEntidad: 1, codigoBcrp: '002', nombreEntidad: 'BCP (Yape)', totalIncidentes: 2, incidentesCriticos: 2, mttrMinutos: 95.5, mttdMinutos: 1.0, minutosIndisponibles: 191, uptimePct: 99.56 },
  { idEntidad: 2, codigoBcrp: '003', nombreEntidad: 'Interbank (Plim)', totalIncidentes: 1, incidentesCriticos: 1, mttrMinutos: 0, mttdMinutos: 1.0, minutosIndisponibles: 45, uptimePct: 99.9 },
  { idEntidad: 5, codigoBcrp: '901', nombreEntidad: 'CCE (Switch Central)', totalIncidentes: 1, incidentesCriticos: 1, mttrMinutos: 120, mttdMinutos: 2.0, minutosIndisponibles: 120, uptimePct: 99.72 }
];
let lastSFT = '', lastHash = '', lastName = 'INCIDENTES_SFT.txt';

function qp() {
  const d = document.getElementById('fDesde').value, h = document.getElementById('fHasta').value;
  let s = '';
  if (d && h) s = `?desde=${d}:00&hasta=${h}:00`;
  return s;
}
async function getJSON(path) {
  const r = await fetch(BASE + path + qp(), { headers: { Authorization: AUTH } });
  if (!r.ok) throw new Error('HTTP ' + r.status);
  return r.json();
}
function drawChart(rows) {
  const c = document.getElementById('chartUptime');
  const ctx = c.getContext('2d');
  ctx.clearRect(0, 0, c.width, c.height);
  const max = 100;
  const bw = c.width / Math.max(1, rows.length * 2);
  rows.forEach((r, i) => {
    const h = (r.uptimePct / max) * 180;
    const x = 40 + i * bw * 1.6, y = 200 - h;
    ctx.fillStyle = r.uptimePct >= 99.5 ? '#10B981' : '#F59E0B';
    ctx.fillRect(x, y, bw, h);
    ctx.fillStyle = '#94A3B8'; ctx.font = '11px sans-serif';
    ctx.fillText(r.codigoBcrp + ' ' + r.uptimePct + '%', x - 6, 214);
  });
}
async function cargar() {
  let rows, kpis;
  try {
    rows = await getJSON('/reportes/indisponibilidad');
    kpis = await getJSON('/reportes/kpis');
    document.getElementById('connPill').innerText = 'Backend Conectado';
    document.getElementById('connPill').className = 'backend-status-pill online';
  } catch (e) {
    rows = MOCK;
    kpis = { totalIncidentes: 4, activos: 3, cerrados: 1, mttrGlobalMinutos: 71.8, mttdGlobalMinutos: 1.3, uptimeGlobalPct: 99.72 };
    document.getElementById('connPill').innerText = 'Modo Simulación (mock)';
  }
  const ent = document.getElementById('fEntidad').value;
  const view = ent ? rows.filter(r => String(r.idEntidad) === ent) : rows;
  document.getElementById('tbodyIndisp').innerHTML = view.map(r =>
    `<tr><td>${r.nombreEntidad}</td><td class="ticket-mono">${r.codigoBcrp}</td><td>${r.totalIncidentes}</td><td>${r.incidentesCriticos}</td><td>${r.mttrMinutos}</td><td>${r.mttdMinutos}</td><td>${r.minutosIndisponibles}</td><td>${r.uptimePct}%</td></tr>`).join('');
  document.getElementById('kTotal').innerText = kpis.totalIncidentes;
  document.getElementById('kActivos').innerText = kpis.activos;
  document.getElementById('kCerrados').innerText = kpis.cerrados;
  document.getElementById('kMttr').innerText = kpis.mttrGlobalMinutos;
  document.getElementById('kMttd').innerText = kpis.mttdGlobalMinutos;
  document.getElementById('kUptime').innerText = kpis.uptimeGlobalPct + '%';
  drawChart(view);
  const sel = document.getElementById('fEntidad');
  if (sel.options.length <= 1) rows.forEach(r => { const o = document.createElement('option'); o.value = r.idEntidad; o.text = r.nombreEntidad; sel.appendChild(o); });
}
document.getElementById('btnAplicar').onclick = cargar;
document.getElementById('btnCSV').onclick = () => {
  const rows = [...document.querySelectorAll('#tbodyIndisp tr')].map(tr => [...tr.children].map(td => td.innerText).join(';')).join('\n');
  const blob = new Blob(['Entidad;Cod;Total;Criticos;MTTR;MTTD;MinIndisp;Uptime\n' + rows], { type: 'text/csv;charset=utf-8' });
  const a = document.createElement('a'); a.href = URL.createObjectURL(blob); a.download = 'indisponibilidad.csv'; a.click();
};
document.getElementById('btnPrint').onclick = () => window.print();
document.getElementById('btnSFT').onclick = async () => {
  try {
    const ent = document.getElementById('fEntidad').value;
    let url = BASE + '/reportes/generar-sft' + qp();
    if (ent && qp().includes('?')) url += `&idEntidad=${ent}`;
    const r = await fetch(url, { method: 'POST', headers: { Authorization: AUTH } });
    const j = await r.json();
    lastSFT = j.contenidoPlano; lastHash = j.hashSha256; lastName = j.nombreArchivo;
  } catch (e) { lastSFT = 'HEADER|SFT-BCRP-demo|20261003|1\nDETALLE|INC-demo|002|BCP (Yape)|DISP_SERV|CRITICA|20261003|20261003|AUTOMATICO|SWITCH\nFOOTER|demo'; lastHash = 'demo'; }
  document.getElementById('sftPreview').innerText = lastSFT;
  document.getElementById('sftHash').innerText = lastHash;
};
document.getElementById('btnDL').onclick = () => {
  const blob = new Blob([lastSFT], { type: 'text/plain;charset=utf-8' });
  const a = document.createElement('a'); a.href = URL.createObjectURL(blob); a.download = lastName; a.click();
};
document.getElementById('btnHist').onclick = async () => {
  try {
    const r = await fetch(BASE + '/reportes', { headers: { Authorization: AUTH } });
    const list = await r.json();
    document.getElementById('tbodyHist').innerHTML = list.map(x =>
      `<tr><td>${x.idReporte ?? x.id ?? ''}</td><td class="ticket-mono">${x.numeroEnvio}</td><td>${x.nombreArchivo}</td><td>${x.totalRegistros}</td><td style="font-size:.65rem">${(x.hashSha256 || '').slice(0, 16)}…</td><td><button class="btn-action-table" onclick="window.open('${BASE}/reportes/${x.idReporte ?? x.id}/descargar-txt')">TXT</button></td></tr>`).join('');
  } catch (e) { document.getElementById('tbodyHist').innerHTML = '<tr><td colspan="6">Sin backend: historial no disponible.</td></tr>'; }
};
cargar();

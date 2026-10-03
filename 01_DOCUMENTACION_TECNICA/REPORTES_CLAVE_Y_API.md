# Módulo de Reportes Clave — SIGIR BCRP (APF2)

## 1. Alcance
- Reporte de indisponibilidad por entidad (MTTR, MTTD, Uptime%).
- Dashboard de KPIs globales con gráfico de barras (canvas, sin dependencias).
- Archivo normativo SFT con formato vigente HEADER/DETALLE/FOOTER + SHA-256 (sin cambios de formato).
- Exportación: CSV compatible Excel + PDF vía impresión + TXT normativo.
- Frontend separado: `04_FRONTEND_UI/reportes.html` + `js/reportes.js`, ruteado desde `index.html` (botón Reportes Clave).

## 2. Decisión de formato SFT
Se mantiene el formato actual. Motivos: los tests `SftReportServiceTest`, el seed y el modal NOC dependen de él; la circular real no fue provista por cátedra. Cambiarlo rompería compatibilidad sin beneficio evaluable. Solo se agregaron filtros opcionales.

## 3. Endpoints (base `http://localhost:8080/api/v1`, auth Basic `operador.noc:Bcrp2026!`, roles OPERADOR/SUPERVISOR/ADMIN)

| Método | Endpoint | Query params | Descripción |
|---|---|---|---|
| GET | `/reportes/indisponibilidad` | `desde, hasta` (ISO `yyyy-MM-ddTHH:mm:ss`) | Lista por entidad: total, críticos, MTTR/MTTD min, min indisponibles, uptime%. RF-09 |
| GET | `/reportes/kpis` | `desde, hasta` | Totales + MTTR/MTTD global + uptime global + detalle por entidad. RF-09 |
| POST | `/reportes/generar-sft` | `desde, hasta, idEntidad` (opcionales) | Compila lote de CERRADOS con filtros, guarda en `reportes_regulatorios_bcrp`. RF-08 |
| GET | `/reportes` | — | Historial de lotes generados |
| GET | `/reportes/{id}/descargar-txt` | — | Descarga TXT con `Content-Disposition` |

Ejemplos:
```bash
curl -u operador.noc:Bcrp2026! "http://localhost:8080/api/v1/reportes/indisponibilidad"
curl -u operador.noc:Bcrp2026! "http://localhost:8080/api/v1/reportes/kpis?desde=2026-09-01T00:00:00&hasta=2026-09-30T23:59:59"
curl -X POST -u operador.noc:Bcrp2026! "http://localhost:8080/api/v1/reportes/generar-sft?desde=2026-09-01T00:00:00&hasta=2026-09-30T23:59:59&idEntidad=1"
```

## 4. Fórmulas
- `MTTR = avg(fechaSolucion - fechaInicio)` minutos, solo con solución no nula.
- `MTTD = avg(fechaDeteccion - fechaInicio)` minutos.
- `Uptime% = 100 * (1 - minIndisponibles / 43200)` por entidad (período 30 días); global pondera por N entidades. Clamp 0–100, redondeo 2 decimales.
- Implementación: `service/ReporteAnaliticoService.java`.

## 5. Frontend
- `reportes.html`: filtros fecha/entidad → `GET /indisponibilidad` + `/kpis`; gráfico canvas de uptime; tabla; SFT con filtros + preview hash + historial + descarga; CSV + Print-PDF. Fallback mock si backend caído.
- Ruteo: `index.html` → botón `Reportes Clave` → `reportes.html`; retorno `← Volver a Consola NOC`.

## 6. Despliegue y prueba
```bash
cd 05_DESPLIEGUE_Y_SCRIPTS
docker-compose up -d --build
# Frontend: http://localhost/reportes.html  | Consola: http://localhost
# API docs: http://localhost:8080/swagger-ui.html
cd ../03_BACKEND_SPRINGBOOT
mvn test
```
Seguridad: endpoints bajo `/api/v1/reportes/**` requieren rol (SecurityConfig); SFT persiste hash SHA-256 e historial append-only.

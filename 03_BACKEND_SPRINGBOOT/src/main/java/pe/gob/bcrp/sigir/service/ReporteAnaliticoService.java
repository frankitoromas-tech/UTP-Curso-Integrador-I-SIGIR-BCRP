package pe.gob.bcrp.sigir.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.gob.bcrp.sigir.domain.entity.EntidadFinanciera;
import pe.gob.bcrp.sigir.domain.entity.Incidente;
import pe.gob.bcrp.sigir.domain.enums.EstadoIncidente;
import pe.gob.bcrp.sigir.domain.enums.Severidad;
import pe.gob.bcrp.sigir.repository.EntidadFinancieraRepository;
import pe.gob.bcrp.sigir.repository.IncidenteRepository;
import pe.gob.bcrp.sigir.web.dto.IndisponibilidadDTO;
import pe.gob.bcrp.sigir.web.dto.KpiDashboardDTO;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Servicio analitico del Modulo de Reportes Clave (APF2).
 * Calcula indisponibilidad por entidad: MTTR, MTTD y % Uptime.
 * Formulas:
 *  MTTR = avg(fechaSolucion - fechaInicio) en minutos (solo resueltos/cerrados)
 *  MTTD = avg(fechaDeteccion - fechaInicio) en minutos
 *  Uptime% = 100 * (1 - minutosIndisponibles / minutosPeriodo), periodo por defecto 30 dias.
 */
@Service
public class ReporteAnaliticoService {

    private static final long MINUTOS_PERIODO_DEFAULT = 30L * 24 * 60; // 43200

    private final IncidenteRepository incidenteRepository;
    private final EntidadFinancieraRepository entidadRepository;

    public ReporteAnaliticoService(IncidenteRepository incidenteRepository,
                                   EntidadFinancieraRepository entidadRepository) {
        this.incidenteRepository = incidenteRepository;
        this.entidadRepository = entidadRepository;
    }

    @Transactional(readOnly = true)
    public List<IndisponibilidadDTO> indisponibilidadPorEntidad(LocalDateTime desde, LocalDateTime hasta) {
        List<EntidadFinanciera> entidades = entidadRepository.findAll();
        List<IndisponibilidadDTO> out = new ArrayList<>();
        for (EntidadFinanciera e : entidades) {
            List<Incidente>incs = incidenteRepository.findByEntidadIdEntidad(e.getIdEntidad());
            if (desde != null && hasta != null) {
                incs = incs.stream()
                        .filter(i -> !i.getFechaHoraInicio().isBefore(desde) && !i.getFechaHoraInicio().isAfter(hasta))
                        .toList();
            }
            out.add(calcularFila(e, incs, MINUTOS_PERIODO_DEFAULT));
        }
        return out;
    }

    @Transactional(readOnly = true)
    public KpiDashboardDTO dashboardGlobal(LocalDateTime desde, LocalDateTime hasta) {
        List<IndisponibilidadDTO> filas = indisponibilidadPorEntidad(desde, hasta);
        KpiDashboardDTO dto = new KpiDashboardDTO();
        dto.setPorEntidad(filas);
        long total = filas.stream().mapToLong(IndisponibilidadDTO::getTotalIncidentes).sum();

        List<Incidente> todos = incidenteRepository.findAll();
        if (desde != null && hasta != null) {
            todos = todos.stream()
                    .filter(i -> !i.getFechaHoraInicio().isBefore(desde) && !i.getFechaHoraInicio().isAfter(hasta))
                    .toList();
        }
        long cerrados = todos.stream()
                .filter(i -> i.getEstadoActual() == EstadoIncidente.CERRADO || i.getEstadoActual() == EstadoIncidente.RESUELTO)
                .count();
        dto.setTotalIncidentes(total);
        dto.setCerrados(cerrados);
        dto.setActivos(total - cerrados);
        dto.setMttrGlobalMinutos(promedio(todos.stream()
                .filter(i -> i.getFechaHoraSolucion() != null)
                .mapToDouble(i -> Duration.between(i.getFechaHoraInicio(), i.getFechaHoraSolucion()).toMinutes())
                .toArray()));
        dto.setMttdGlobalMinutos(promedio(todos.stream()
                .mapToDouble(i -> Duration.between(i.getFechaHoraInicio(), i.getFechaHoraDeteccion()).toMinutes())
                .toArray()));
        long minIndisp = filas.stream().mapToLong(IndisponibilidadDTO::getMinutosIndisponibles).sum();
        int nEnt = Math.max(1, filas.size());
        double uptime = 100.0 * (1.0 - ((double) minIndisp / (MINUTOS_PERIODO_DEFAULT * nEnt)));
        dto.setUptimeGlobalPct(redondear(clamp(uptime, 0, 100)));
        return dto;
    }

    private IndisponibilidadDTO calcularFila(EntidadFinanciera e, List<Incidente> incs, long minutosPeriodo) {
        IndisponibilidadDTO d = new IndisponibilidadDTO();
        d.setIdEntidad(e.getIdEntidad());
        d.setCodigoBcrp(e.getCodigoBcrp());
        d.setNombreEntidad(e.getNombreComercial());
        d.setTotalIncidentes(incs.size());
        d.setIncidentesCriticos(incs.stream()
                .filter(i -> i.getSeveridad() == Severidad.CRITICA || i.getSeveridad() == Severidad.ALTA)
                .count());
        double[] mttrVals = incs.stream()
                .filter(i -> i.getFechaHoraSolucion() != null)
                .mapToDouble(i -> Duration.between(i.getFechaHoraInicio(), i.getFechaHoraSolucion()).toMinutes())
                .toArray();
        double[] mttdVals = incs.stream()
                .mapToDouble(i -> Math.max(0, Duration.between(i.getFechaHoraInicio(), i.getFechaHoraDeteccion()).toMinutes()))
                .toArray();
        d.setMttrMinutos(redondear(promedio(mttrVals)));
        d.setMttdMinutos(redondear(promedio(mttdVals)));
        long minIndisp = (long) incs.stream()
                .mapToDouble(i -> {
                    LocalDateTime fin = i.getFechaHoraSolucion() != null ? i.getFechaHoraSolucion() : LocalDateTime.now();
                    return Math.max(0, Duration.between(i.getFechaHoraInicio(), fin).toMinutes());
                }).sum();
        d.setMinutosIndisponibles(minIndisp);
        d.setUptimePct(redondear(clamp(100.0 * (1.0 - ((double) minIndisp / minutosPeriodo)), 0, 100)));
        return d;
    }

    private Double promedio(double[] vals) {
        if (vals == null || vals.length == 0) return 0.0;
        double s = 0; for (double v : vals) s += v;
        return s / vals.length;
    }

    private Double redondear(Double v) {
        if (v == null) return 0.0;
        return Math.round(v * 100.0) / 100.0;
    }

    private double clamp(double v, double min, double max) {
        return Math.max(min, Math.min(max, v));
    }
}

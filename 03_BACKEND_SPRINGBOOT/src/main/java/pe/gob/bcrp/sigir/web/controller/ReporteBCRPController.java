package pe.gob.bcrp.sigir.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import pe.gob.bcrp.sigir.domain.entity.ReporteBCRP;
import pe.gob.bcrp.sigir.service.ReporteAnaliticoService;
import pe.gob.bcrp.sigir.service.SftReportService;
import pe.gob.bcrp.sigir.web.dto.IndisponibilidadDTO;
import pe.gob.bcrp.sigir.web.dto.KpiDashboardDTO;

import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/api/v1/reportes")
@Tag(name = "Reportes Regulatorios BCRP", description = "Generación y descarga de archivos normativos SFT en texto plano con hash SHA-256")
public class ReporteBCRPController {

    private final SftReportService reportService;
    private final ReporteAnaliticoService analiticoService;

    public ReporteBCRPController(SftReportService reportService, ReporteAnaliticoService analiticoService) {
        this.reportService = reportService;
        this.analiticoService = analiticoService;
    }

    @PostMapping("/generar-sft")
    @Operation(summary = "Compilar lote de incidentes cerrados en formato oficial SFT BCRP (.TXT)")
    public ResponseEntity<ReporteBCRP> generarSft(@AuthenticationPrincipal UserDetails userDetails,
                                                 @RequestParam(required = false) String desde,
                                                 @RequestParam(required = false) String hasta,
                                                 @RequestParam(required = false) Long idEntidad) {
        String usuario = (userDetails != null) ? userDetails.getUsername() : "supervisor.bcrp";
        if (desde != null && hasta != null) {
            java.time.LocalDateTime d = java.time.LocalDateTime.parse(desde);
            java.time.LocalDateTime h = java.time.LocalDateTime.parse(hasta);
            return ResponseEntity.ok(reportService.generarReporteNormativoSFTConFiltros(usuario, d, h, idEntidad));
        }
        ReporteBCRP reporte = reportService.generarReporteNormativoSFT(usuario);
        return ResponseEntity.ok(reporte);
    }

    @GetMapping
    @Operation(summary = "Listar el histórico de reportes generados para el BCRP")
    public ResponseEntity<List<ReporteBCRP>> listar() {
        return ResponseEntity.ok(reportService.listarReportes());
    }

    @GetMapping("/{id}/descargar-txt")
    @Operation(summary = "Descargar el archivo plano .TXT generado para remisión al BCRP")
    public ResponseEntity<byte[]> descargarTxt(@PathVariable Long id) {
        ReporteBCRP rep = reportService.obtenerPorId(id);
        byte[] contenido = rep.getContenidoPlano().getBytes(StandardCharsets.UTF_8);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + rep.getNombreArchivo() + "\"")
                .contentType(MediaType.TEXT_PLAIN)
                .body(contenido);
    }

    @GetMapping("/indisponibilidad")
    @Operation(summary = "Reporte de indisponibilidad por entidad: MTTR, MTTD y Uptime%. RF-09")
    public ResponseEntity<List<IndisponibilidadDTO>> indisponibilidad(
            @RequestParam(required = false) String desde,
            @RequestParam(required = false) String hasta) {
        java.time.LocalDateTime d = desde != null ? java.time.LocalDateTime.parse(desde) : null;
        java.time.LocalDateTime h = hasta != null ? java.time.LocalDateTime.parse(hasta) : null;
        return ResponseEntity.ok(analiticoService.indisponibilidadPorEntidad(d, h));
    }

    @GetMapping("/kpis")
    @Operation(summary = "Dashboard global de KPIs: totales, MTTR/MTTD global y Uptime. RF-09")
    public ResponseEntity<KpiDashboardDTO> kpis(
            @RequestParam(required = false) String desde,
            @RequestParam(required = false) String hasta) {
        java.time.LocalDateTime d = desde != null ? java.time.LocalDateTime.parse(desde) : null;
        java.time.LocalDateTime h = hasta != null ? java.time.LocalDateTime.parse(hasta) : null;
        return ResponseEntity.ok(analiticoService.dashboardGlobal(d, h));
    }
}

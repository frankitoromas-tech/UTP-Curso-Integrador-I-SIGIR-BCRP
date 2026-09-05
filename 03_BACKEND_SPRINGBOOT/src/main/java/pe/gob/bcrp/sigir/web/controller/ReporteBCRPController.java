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
import pe.gob.bcrp.sigir.service.SftReportService;

import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/api/v1/reportes")
@Tag(name = "Reportes Regulatorios BCRP", description = "Generación y descarga de archivos normativos SFT en texto plano con hash SHA-256")
public class ReporteBCRPController {

    private final SftReportService reportService;

    public ReporteBCRPController(SftReportService reportService) {
        this.reportService = reportService;
    }

    @PostMapping("/generar-sft")
    @Operation(summary = "Compilar lote de incidentes cerrados en formato oficial SFT BCRP (.TXT)")
    public ResponseEntity<ReporteBCRP> generarSft(@AuthenticationPrincipal UserDetails userDetails) {
        String usuario = (userDetails != null) ? userDetails.getUsername() : "supervisor.bcrp";
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
}

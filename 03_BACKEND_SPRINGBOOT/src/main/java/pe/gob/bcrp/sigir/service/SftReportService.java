package pe.gob.bcrp.sigir.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.gob.bcrp.sigir.domain.entity.Incidente;
import pe.gob.bcrp.sigir.domain.entity.ReporteBCRP;
import pe.gob.bcrp.sigir.repository.IncidenteRepository;
import pe.gob.bcrp.sigir.repository.ReporteBCRPRepository;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class SftReportService {

    private static final Logger log = LoggerFactory.getLogger(SftReportService.class);
    private static final DateTimeFormatter FORMATO_NORM = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss");

    private final IncidenteRepository incidenteRepository;
    private final ReporteBCRPRepository reporteRepository;

    public SftReportService(IncidenteRepository incidenteRepository, ReporteBCRPRepository reporteRepository) {
        this.incidenteRepository = incidenteRepository;
        this.reporteRepository = reporteRepository;
    }

    @Transactional
    public ReporteBCRP generarReporteNormativoSFT(String usuarioEjecutor) {
        List<Incidente> cerrados = incidenteRepository.findIncidentesListosParaReporteNormativo();
        String timestamp = LocalDateTime.now().format(FORMATO_NORM);
        String numeroEnvio = "SFT-BCRP-" + timestamp;

        StringBuilder sb = new StringBuilder();

        // 1. REGISTRO HEADER (Cabecera): HEADER|NUM_ENVIO|TIMESTAMP|CANTIDAD_REGISTROS
        sb.append("HEADER|")
          .append(numeroEnvio).append("|")
          .append(timestamp).append("|")
          .append(cerrados.size()).append("\n");

        // 2. REGISTROS DETALLE: DETALLE|TICKET|COD_ENTIDAD|ENTIDAD|CATEGORIA|SEVERIDAD|FECHA_INI|FECHA_FIN|ORIGEN|SERVICIO
        for (Incidente inc : cerrados) {
            String fechaFinStr = inc.getFechaHoraSolucion() != null 
                    ? inc.getFechaHoraSolucion().format(FORMATO_NORM) 
                    : "NO_RESUELTO";

            sb.append("DETALLE|")
              .append(inc.getCodigoTicket()).append("|")
              .append(inc.getEntidad().getCodigoBcrp()).append("|")
              .append(inc.getEntidad().getNombreComercial()).append("|")
              .append(inc.getCategoria().getCodigoNormativo()).append("|")
              .append(inc.getSeveridad().name()).append("|")
              .append(inc.getFechaHoraInicio().format(FORMATO_NORM)).append("|")
              .append(fechaFinStr).append("|")
              .append(inc.getOrigenDeteccion().name()).append("|")
              .append(inc.getServicioAfectado())
              .append("\n");
        }

        // 3. REGISTRO FOOTER (Pie con Hash SHA-256 de Integridad)
        String cuerpo = sb.toString();
        String sha256 = calcularHashSha256(cuerpo);
        sb.append("FOOTER|").append(sha256);

        ReporteBCRP reporte = ReporteBCRP.builder()
                .numeroEnvio(numeroEnvio)
                .fechaGeneracion(LocalDateTime.now())
                .nombreArchivo("INCIDENTES_SFT_" + timestamp + ".txt")
                .totalRegistros(cerrados.size())
                .hashSha256(sha256)
                .estadoEnvio("PENDIENTE")
                .contenidoPlano(sb.toString())
                .usuarioGenerador(usuarioEjecutor)
                .build();

        ReporteBCRP guardado = reporteRepository.save(reporte);
        log.info("Reporte SFT BCRP compilado exitosamente: {} con Hash SHA-256: {}", numeroEnvio, sha256);
        return guardado;
    }

    @Transactional(readOnly = true)
    public List<ReporteBCRP> listarReportes() {
        return reporteRepository.findAll();
    }

    @Transactional(readOnly = true)
    public ReporteBCRP obtenerPorId(Long id) {
        return reporteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Reporte BCRP no encontrado con ID: " + id));
    }

    private String calcularHashSha256(String texto) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(texto.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error criptográfico generando hash SHA-256 del reporte BCRP", e);
        }
    }
}

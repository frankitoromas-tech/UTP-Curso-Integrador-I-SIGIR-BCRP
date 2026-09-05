package pe.gob.bcrp.sigir.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.gob.bcrp.sigir.domain.entity.CategoriaIncidente;
import pe.gob.bcrp.sigir.domain.entity.EntidadFinanciera;
import pe.gob.bcrp.sigir.domain.entity.Incidente;
import pe.gob.bcrp.sigir.domain.entity.ReporteBCRP;
import pe.gob.bcrp.sigir.domain.enums.EstadoIncidente;
import pe.gob.bcrp.sigir.domain.enums.OrigenDeteccion;
import pe.gob.bcrp.sigir.domain.enums.Severidad;
import pe.gob.bcrp.sigir.repository.IncidenteRepository;
import pe.gob.bcrp.sigir.repository.ReporteBCRPRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SftReportServiceTest {

    @Mock
    private IncidenteRepository incidenteRepository;

    @Mock
    private ReporteBCRPRepository reporteRepository;

    @InjectMocks
    private SftReportService sftReportService;

    private Incidente incidenteCerrado;

    @BeforeEach
    void setUp() {
        EntidadFinanciera entidad = EntidadFinanciera.builder()
                .idEntidad(1L)
                .codigoBcrp("002")
                .razonSocial("Banco de Crédito del Perú")
                .nombreComercial("BCP (Yape)")
                .canalInteroperable("BILLETERA_MOVIL")
                .activo(true)
                .build();

        CategoriaIncidente categoria = CategoriaIncidente.builder()
                .idCategoria(1L)
                .codigoNormativo("DISP_SERV")
                .nombre("Disponibilidad de Servicios")
                .descripcion("Caída de APIs")
                .esDeteccionAutomatica(true)
                .build();

        incidenteCerrado = Incidente.builder()
                .idIncidente(50L)
                .codigoTicket("INC-20260903-10001")
                .entidad(entidad)
                .categoria(categoria)
                .severidad(Severidad.CRITICA)
                .origenDeteccion(OrigenDeteccion.AUTOMATICO)
                .estadoActual(EstadoIncidente.CERRADO)
                .fechaHoraInicio(LocalDateTime.of(2026, 9, 3, 10, 0))
                .fechaHoraDeteccion(LocalDateTime.of(2026, 9, 3, 10, 1))
                .fechaHoraSolucion(LocalDateTime.of(2026, 9, 3, 10, 45))
                .servicioAfectado("SWITCH_PAGOS")
                .descripcionDetallada("Falla mitigada")
                .usuarioCreador("SISTEMA_DAEMON")
                .build();
    }

    @Test
    @DisplayName("Debe compilar reporte SFT BCRP con estructura delimitada por pipes y hash SHA-256 válido")
    void testGenerarReporteNormativoConRegistros() {
        when(incidenteRepository.findIncidentesListosParaReporteNormativo()).thenReturn(List.of(incidenteCerrado));
        when(reporteRepository.save(any(ReporteBCRP.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ReporteBCRP reporte = sftReportService.generarReporteNormativoSFT("auditor.bcrp");

        assertNotNull(reporte);
        assertEquals(1, reporte.getTotalRegistros());
        assertEquals("PENDIENTE", reporte.getEstadoEnvio());
        assertEquals("auditor.bcrp", reporte.getUsuarioGenerador());
        assertNotNull(reporte.getHashSha256());
        assertEquals(64, reporte.getHashSha256().length(), "El hash SHA-256 debe tener exactamente 64 caracteres hexadecimales");

        String texto = reporte.getContenidoPlano();
        assertTrue(texto.contains("HEADER|SFT-BCRP-"), "Debe contener cabecera normativa con número de envío");
        assertTrue(texto.contains("DETALLE|INC-20260903-10001|002|BCP (Yape)|DISP_SERV|CRITICA"), "Debe contener detalle formateado");
        assertTrue(texto.contains("FOOTER|" + reporte.getHashSha256()), "El pie debe incluir el hash SHA-256 de integridad");

        verify(reporteRepository, times(1)).save(any(ReporteBCRP.class));
    }

    @Test
    @DisplayName("Debe compilar reporte SFT vacío correctamente cuando no hay incidentes cerrados")
    void testGenerarReporteNormativoVacio() {
        when(incidenteRepository.findIncidentesListosParaReporteNormativo()).thenReturn(Collections.emptyList());
        when(reporteRepository.save(any(ReporteBCRP.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ReporteBCRP reporte = sftReportService.generarReporteNormativoSFT("operador.noc");

        assertNotNull(reporte);
        assertEquals(0, reporte.getTotalRegistros());
        assertNotNull(reporte.getHashSha256());
        assertTrue(reporte.getContenidoPlano().contains("|0\nFOOTER|"), "El conteo en header debe ser 0");
    }
}

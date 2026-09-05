package pe.gob.bcrp.sigir.worker;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import pe.gob.bcrp.sigir.domain.entity.EntidadFinanciera;
import pe.gob.bcrp.sigir.repository.EntidadFinancieraRepository;
import pe.gob.bcrp.sigir.service.IIncidenteService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TelemetriaWorkerTest {

    @Mock
    private EntidadFinancieraRepository entidadRepository;

    @Mock
    private IIncidenteService incidenteService;

    @Mock
    private RestTemplate restTemplate;

    private TelemetriaWorker telemetriaWorker;
    private EntidadFinanciera entidadYape;

    @BeforeEach
    void setUp() {
        telemetriaWorker = new TelemetriaWorker(entidadRepository, incidenteService, restTemplate);

        entidadYape = EntidadFinanciera.builder()
                .idEntidad(1L)
                .codigoBcrp("002")
                .razonSocial("Banco de Crédito del Perú")
                .nombreComercial("BCP (Yape)")
                .canalInteroperable("BILLETERA_MOVIL")
                .urlHealthcheck("https://api.yape.pe/health")
                .activo(true)
                .build();
    }

    @AfterEach
    void tearDown() {
        telemetriaWorker.shutdown();
    }

    @Test
    @DisplayName("Debe ignorar registro de incidentes cuando la entidad responde HTTP 200 OK")
    void testSondeoEntidadSaludable() {
        when(entidadRepository.findByActivoTrue()).thenReturn(List.of(entidadYape));
        when(restTemplate.getForEntity(eq("https://api.yape.pe/health"), eq(String.class)))
                .thenReturn(new ResponseEntity<>("{\"status\":\"UP\"}", HttpStatus.OK));

        telemetriaWorker.ejecutarCicloSondeoTelemetria();

        verify(incidenteService, never()).registrarIncidenteAutomatico(any(), any(), any());
    }

    @Test
    @DisplayName("Debe auto-registrar incidente cuando la entidad responde HTTP 503 Server Error y no hay ticket previo")
    void testSondeoEntidadConFalla503() {
        when(entidadRepository.findByActivoTrue()).thenReturn(List.of(entidadYape));
        when(restTemplate.getForEntity(eq("https://api.yape.pe/health"), eq(String.class)))
                .thenReturn(new ResponseEntity<>("Service Unavailable", HttpStatus.SERVICE_UNAVAILABLE));
        when(incidenteService.existeIncidenteAbiertoParaEntidad(1L)).thenReturn(false);

        telemetriaWorker.ejecutarCicloSondeoTelemetria();

        verify(incidenteService, times(1))
                .registrarIncidenteAutomatico(eq(entidadYape), contains("HTTP 5xx Server Error"), any());
    }

    @Test
    @DisplayName("Debe auto-registrar incidente cuando la entidad sufre timeout de red y no hay ticket previo")
    void testSondeoEntidadConTimeout() {
        when(entidadRepository.findByActivoTrue()).thenReturn(List.of(entidadYape));
        when(restTemplate.getForEntity(eq("https://api.yape.pe/health"), eq(String.class)))
                .thenThrow(new ResourceAccessException("Connection timed out"));
        when(incidenteService.existeIncidenteAbiertoParaEntidad(1L)).thenReturn(false);

        telemetriaWorker.ejecutarCicloSondeoTelemetria();

        verify(incidenteService, times(1))
                .registrarIncidenteAutomatico(eq(entidadYape), contains("Timeout o Conexión Rechazada"), any());
    }
}

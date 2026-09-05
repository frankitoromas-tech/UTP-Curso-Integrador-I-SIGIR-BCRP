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
import pe.gob.bcrp.sigir.domain.entity.HistorialEstado;
import pe.gob.bcrp.sigir.domain.entity.Incidente;
import pe.gob.bcrp.sigir.domain.enums.EstadoIncidente;
import pe.gob.bcrp.sigir.domain.enums.OrigenDeteccion;
import pe.gob.bcrp.sigir.domain.enums.Severidad;
import pe.gob.bcrp.sigir.repository.CategoriaIncidenteRepository;
import pe.gob.bcrp.sigir.repository.EntidadFinancieraRepository;
import pe.gob.bcrp.sigir.repository.HistorialEstadoRepository;
import pe.gob.bcrp.sigir.repository.IncidenteRepository;
import pe.gob.bcrp.sigir.web.dto.CambioEstadoDTO;
import pe.gob.bcrp.sigir.web.dto.IncidenteRegistroDTO;
import pe.gob.bcrp.sigir.web.dto.IncidenteResponseDTO;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IncidenteServiceTest {

    @Mock
    private IncidenteRepository incidenteRepository;

    @Mock
    private EntidadFinancieraRepository entidadRepository;

    @Mock
    private CategoriaIncidenteRepository categoriaRepository;

    @Mock
    private HistorialEstadoRepository historialRepository;

    @InjectMocks
    private IncidenteService incidenteService;

    private EntidadFinanciera entidadYape;
    private CategoriaIncidente categoriaFraude;
    private Incidente incidenteExistente;

    @BeforeEach
    void setUp() {
        entidadYape = EntidadFinanciera.builder()
                .idEntidad(1L)
                .codigoBcrp("002")
                .razonSocial("Banco de Crédito del Perú")
                .nombreComercial("BCP (Yape)")
                .canalInteroperable("BILLETERA_MOVIL")
                .activo(true)
                .build();

        categoriaFraude = CategoriaIncidente.builder()
                .idCategoria(2L)
                .codigoNormativo("FRAUDE_FIN")
                .nombre("Fraude Financiero")
                .descripcion("Transacciones no autorizadas")
                .esDeteccionAutomatica(false)
                .build();

        incidenteExistente = Incidente.builder()
                .idIncidente(100L)
                .codigoTicket("INC-20260903-10045")
                .entidad(entidadYape)
                .categoria(categoriaFraude)
                .severidad(Severidad.ALTA)
                .origenDeteccion(OrigenDeteccion.MANUAL_OPERADOR)
                .estadoActual(EstadoIncidente.REGISTRADO)
                .fechaHoraInicio(LocalDateTime.now().minusHours(1))
                .fechaHoraDeteccion(LocalDateTime.now())
                .servicioAfectado("PASARELA_QR")
                .descripcionDetallada("Transacciones no reconocidas")
                .impactoEstimadoUsuarios(50)
                .usuarioCreador("frank.vargas@bcrp.local")
                .build();
    }

    @Test
    @DisplayName("Debe registrar un incidente manual exitosamente cuando los datos son válidos")
    void testRegistrarIncidenteManualExitoso() {
        IncidenteRegistroDTO dto = IncidenteRegistroDTO.builder()
                .idEntidad(1L)
                .codigoCategoria("FRAUDE_FIN")
                .severidad(Severidad.ALTA)
                .fechaHoraInicio(LocalDateTime.now().minusHours(1))
                .servicioAfectado("PASARELA_QR")
                .descripcionDetallada("Anomalía detectada en QR")
                .impactoEstimadoUsuarios(20)
                .build();

        when(entidadRepository.findById(1L)).thenReturn(Optional.of(entidadYape));
        when(categoriaRepository.findByCodigoNormativo("FRAUDE_FIN")).thenReturn(Optional.of(categoriaFraude));
        when(incidenteRepository.existeIncidenteAbiertoParaEntidad(1L)).thenReturn(false);
        when(incidenteRepository.save(any(Incidente.class))).thenAnswer(invocation -> {
            Incidente i = invocation.getArgument(0);
            i.setIdIncidente(101L);
            return i;
        });

        IncidenteResponseDTO resultado = incidenteService.registrarIncidenteManual(dto, "frank.vargas@bcrp.local");

        assertNotNull(resultado);
        assertEquals("BCP (Yape)", resultado.getNombreEntidad());
        assertEquals("FRAUDE_FIN", resultado.getCodigoCategoria());
        assertEquals(EstadoIncidente.REGISTRADO, resultado.getEstadoActual());
        verify(historialRepository, times(1)).save(any(HistorialEstado.class));
    }

    @Test
    @DisplayName("Debe lanzar excepción si la entidad ya cuenta con un incidente activo no cerrado")
    void testRegistrarIncidenteFallaPorIncidenteActivo() {
        IncidenteRegistroDTO dto = IncidenteRegistroDTO.builder()
                .idEntidad(1L)
                .codigoCategoria("FRAUDE_FIN")
                .severidad(Severidad.ALTA)
                .fechaHoraInicio(LocalDateTime.now())
                .servicioAfectado("PASARELA_QR")
                .descripcionDetallada("Duplicado")
                .build();

        when(entidadRepository.findById(1L)).thenReturn(Optional.of(entidadYape));
        when(categoriaRepository.findByCodigoNormativo("FRAUDE_FIN")).thenReturn(Optional.of(categoriaFraude));
        when(incidenteRepository.existeIncidenteAbiertoParaEntidad(1L)).thenReturn(true);

        assertThrows(IllegalStateException.class, () -> {
            incidenteService.registrarIncidenteManual(dto, "frank.vargas@bcrp.local");
        });
        verify(incidenteRepository, never()).save(any(Incidente.class));
    }

    @Test
    @DisplayName("Debe transicionar de estado correctamente y registrar en historial de auditoría")
    void testCambiarEstadoExitoso() {
        CambioEstadoDTO dto = CambioEstadoDTO.builder()
                .nuevoEstado(EstadoIncidente.EN_EVALUACION)
                .comentarioTecnico("Iniciando análisis forense")
                .build();

        when(incidenteRepository.findById(100L)).thenReturn(Optional.of(incidenteExistente));
        when(incidenteRepository.save(any(Incidente.class))).thenReturn(incidenteExistente);

        IncidenteResponseDTO response = incidenteService.cambiarEstado(100L, dto, "frank.vargas@bcrp.local");

        assertNotNull(response);
        assertEquals(EstadoIncidente.EN_EVALUACION, response.getEstadoActual());
        verify(historialRepository, times(1)).save(any(HistorialEstado.class));
    }
}

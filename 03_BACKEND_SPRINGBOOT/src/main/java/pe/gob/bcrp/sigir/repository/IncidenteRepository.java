package pe.gob.bcrp.sigir.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.gob.bcrp.sigir.domain.entity.Incidente;
import pe.gob.bcrp.sigir.domain.enums.EstadoIncidente;
import pe.gob.bcrp.sigir.domain.enums.Severidad;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface IncidenteRepository extends JpaRepository<Incidente, Long> {

    Optional<Incidente> findByCodigoTicket(String codigoTicket);

    List<Incidente> findByEstadoActual(EstadoIncidente estadoActual);

    List<Incidente> findByEntidadIdEntidad(Long idEntidad);

    List<Incidente> findBySeveridad(Severidad severidad);

    @Query("SELECT i FROM Incidente i WHERE i.estadoActual != pe.gob.bcrp.sigir.domain.enums.EstadoIncidente.CERRADO AND i.entidad.idEntidad = :idEntidad")
    List<Incidente> findIncidentesAbiertosPorEntidad(@Param("idEntidad") Long idEntidad);

    @Query("SELECT COUNT(i) > 0 FROM Incidente i WHERE i.estadoActual != pe.gob.bcrp.sigir.domain.enums.EstadoIncidente.CERRADO AND i.entidad.idEntidad = :idEntidad")
    boolean existeIncidenteAbiertoParaEntidad(@Param("idEntidad") Long idEntidad);

    @Query("SELECT i FROM Incidente i WHERE i.estadoActual = pe.gob.bcrp.sigir.domain.enums.EstadoIncidente.CERRADO ORDER BY i.fechaHoraSolucion DESC")
    List<Incidente> findIncidentesListosParaReporteNormativo();

    @Query("SELECT i FROM Incidente i WHERE i.fechaHoraInicio BETWEEN :desde AND :hasta ORDER BY i.fechaHoraInicio DESC")
    List<Incidente> findPorRangoFechas(@Param("desde") LocalDateTime desde, @Param("hasta") LocalDateTime hasta);
}

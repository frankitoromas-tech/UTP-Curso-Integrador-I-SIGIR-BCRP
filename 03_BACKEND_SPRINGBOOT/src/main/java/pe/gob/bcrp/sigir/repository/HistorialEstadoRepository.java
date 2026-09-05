package pe.gob.bcrp.sigir.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.gob.bcrp.sigir.domain.entity.HistorialEstado;

import java.util.List;

@Repository
public interface HistorialEstadoRepository extends JpaRepository<HistorialEstado, Long> {
    List<HistorialEstado> findByIncidenteIdIncidenteOrderByFechaTransicionAsc(Long idIncidente);
}

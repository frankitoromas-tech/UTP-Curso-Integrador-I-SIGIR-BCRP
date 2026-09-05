package pe.gob.bcrp.sigir.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.gob.bcrp.sigir.domain.entity.CategoriaIncidente;

import java.util.Optional;

@Repository
public interface CategoriaIncidenteRepository extends JpaRepository<CategoriaIncidente, Long> {
    Optional<CategoriaIncidente> findByCodigoNormativo(String codigoNormativo);
}

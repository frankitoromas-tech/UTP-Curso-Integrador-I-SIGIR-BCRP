package pe.gob.bcrp.sigir.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.gob.bcrp.sigir.domain.entity.ReporteBCRP;

import java.util.Optional;

@Repository
public interface ReporteBCRPRepository extends JpaRepository<ReporteBCRP, Long> {
    Optional<ReporteBCRP> findByNumeroEnvio(String numeroEnvio);
}

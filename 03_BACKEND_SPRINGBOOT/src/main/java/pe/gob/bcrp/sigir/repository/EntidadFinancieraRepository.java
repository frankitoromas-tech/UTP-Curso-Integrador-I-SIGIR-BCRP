package pe.gob.bcrp.sigir.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.gob.bcrp.sigir.domain.entity.EntidadFinanciera;

import java.util.List;
import java.util.Optional;

@Repository
public interface EntidadFinancieraRepository extends JpaRepository<EntidadFinanciera, Long> {
    List<EntidadFinanciera> findByActivoTrue();
    Optional<EntidadFinanciera> findByCodigoBcrp(String codigoBcrp);
    boolean existsByCodigoBcrp(String codigoBcrp);
}

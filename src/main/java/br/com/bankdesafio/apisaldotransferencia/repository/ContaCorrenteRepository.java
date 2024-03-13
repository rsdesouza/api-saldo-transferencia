package br.com.bankdesafio.apisaldotransferencia.repository;

import br.com.bankdesafio.apisaldotransferencia.model.ContaCorrente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ContaCorrenteRepository extends JpaRepository<ContaCorrente, UUID> {

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM ContaCorrente c WHERE c.id = ?1 AND c.ativa = true")
    boolean existsByIdAndAtivaTrue(UUID id);
}

package br.com.bankdesafio.apisaldotransferencia.repository;

import br.com.bankdesafio.apisaldotransferencia.model.ContaCorrente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ContaCorrenteRepository extends JpaRepository<ContaCorrente, UUID> {
}

package br.com.bankdesafio.apisaldotransferencia.repository;

import br.com.bankdesafio.apisaldotransferencia.model.Transacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface TransacaoRepository extends JpaRepository<Transacao, UUID> {
}

package br.com.bankdesafio.apisaldotransferencia.repository;

import br.com.bankdesafio.apisaldotransferencia.model.Transacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TransacaoRepository extends JpaRepository<Transacao, UUID> {
}

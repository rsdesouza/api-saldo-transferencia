package br.com.bankdesafio.apisaldotransferencia.repository;

import br.com.bankdesafio.apisaldotransferencia.model.ContaCorrente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ContaCorrenteRepository extends JpaRepository<ContaCorrente, String> {
    @Query("SELECT c FROM ContaCorrente c WHERE c.id IN (?1, ?2)")
    List<ContaCorrente> findContasByIds(String idContaOrigem, String idContaDestino);

    @Query("SELECT c FROM ContaCorrente c WHERE c.id = :id AND c.ativa = TRUE")
    Optional<ContaCorrente> findContaAtivaById(String id);

    @Modifying
    @Query("UPDATE ContaCorrente c SET c.saldo = c.saldo - :valor, c.totalTransferidoHoje = c.totalTransferidoHoje + :valor " +
            "WHERE c.id = :idContaOrigem AND c.saldo >= :valor AND (c.totalTransferidoHoje + :valor) <= c.limiteDiario")
    int atualizarSaldoContaOrigemComLimiteDiario(String idContaOrigem, BigDecimal valor);


    @Modifying
    @Query("UPDATE ContaCorrente c SET c.saldo = c.saldo + :valor WHERE c.id = :idContaDestino")
    int atualizarSaldoContaDestino(String idContaDestino, BigDecimal valor);


}

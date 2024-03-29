package br.com.bankdesafio.apisaldotransferencia.service.validation;

import br.com.bankdesafio.apisaldotransferencia.dto.TransferenciaDTO;
import br.com.bankdesafio.apisaldotransferencia.exception.ContaInativaException;
import br.com.bankdesafio.apisaldotransferencia.exception.ContaNotFoundException;
import br.com.bankdesafio.apisaldotransferencia.exception.LimiteDiarioExcedidoException;
import br.com.bankdesafio.apisaldotransferencia.exception.SaldoInsuficienteException;
import br.com.bankdesafio.apisaldotransferencia.model.ContaCorrente;
import br.com.bankdesafio.apisaldotransferencia.repository.ContaCorrenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TransferenciaValidationService {

    private final ContaCorrenteRepository contaCorrenteRepository;

    @Autowired
    public TransferenciaValidationService(ContaCorrenteRepository contaCorrenteRepository) {
        this.contaCorrenteRepository = contaCorrenteRepository;
    }

    public void validarTransferencia(TransferenciaDTO transferenciaDTO) {
        validarContasAtivas(transferenciaDTO.getIdContaOrigem(), transferenciaDTO.getIdContaDestino());
        validarSaldoContaOrigem(transferenciaDTO.getIdContaOrigem(), transferenciaDTO.getValor());
        validarLimiteDiarioContaOrigem(transferenciaDTO.getIdContaOrigem(), transferenciaDTO.getValor());
    }

    private void validarContasAtivas(String idContaOrigem, String idContaDestino) {
        List<ContaCorrente> contas = contaCorrenteRepository.findContasByIds(idContaOrigem, idContaDestino);

        if (contas.size() < 2) {
            throw new ContaNotFoundException("Uma ou ambas as contas não foram encontradas.");
        }

        contas.forEach(conta -> {
            if (!conta.getAtiva()) {
                throw new ContaInativaException("Conta " + conta.getId() + " inativa.");
            }
        });
    }

    private void validarSaldoContaOrigem(String idContaOrigem, BigDecimal valorTransferencia) {
        ContaCorrente contaOrigem = contaCorrenteRepository.findById(idContaOrigem)
                .orElseThrow(() -> new ContaNotFoundException("Conta de origem não encontrada."));

        if (contaOrigem.getSaldo().compareTo(valorTransferencia) < 0) {
            throw new SaldoInsuficienteException("Saldo insuficiente para realizar a transferência.");
        }
    }

    private void validarLimiteDiarioContaOrigem(String idContaOrigem, BigDecimal valorTransferencia) {
        ContaCorrente contaOrigem = contaCorrenteRepository.findById(idContaOrigem)
                .orElseThrow(() -> new ContaNotFoundException("Conta de origem não encontrada."));

        BigDecimal totalPermitidoParaHoje = contaOrigem.getLimiteDiario();
        BigDecimal totalTransferidoHoje = obterTotalTransferidoHoje(contaOrigem);

        if (totalTransferidoHoje.add(valorTransferencia).compareTo(totalPermitidoParaHoje) > 0) {
            throw new LimiteDiarioExcedidoException("A transferência excede o limite diário permitido para a conta de origem.");
        }
    }

    private BigDecimal obterTotalTransferidoHoje(ContaCorrente contaOrigemId) {

        BigDecimal totalTransferidoHoje = contaOrigemId.getTotalTransferidoHoje();

        return totalTransferidoHoje != null ? totalTransferidoHoje : BigDecimal.ZERO;
    }
}

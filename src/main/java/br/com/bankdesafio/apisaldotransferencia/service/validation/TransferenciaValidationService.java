package br.com.bankdesafio.apisaldotransferencia.service.validation;

import br.com.bankdesafio.apisaldotransferencia.dto.TransferenciaDTO;
import br.com.bankdesafio.apisaldotransferencia.exception.ContaInativaException;
import br.com.bankdesafio.apisaldotransferencia.model.ContaCorrente;
import br.com.bankdesafio.apisaldotransferencia.repository.ContaCorrenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

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
        if (!contaCorrenteRepository.existsByIdAndAtivaTrue(idContaOrigem)) {
            throw new ContaInativaException("Conta de origem inativa ou não encontrada.");
        }
        if (!contaCorrenteRepository.existsByIdAndAtivaTrue(idContaDestino)) {
            throw new ContaInativaException("Conta de destino inativa ou não encontrada.");
        }
    }

    private void validarSaldoContaOrigem(String idContaOrigem, BigDecimal valorTransferencia) {
        ContaCorrente contaOrigem = contaCorrenteRepository.findById(idContaOrigem)
                .orElseThrow(() -> new IllegalArgumentException("Conta de origem não encontrada."));

        if (contaOrigem.getSaldo().compareTo(valorTransferencia) < 0) {
            throw new IllegalArgumentException("Saldo insuficiente para realizar a transferência.");
        }
    }

    private void validarLimiteDiarioContaOrigem(String idContaOrigem, BigDecimal valorTransferencia) {
        ContaCorrente contaOrigem = contaCorrenteRepository.findById(idContaOrigem)
                .orElseThrow(() -> new IllegalArgumentException("Conta de origem não encontrada."));

        BigDecimal totalPermitidoParaHoje = contaOrigem.getLimiteDiario();
        BigDecimal totalTransferidoHoje = obterTotalTransferidoHoje(contaOrigem);

        if (totalTransferidoHoje.add(valorTransferencia).compareTo(totalPermitidoParaHoje) > 0) {
            throw new IllegalArgumentException("A transferência excede o limite diário permitido para a conta de origem.");
        }
    }

    private BigDecimal obterTotalTransferidoHoje(ContaCorrente contaOrigemId) {

        BigDecimal totalTransferidoHoje = contaOrigemId.getTotalTransferidoHoje();

        return totalTransferidoHoje != null ? totalTransferidoHoje : BigDecimal.ZERO;
    }
}

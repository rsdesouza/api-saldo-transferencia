package br.com.bankdesafio.apisaldotransferencia.service;

import br.com.bankdesafio.apisaldotransferencia.dto.SaldoDTO;
import br.com.bankdesafio.apisaldotransferencia.dto.TransferenciaDTO;
import br.com.bankdesafio.apisaldotransferencia.exception.ContaNotFoundException;
import br.com.bankdesafio.apisaldotransferencia.exception.LimiteDiarioExcedidoException;
import br.com.bankdesafio.apisaldotransferencia.model.ContaCorrente;
import br.com.bankdesafio.apisaldotransferencia.repository.ContaCorrenteRepository;
import br.com.bankdesafio.apisaldotransferencia.service.validation.ContaCorrenteValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class ContaCorrenteService {

    private final ContaCorrenteRepository contaCorrenteRepository;
    private final ContaCorrenteValidationService contaCorrenteValidationService;

    @Autowired
    public ContaCorrenteService(ContaCorrenteRepository contaCorrenteRepository, ContaCorrenteValidationService contaCorrenteValidationService) {
        this.contaCorrenteRepository = contaCorrenteRepository;
        this.contaCorrenteValidationService = contaCorrenteValidationService;
    }

    public SaldoDTO consultarSaldo(String id) {
        ContaCorrente conta = contaCorrenteValidationService.validarContaAtiva(id);

        SaldoDTO saldo = new SaldoDTO();
        saldo.setIdConta(conta.getId());
        saldo.setSaldo(conta.getSaldo());

        return saldo;
    }

    @Transactional
    public void desativarConta(String id) {
        ContaCorrente conta = contaCorrenteValidationService.validarExistenciaConta(id);
        conta.setAtiva(false);
        contaCorrenteRepository.save(conta);
    }

    @Transactional
    public ContaCorrente atualizarSaldo(TransferenciaDTO transferenciaDTO) {

        ContaCorrente contaOrigem = contaCorrenteRepository.findById(transferenciaDTO.getIdContaOrigem())
                .orElseThrow(() -> new ContaNotFoundException("Conta corrente de origem não encontrada."));

        ContaCorrente contaDestino = contaCorrenteRepository.findById(transferenciaDTO.getIdContaDestino())
                .orElseThrow(() -> new ContaNotFoundException("Conta corrente de destino não encontrada."));

        // Atualizar o saldo da conta de origem
        BigDecimal novoSaldoOrigem = contaOrigem.getSaldo().subtract(transferenciaDTO.getValor());
        contaOrigem.setSaldo(novoSaldoOrigem);

        // Atualizar o saldo da conta de destino
        BigDecimal novoSaldoDestino = contaDestino.getSaldo().add(transferenciaDTO.getValor());
        contaDestino.setSaldo(novoSaldoDestino);

        // Persistir as alterações no banco de dados
        contaCorrenteRepository.save(contaOrigem);
        contaCorrenteRepository.save(contaDestino);

        return contaOrigem;
    }


    @Transactional
    public void atualizarTotalTransferidoHoje(ContaCorrente contaOrigem, BigDecimal valorTransferencia) {
        BigDecimal totalTransferidoHojeAtualizado = contaOrigem.getTotalTransferidoHoje().add(valorTransferencia);

        // Verificar se o total transferido hoje excede o limite diário
        if (totalTransferidoHojeAtualizado.compareTo(contaOrigem.getLimiteDiario()) > 0) {
            throw new LimiteDiarioExcedidoException("A transferência excede o limite diário permitido para a conta de origem.");
        }

        // Atualizar o total transferido hoje na conta de origem
        contaOrigem.setTotalTransferidoHoje(totalTransferidoHojeAtualizado);

        // Salvar a conta com o total transferido hoje atualizado
        contaCorrenteRepository.save(contaOrigem);
    }

}

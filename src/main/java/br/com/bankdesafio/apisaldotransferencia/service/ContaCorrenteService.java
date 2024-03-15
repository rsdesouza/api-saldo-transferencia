package br.com.bankdesafio.apisaldotransferencia.service;

import br.com.bankdesafio.apisaldotransferencia.dto.SaldoDTO;
import br.com.bankdesafio.apisaldotransferencia.dto.TransferenciaDTO;
import br.com.bankdesafio.apisaldotransferencia.exception.ContaNotFoundException;
import br.com.bankdesafio.apisaldotransferencia.exception.LimiteDiarioExcedidoException;
import br.com.bankdesafio.apisaldotransferencia.exception.SaldoInsuficienteException;
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
    public void atualizarSaldo(TransferenciaDTO transferenciaDTO) {
        int rowsUpdatedOrigem = contaCorrenteRepository.atualizarSaldoContaOrigemComLimiteDiario(transferenciaDTO.getIdContaOrigem(), transferenciaDTO.getValor());
        if (rowsUpdatedOrigem == 0) {
            throw new SaldoInsuficienteException("Saldo insuficiente ou conta não encontrada.");
        }
        contaCorrenteRepository.atualizarSaldoContaDestino(transferenciaDTO.getIdContaDestino(), transferenciaDTO.getValor());
    }

}

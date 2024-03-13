package br.com.bankdesafio.apisaldotransferencia.service;

import br.com.bankdesafio.apisaldotransferencia.dto.SaldoDTO;
import br.com.bankdesafio.apisaldotransferencia.mapper.SaldoMapper;
import br.com.bankdesafio.apisaldotransferencia.model.ContaCorrente;
import br.com.bankdesafio.apisaldotransferencia.repository.ContaCorrenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
import java.util.UUID;

@Service
public class ContaCorrenteService {

    @Autowired
    private ContaCorrenteRepository contaCorrenteRepository;

    @Autowired
    private SaldoMapper saldoMapper;

    public SaldoDTO consultarSaldo(UUID id) {
        Optional<ContaCorrente> contaOpt = contaCorrenteRepository.findById(id);
        if (contaOpt.isEmpty() || !contaOpt.get().getAtiva()) {
            throw new IllegalArgumentException("Conta corrente não encontrada ou inativa.");
        }

        ContaCorrente conta = contaOpt.get();
        return saldoMapper.toDto(conta);

    }

    @Transactional
    public void desativarConta(UUID id) {
        Optional<ContaCorrente> conta = contaCorrenteRepository.findById(id);
        if (conta.isPresent()) {
            ContaCorrente contaCorrente = conta.get();
            contaCorrente.setAtiva(false);
            contaCorrenteRepository.save(contaCorrente);
        } else {
            throw new IllegalArgumentException("Conta corrente não encontrada.");
        }
    }
}
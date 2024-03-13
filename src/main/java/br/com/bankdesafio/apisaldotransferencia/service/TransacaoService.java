package br.com.bankdesafio.apisaldotransferencia.service;

import br.com.bankdesafio.apisaldotransferencia.dto.TransferenciaDTO;
import br.com.bankdesafio.apisaldotransferencia.model.ContaCorrente;
import br.com.bankdesafio.apisaldotransferencia.model.Transacao;
import br.com.bankdesafio.apisaldotransferencia.repository.ContaCorrenteRepository;
import br.com.bankdesafio.apisaldotransferencia.repository.TransacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class TransacaoService {

    private final TransacaoRepository transacaoRepository;
    private final ContaCorrenteRepository contaCorrenteRepository;

    @Autowired
    public TransacaoService(TransacaoRepository transacaoRepository, ContaCorrenteRepository contaCorrenteRepository) {
        this.transacaoRepository = transacaoRepository;
        this.contaCorrenteRepository = contaCorrenteRepository;
    }

    public Transacao criarTransacao(TransferenciaDTO transferenciaDTO, String nomeClienteOrigem, String nomeClienteDestino) {
        // Recuperar as contas correntes envolvidas na transferência
        Optional<ContaCorrente> contaOrigem = contaCorrenteRepository.findById(transferenciaDTO.getIdContaOrigem());
        Optional<ContaCorrente> contaDestino = contaCorrenteRepository.findById(transferenciaDTO.getIdContaDestino());

        if (contaOrigem.isEmpty() || contaDestino.isEmpty()) {
            throw new IllegalArgumentException("Conta de origem ou destino não encontrada.");
        }

        // Criar a transação
        Transacao transacao = new Transacao();
        transacao.setValor(transferenciaDTO.getValor());
        transacao.setDataHoraTransacao(LocalDateTime.now());
        transacao.setContaOrigem(contaOrigem.get());
        transacao.setContaDestino(contaDestino.get());
        transacao.setNomeClienteOrigem(nomeClienteOrigem);
        transacao.setNomeClienteDestino(nomeClienteDestino);

        // Salvar a transação no repositório
        return transacaoRepository.save(transacao);
    }
}

package br.com.bankdesafio.apisaldotransferencia.service;

import br.com.bankdesafio.apisaldotransferencia.dto.TransferenciaDTO;
import br.com.bankdesafio.apisaldotransferencia.model.ContaCorrente;
import br.com.bankdesafio.apisaldotransferencia.model.Transacao;
import br.com.bankdesafio.apisaldotransferencia.repository.ContaCorrenteRepository;
import br.com.bankdesafio.apisaldotransferencia.repository.TransacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        // Buscar as contas correntes envolvidas na transferência de forma otimizada
        List<ContaCorrente> contas = contaCorrenteRepository.findContasByIds(
                transferenciaDTO.getIdContaOrigem(), transferenciaDTO.getIdContaDestino());

        // Converter a lista para um mapa para facilitar o acesso por ID
        Map<String, ContaCorrente> contaMap = contas.stream()
                .collect(Collectors.toMap(ContaCorrente::getId, conta -> conta));

        // Obter as contas a partir do mapa
        ContaCorrente contaOrigem = contaMap.get(transferenciaDTO.getIdContaOrigem());
        ContaCorrente contaDestino = contaMap.get(transferenciaDTO.getIdContaDestino());

        // Verificar a existência das contas
        if (contaOrigem == null || contaDestino == null) {
            throw new IllegalArgumentException("Conta de origem ou destino não encontrada.");
        }

        // Criar a transação com as contas obtidas
        Transacao transacao = new Transacao();
        transacao.setValor(transferenciaDTO.getValor());
        transacao.setDataHoraTransacao(LocalDateTime.now());
        transacao.setContaOrigem(contaOrigem);
        transacao.setContaDestino(contaDestino);
        transacao.setNomeClienteOrigem(nomeClienteOrigem);
        transacao.setNomeClienteDestino(nomeClienteDestino);

        // Salvar a transação
        return transacaoRepository.save(transacao);
    }
}
